package com.atguigu.syt.cmn.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.listener.UploadDataListener;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@RestController
@RequestMapping("/admin/cmn/region")
@Api("地区管理")
@Slf4j
public class AdminRegionController {
    @Resource
    private RegionService regionService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有的地区信息
     */
    @GetMapping("/findRegionListByParentCode/{parentCode}")
    @ApiOperation("根据上级code获取子节点列表")
    public Result<?> findRegionListByParentCode(@ApiParam(value = "上级code") @PathVariable String parentCode) {
        List<Region> regionList = regionService.getRegionListByParentCode(parentCode);
        return Result.ok(regionList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:文件下载
     */
    @SuppressWarnings("all")
    @GetMapping("/download")
    @ApiOperation(value = "文件下载")
    public void download(HttpServletResponse response) {
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<RegionExcelVo> regionExcelVoList = regionService.getRegionList();
            EasyExcel.write(response.getOutputStream(), RegionExcelVo.class).sheet("模板").doWrite(regionExcelVoList);
        } catch (IOException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            response.reset();
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            try {
                response.getWriter().println("导出失败");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:文件上传
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public Result<?> upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), RegionExcelVo.class, new UploadDataListener(regionService)).sheet().doRead();
        return Result.ok();
    }
}

