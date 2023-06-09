package com.atguigu.syt.hosp.controller.admin;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.admin
 * class:AdminDepartmentController
 *
 * @author: smile
 * @create: 2023/6/6-15:52
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/admin/hosp/department")
@Api("医院科室文档")
public class AdminDepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    @GetMapping("/children/{hoscode}")
    @ApiOperation("医院科室列表")
    public Result<?> getDepartmentByHoscode(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.getDepartmentByHoscode(hoscode);
        return Result.ok(list);
    }
}
