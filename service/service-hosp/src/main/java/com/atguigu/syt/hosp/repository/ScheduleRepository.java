package com.atguigu.syt.hosp.repository;

import com.atguigu.syt.model.hosp.Schedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.repository
 * class:ScheduleRepository
 *
 * @author: smile
 * @create: 2023/6/4-10:39
 * @Version: v1.0
 * @Description:
 */
public interface ScheduleRepository extends MongoRepository<Schedule, ObjectId> {

    Schedule findByHoscodeAndDepcodeAndHosScheduleId(String hoscode, String depcode, String hosScheduleId);
    Schedule findByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
