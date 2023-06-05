package com.atguigu.syt.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableFeignClients("com.atguigu.syt")
public class ServiceHospApplication{
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}
/*
整合mongoDB
导入依赖 spring-boot-starter-data-mongodb
配置yml文件 数据库连接
创建pojo类 @document @id @field
创建接口UserRepository实现MongoRepository<User,ObjectId>
引入UserRepository实现类对象
 */
