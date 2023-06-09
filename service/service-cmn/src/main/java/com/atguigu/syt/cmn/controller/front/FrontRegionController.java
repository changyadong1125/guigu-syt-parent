package com.atguigu.syt.cmn.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.controller.front
 * class:FrontRegionController
 *
 * @author: smile
 * @create: 2023/6/7-18:45
 * @Version: v1.0
 * @Description:
 */
@Api("获取地区列表")
@RestController
@RequestMapping("/front/cmn/region")
public class FrontRegionController {
    @Resource
    private RegionService regionService;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取地区列表
     */
    @GetMapping("/list/{parentCode}")
    @ApiImplicitParam(name = "parentCode" ,value = "北京code")
    public Result<?> getRegionList(@PathVariable String parentCode){
        List<Region> list = regionService.getRegionListByParentCode(parentCode);
        return Result.ok(list);
    }
}
