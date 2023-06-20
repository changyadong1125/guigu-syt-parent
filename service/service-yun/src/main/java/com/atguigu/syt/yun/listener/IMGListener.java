package com.atguigu.syt.yun.listener;

import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.vo.ImgVo.IMGVo;
import com.atguigu.syt.yun.service.ImgService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.listener
 * class:IMGListener
 *
 * @author: smile
 * @create: 2023/6/19-22:40
 * @Version: v1.0
 * @Description:
 */
@Configuration
public class IMGListener  {
    @Resource
    private ImgService imgService;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = MQConst.QUEUE_USER_IMG),
                    exchange = @Exchange(name = MQConst.EXCHANGE_DIRECT_USER_IMG, type = "direct"),
                    key = MQConst.ROUTING_KEY_USER_IMG)
    })
    public void removeImg(IMGVo imgVo) {
            imgService.removeImg(imgVo);
    }
}
