package com.atguigu.syt.hosp.client;

import com.atguigu.syt.hosp.client.imp.ScheduleFeignDegrade;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.client
 * class:ScheduleFeignClient
 *
 * @author: smile
 * @create: 2023/6/14-10:33
 * @Version: v1.0
 * @Description:
 */
@FeignClient(value = "service-hosp",contextId = "scheduleFeignClient",path = "/inner/hosp/schedule",fallbackFactory = ScheduleFeignDegrade.class)
public interface ScheduleFeignClient {
    @GetMapping("/info/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId);

}
