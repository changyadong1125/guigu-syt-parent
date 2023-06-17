package com.atguigu.syt.hosp.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:UserMongoDBRepository
 *
 * @author: smile
 * @create: 2023/6/2-18:35
 * @Version: v1.0
 * @Description:
 */
public interface UserMongoDBRepository extends MongoRepository<User, ObjectId> {
}
