package com.atguigu.syt.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp
 * class:HospApplicationStarter
 *
 * @author: smile
 * @create: 2023/5/29-18:32
 * @Version: v1.0
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
public class HospApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(HospApplicationStarter.class,args);
    }
}
