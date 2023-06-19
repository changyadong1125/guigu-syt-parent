package com.atguigu.syt.order.schedule;

import com.atguigu.syt.order.service.OrderInfoService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.schedule
 * class:PatientRemind
 *
 * @author: smile
 * @create: 2023/6/19-14:40
 * @Version: v1.0
 * @Description:
 * 第一步 ： 类上加@EnableScheduling
 * 第二步 ： 在方法上添加@component注解
 */
@Component
@EnableScheduling
public class PatientRemind {
    @Resource
    private OrderInfoService orderInfoService;
    /*
    fixedDelay:表示当一个定时任务执行完成之后在延迟多长时间执行下一个任务
    fixedRate:表示每隔多长时间执行一次任务
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void remind(){
        orderInfoService.remind();
    }
}
