package com.atguigu.syt.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn
 * class:ServerCmnApplication
 *
 * @author: smile
 * @create: 2023/5/31-18:34
 * @Version: v1.0
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class,args);
    }
}
