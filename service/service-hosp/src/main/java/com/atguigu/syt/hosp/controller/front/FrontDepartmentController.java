package com.atguigu.syt.hosp.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.front
 * class:FrontDepartmentController
 *
 * @author: smile
 * @create: 2023/6/7-18:35
 * @Version: v1.0
 * @Description:
 */
@Api("医院科室接口")
@RestController
@RequestMapping("/front/hosp/department")
public class FrontDepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据医院编号获取科室信息
     */
    @GetMapping("/children/{hoscode}")
    @ApiImplicitParam(name = "hoscode", value = "医院编号")
    @ApiOperation("查询医院科室")
    public Result<?> getDepartmentListByHoscode(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDepartmentByHoscode(hoscode);
        return Result.ok(list);
    }
}
