package com.atguigu.syt.yun.listener;

import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.vo.sms.SmsVo;
import com.atguigu.syt.yun.service.SMSService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.listener
 * class:SmsListener
 *
 * @author: smile
 * @create: 2023/6/19-10:24
 * @Version: v1.0
 * @Description:
 */
@Component
public class SmsListener {
    @Resource
    private SMSService smsService;
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = MQConst.QUEUE_SMS),
                    exchange = @Exchange(name = MQConst.EXCHANGE_DIRECT_SMS, type = "direct"),
                    key = MQConst.ROUTING_KEY_SMS)
    })
    public void consume(SmsVo smsVo) {
        smsService.sendMessage(smsVo);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = MQConst.QUEUE_SMS_REMIND),
                    exchange = @Exchange(name = MQConst.EXCHANGE_DIRECT_SMS_REMIND, type = "direct"),
                    key = MQConst.ROUTING_KEY_SMS_REMIND)
    })
    public void consumeCancel(SmsVo smsVo) {
        smsService.sendMessageRemind(smsVo);
    }
}
