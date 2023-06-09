package com.atguigu.syt.hosp.controller.admin;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.admin
 * class:AdminHospitalController
 *
 * @author: smile
 * @create: 2023/6/5-18:29
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@Api("医院信息接口")
public class AdminHospitalController {
    @Resource
    private HospitalService hospitalService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取医院信息分页
     */
    @GetMapping("/page/{pageNum}/{pageSize}")
    @ApiOperation("医院信息分页展示")
    @ApiImplicitParams(value = {@ApiImplicitParam(value = "pageNum", name = "当前页"),
            @ApiImplicitParam(value = "pageSize", name = "每页数量"), @ApiImplicitParam(value = "hosname", name = "医院名称关键词")})
    public Result<?> getHospitalPageList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                         @RequestParam("hosname") String hosname) {

        Page<Hospital> page = hospitalService.getHospitalPageList(pageNum, pageSize, hosname);
        return Result.ok(page);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改医院状态
     */
    @PutMapping("/status/{hoscode}/{status}")
    @ApiOperation("修改医院状态")
    public Result<?> updateStatus(@PathVariable String hoscode, @PathVariable Integer status) {
        hospitalService.updateStatus(hoscode, status);
        return Result.ok();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:返回医院详情信息
     */
    @GetMapping("/detail/{hoscode}")
    @ApiOperation("查询医院详细信息")
    public Result<?> getHosDetail(@PathVariable String hoscode) {
        Hospital hospital = hospitalService.getHosDetail(hoscode);
        return Result.ok(hospital);
    }
}
