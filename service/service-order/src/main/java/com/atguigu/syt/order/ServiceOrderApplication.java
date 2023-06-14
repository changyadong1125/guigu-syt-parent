package com.atguigu.syt.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order
 * class:ServiceOrderApplication
 *
 * @author: smile
 * @create: 2023/6/13-15:32
 * @Version: v1.0
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
@EnableFeignClients("com.atguigu")
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
