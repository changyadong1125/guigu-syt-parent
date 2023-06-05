package com.atguigu.syt.hosp.repository;

import com.atguigu.syt.model.hosp.Hospital;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.repository
 * class:HospitalRepository
 *
 * @author: smile
 * @create: 2023/6/3-21:03
 * @Version: v1.0
 * @Description:
 */
public interface HospitalRepository extends MongoRepository<Hospital, ObjectId> {
    Hospital getByHoscode(String hoscode);
}
