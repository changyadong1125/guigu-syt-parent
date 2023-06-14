package com.atguigu.syt.hosp.client.imp;

import com.atguigu.syt.hosp.client.ScheduleFeignClient;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.client.imp
 * class:ScheduleFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/14-10:46
 * @Version: v1.0
 * @Description:
 */
@Component
public class ScheduleFeignDegrade implements FallbackFactory<ScheduleFeignClient> {

    @Override
    public ScheduleFeignClient create(Throwable throwable) {
        return new ScheduleFeignClient() {
            @Override
            public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
                return null;
            }
        };
    }
}
