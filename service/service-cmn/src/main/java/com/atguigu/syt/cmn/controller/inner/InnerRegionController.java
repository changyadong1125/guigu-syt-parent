package com.atguigu.syt.cmn.controller.inner;

import com.atguigu.syt.cmn.service.RegionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.controller.inner
 * class:InnerRegionController
 *
 * @author: smile
 * @create: 2023/6/5-19:06
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/inner/cmn/region")
@Api(value = "获取地区名称")
public class InnerRegionController {

    @Resource
    private RegionService regionService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据code获取地区名称
     */
    @GetMapping("/getName/{code}")
    public String getRegionNameByCode(@PathVariable(value = "code") String code){
        return regionService.getRegionNameByCode(code);
    }
}
