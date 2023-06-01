package com.atguigu.syt.cmn.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.DictTypeService;
import com.atguigu.syt.vo.cmn.DictTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/admin/cmn/dict")
@Api(value = "数据字典")
public class AdminDictController {
    @Resource
    private DictTypeService dictTypeService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取全部数据字典
     */
    @GetMapping("/findAll")
    @ApiOperation("获取所有的数据字典")
    public Result<?> findAll(){
     // List<DictTypeVo> dictTypeList = dictTypeService.getDictTypeLists();
        List<DictTypeVo> dictTypeList = dictTypeService.findAll_xml();
        return Result.ok(dictTypeList);
    }
}

