package com.atguigu.syt.cmn.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@RestController
@RequestMapping("/admin/cmn/region")
@Api("地区管理")
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
    public Result<?> findRegionListByParentCode(@ApiParam(value = "上级code") @PathVariable String parentCode){
      List<Region> regionList =  regionService.getRegionListByParentCode(parentCode);
      return Result.ok(regionList);
    }
}

