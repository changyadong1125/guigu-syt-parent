package com.atguigu.syt.hosp.repository;

import com.atguigu.syt.model.hosp.Department;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.repository
 * class:DepartmentRepository
 *
 * @author: smile
 * @create: 2023/6/3-23:35
 * @Version: v1.0
 * @Description:
 */
public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {
    Department findByHoscodeAndDepcode(String hoscode, String depcode);
}
