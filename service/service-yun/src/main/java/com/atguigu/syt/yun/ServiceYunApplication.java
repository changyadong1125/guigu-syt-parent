package com.atguigu.syt.yun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun
 * class:ServiceYunApplication
 *
 * @author: smile
 * @create: 2023/6/9-20:17
 * @Version: v1.0
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源配置自动读取
@ComponentScan("com.atguigu")
public class ServiceYunApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceYunApplication.class,args);
    }
}
