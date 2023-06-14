package com.atguigu.syt.user.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.controller.front
 * class:FrontPatientController
 *
 * @author: smile
 * @create: 2023/6/12-19:28
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/user/patient")
@Api("就诊人管理")
@RequiredArgsConstructor
public class FrontPatientController {
    private final PatientService patientService;
    private final AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:保存就诊人信息
     */
    @PostMapping("/save")
    @ApiOperation("添加就诊人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patient", value = "就诊人信息", required = true)
    })
    public Result<?> save(@RequestBody Patient patient, HttpServletResponse response, HttpServletRequest request) {
        Long uid = authContextHolder.checkAuth(request, response);
        patient.setUserId(uid);
        boolean flag = patientService.save(patient);
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除就诊人信息
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除就诊人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "就诊人Id", required = true)
    })
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        boolean flag = patientService.delete(uid, id);
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显就诊人信息
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("回显就诊人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "就诊人Id", required = true)
    })
    public Result<?> detail(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        Patient patient = patientService.selectByIdAndUid(id, uid);
        return Result.ok(patient);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改就诊人信息
     */
    @PutMapping("/update")
    @ApiOperation("修改就诊人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patient", value = "就诊人信息", required = true)
    })
    public Result<?> update(@RequestBody Patient patient, HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        patient.setUserId(uid);
        boolean flag = patientService.updateById(patient);
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询就诊人列表
     */
    @GetMapping("/all")
    @ApiOperation("查询就诊人列表")
    public Result<?> getAll(HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        List<Patient> list = patientService.getAll(uid);
        return Result.ok(list);
    }
}
