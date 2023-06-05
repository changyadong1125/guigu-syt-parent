package com.atguigu.syt.hosp.controller.api;

import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.model.hosp.Hospital;
import com.atguigu.syt.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.api
 * class:ApiController
 *
 * @author: smile
 * @create: 2023/6/3-21:07
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/api/hosp")
@Api(value = "医院接口")
public class ApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 保存医院信息到MongoDB
     */
    @PostMapping(value = "/saveHospital")
    @ApiOperation("医院新增")
    public Result<?> saveHospital(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = getParameterMapAndCheckSign(request);
        //添加到Mongo
        hospitalService.save(parameterMap);
        return Result.ok();

    }


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 医院信息查询
     */
    @PostMapping(value = "/hospital/show")
    @ApiOperation("医院查询")
    public Result<?> hospitalShow(HttpServletRequest request) {
        //获取医院方传过来的医院信息
        HashMap<String, Object> parameter = getParameterMapAndCheckSign(request);
        //查询
        Hospital hospital = hospitalService.getHospital((String) parameter.get("hoscode"));
        return Result.ok(hospital);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 保存科室信息
     */
    @PostMapping(value = "/saveDepartment")
    @ApiOperation("保存科室信息")
    public Result<?> saveDepartment(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        departmentService.save(parameterMap);
        return Result.ok();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 科室分页查询
     */
    @PostMapping(value = "/department/list")
    @ApiOperation("科室分页查询")
    public Result<?> departmentList(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        Page<Department> page = departmentService.getDepartmentPageList(Integer.parseInt((String) parameterMap.get("page")), Integer.parseInt((String) parameterMap.get("limit")), (String) parameterMap.get("hoscode"));
        return Result.ok(page);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 上传排班
     */
    @PostMapping(value = "/saveSchedule")
    @ApiOperation("上传排班信息")
    public Result<?> saveSchedule(HttpServletRequest request) {
        //获取医院方传过来的科室信息
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        scheduleService.save(parameterMap);
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 医院排班分页
     */
    @PostMapping(value = "/schedule/list")
    @ApiOperation("上传排班信息")
    public Result<?> scheduleList(HttpServletRequest request) {
        //获取医院方传过来的科室信息
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        Page<Schedule> page = scheduleService.getScheduleList(Integer.parseInt((String) parameterMap.get("page")), Integer.parseInt((String) parameterMap.get("limit")), (String) parameterMap.get("hoscode"));
        return Result.ok(page);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 科室删除
     */
    @PostMapping(value = "/department/remove")
    @ApiOperation("科室删除")
    public Result<?> departmentRemove(HttpServletRequest request) {
        //获取医院方传过来的科室信息
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        departmentService.removeByHoscodeAndDepcode((String)parameterMap.get("hoscode"),(String)parameterMap.get("depcode"));
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 排班信息删除
     */
    @PostMapping(value = "/schedule/remove")
    @ApiOperation("科室删除")
    public Result<?> scheduleRemove(HttpServletRequest request) {
        //获取医院方传过来的科室信息
        HashMap<String, Object> parameterMap = this.getParameterMapAndCheckSign(request);
        scheduleService.removeByHoscodeAndHosScheduleId((String)parameterMap.get("hoscode"),(String)parameterMap.get("hosScheduleId"));
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    private HashMap<String, Object> getParameterMapAndCheckSign(HttpServletRequest request) {
        //获取医院方传过来的医院信息
        Map<String, String[]> parameterMaps = request.getParameterMap();
        //获取医院信息
        HashMap<String, Object> parameterMap = new HashMap<>();
        Set<Map.Entry<String, String[]>> entries = parameterMaps.entrySet();
        entries.forEach(maps -> parameterMap.put(maps.getKey(), maps.getValue()[0]));
        //验签
        String hoscode = (String) parameterMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(parameterMap, signKey);
        return parameterMap;
    }
}

