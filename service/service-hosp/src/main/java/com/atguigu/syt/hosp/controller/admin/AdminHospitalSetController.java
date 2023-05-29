package com.atguigu.syt.hosp.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.entity.HospitalSet;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class AdminHospitalSetController {
    @Resource
    private HospitalSetService hospitalSetService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:增
     */
    @ApiOperation(value = "医院设置新增")
    @PostMapping(value = "/save")
    public Result<?> save(@ApiParam(value = "医院设置对象") @RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) return Result.ok("添加成功");
        else return Result.fail("添加失败");
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删
     */
    @ApiOperation(value = "医院设置删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result<?> delete(@ApiParam(value = "医院设置Id") @PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) return Result.ok("删除成功");
        else return Result.fail("删除失败");
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显
     */
    @ApiOperation(value = "医院设置回显")
    @GetMapping(value = "/edit/{id}")
    public Result<?> edit(@ApiParam(value = "医院设置Id") @PathVariable Long id) {
        return Result.ok(hospitalSetService.getById(id));
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改
     */
    @ApiOperation(value = "医院设置修改")
    @PutMapping(value = "/update")
    public Result<?> update(@ApiParam(value = "医院设置对象") @RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) return Result.ok("修改成功");
        else return Result.fail("修改失败");
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:分页
     */
    @ApiOperation(value = "分页")
    @PostMapping(value = "/page/{pageNum}/{pageSize}")
    public Result<?> page(@ApiParam(value = "当前页") @PathVariable Integer pageNum,
                          @ApiParam(value = "每页多少条") @PathVariable Integer pageSize,
                          @ApiParam(value = "医院设置查询对象") @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = hospitalSetService.selectPage(pageNum, pageSize, hospitalSetQueryVo);
        return Result.ok(page);
    }
}
