package com.atguigu.syt.hosp.controller.admin;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.admin
 * class:AdminScheduleController
 *
 * @author: smile
 * @create: 2023/6/6-19:19
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class AdminScheduleController {

    @Resource
    private ScheduleService scheduleService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取日期排班信息
     */

    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    @ApiOperation("查看排班聚合分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "limit", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "hoscode", value = "医院编码", required = true),
            @ApiImplicitParam(name = "depcode", value = "部门编码", required = true)
    })
    public Result<?> getScheduleRule(@PathVariable Integer page,
                                     @PathVariable Integer limit,
                                     @PathVariable String hoscode,
                                     @PathVariable String depcode) {

        Map<String,Object> map =  scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取排班的详细信息
     */
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    @ApiOperation(value = "查询排班详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode", value = "医院编码", required = true),
            @ApiImplicitParam(name = "depcode", value = "部门编码", required = true),
            @ApiImplicitParam(name = "workDate", value = "选择日期", required = true)
    })
    public Result<?> getScheduleDetail(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate
    ) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
