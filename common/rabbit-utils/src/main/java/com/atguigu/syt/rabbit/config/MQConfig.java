package com.atguigu.syt.rabbit.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.rabbit.config
 * class:q
 *
 * @author: smile
 * @create: 2023/6/19-9:17
 * @Version: v1.0
 * @Description:
 */
@Configuration
public class MQConfig {

    @Bean
    public MessageConverter messageConverter(){
        //配置json字符串转换器，默认使用SimpleMessageConverter
        return new Jackson2JsonMessageConverter();
    }
}
