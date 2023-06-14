package com.atguigu.syt.hosp.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.front
 * class:FrontScheduleController
 *
 * @author: smile
 * @create: 2023/6/13-16:05
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/hosp/schedule")
@Api(tags = "排班")
@RequiredArgsConstructor
public class FrontScheduleController {
    private final ScheduleService scheduleService;
    private final AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取指定时间范围内的医院排班聚合信息
     */
    @GetMapping("/page/{pageNum}/{pageSize}/{hoscode}/{depcode}")
    @ApiOperation("获取排班")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
            @ApiImplicitParam(name = "hoscode", value = "医院编号", required = true),
            @ApiImplicitParam(name = "depcode", value = "科室编号", required = true),
    })
    public Result<?> getBookingScheduleRule(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String hoscode, @PathVariable String depcode) {
        Map<String, Object> map = scheduleService.getBookingScheduleRule(pageNum, pageSize, hoscode, depcode);
        return Result.ok(map);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取排班的详细信息
     */
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    @ApiOperation(value = "查询排班详细列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode", value = "医院编码", required = true),
            @ApiImplicitParam(name = "depcode", value = "部门编码", required = true),
            @ApiImplicitParam(name = "workDate", value = "选择日期", required = true)
    })
    public Result<?> getScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate
    ) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(list);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取排班详细信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询排班详细信息")
    public Result<?> getScheduleDetail(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        authContextHolder.checkAuth(request,response);
        Schedule schedule = scheduleService.getById(id);
        return Result.ok(schedule);
    }
}
