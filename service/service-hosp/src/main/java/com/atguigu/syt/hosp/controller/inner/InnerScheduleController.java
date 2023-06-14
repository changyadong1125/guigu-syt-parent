package com.atguigu.syt.hosp.controller.inner;

import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.inner
 * class:InnerScheduleController
 *
 * @author: smile
 * @create: 2023/6/14-19:51
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/inner/hosp/schedule")
@Api("排班接口-供其他微服务远程调用")
public class InnerScheduleController {
    @Resource
    private ScheduleService scheduleService;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据排班id获取预约下单数据
     */
    @GetMapping("/info/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId){
        return scheduleService.getScheduleOrderVo(scheduleId);
    }
}
