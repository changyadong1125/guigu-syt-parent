package com.atguigu.syt.hosp.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:UserRepository
 *
 * @author: smile
 * @create: 2023/6/2-15:44
 * @Version: v1.0
 * @Description:
 */
public interface UserRepository extends MongoRepository<User1, ObjectId> {

    List<User1> findByName(String name);
    List<User1> findByNameLike(String name);
    List<User1> findByNameAndAge(String name, Integer age);
}
