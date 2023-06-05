package com.atguigu.syt.cmn.controller.inner;

import com.atguigu.syt.cmn.service.DictService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.controller.inner
 * class:InnerDictController
 *
 * @author: smile
 * @create: 2023/6/5-19:07
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/inner/cmn/dict")
@Api(value = "获取数据类型")
public class InnerDictController {

    @Resource
    private DictService dictService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据hosname获取数据类型
     */
    @GetMapping("/getName/{hosType}/{dicTypeId}")
    public String getDicTypeByHosType(@PathVariable(value = "hosType") String hosType,@PathVariable(value = "dicTypeId") Long dicTypeId){
        return dictService.getDicTypeByHosType(hosType,dicTypeId);
    }
}
