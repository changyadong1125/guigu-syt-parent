package com.atguigu.syt.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user
 * class:ServiceUserApplication
 *
 * @author: smile
 * @create: 2023/6/7-19:27
 * @Version: v1.0
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
@EnableFeignClients("com.atguigu.syt")
@EnableDiscoveryClient
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class,args);
    }
}
