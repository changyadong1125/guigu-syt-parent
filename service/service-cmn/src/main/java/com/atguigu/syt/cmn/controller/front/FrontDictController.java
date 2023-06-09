package com.atguigu.syt.cmn.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.DictService;
import com.atguigu.syt.model.cmn.Dict;
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
 * class:FrontDictController
 *
 * @author: smile
 * @create: 2023/6/7-18:45
 * @Version: v1.0
 * @Description:
 */
@Api("用户界面字典接口")
@RestController
@RequestMapping("/front/cmn/dict")
public class FrontDictController {
    @Resource
    private DictService dictService;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有的数据字典类型
     */
    @GetMapping("/list/{dictTypeId}")
    @ApiImplicitParam(name = "dictTypeId" ,value = "字典类型id")
    public Result<?> getDictList(@PathVariable String dictTypeId){
        List<Dict> list = dictService.getDictList(dictTypeId);
        return Result.ok(list);
    }

}
