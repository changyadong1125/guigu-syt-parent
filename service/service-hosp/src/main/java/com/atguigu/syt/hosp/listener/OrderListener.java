package com.atguigu.syt.hosp.listener;

import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.vo.order.OrderMqVo;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.listener
 * class:OrderListener
 *
 * @author: smile
 * @create: 2023/6/19-10:10
 * @Version: v1.0
 * @Description:
 */
@Component
public class OrderListener {
    @Resource
    private ScheduleService scheduleService;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = MQConst.QUEUE_ORDER),
                          exchange = @Exchange(name = MQConst.EXCHANGE_DIRECT_ORDER,type = "direct"),
                          key = MQConst.ROUTING_KEY_ORDER)
    })
    public void consume(OrderMqVo orderMqVo){
        scheduleService.updateAvaNumber(orderMqVo);
    }
}
