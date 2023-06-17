package com.atguigu.syt.hosp.mongo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:MongoDBTest
 *
 * @author: smile
 * @create: 2023/6/2-18:32
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class MongoDBTest {
    @Resource
    private UserMongoDBRepository userMongoDBRepository;
    @Test
    public void test(){
        userMongoDBRepository.save(new User(null,"李四","lisi@Gmail.com",23,true));
    }

}
