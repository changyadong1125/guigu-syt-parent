package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service
 * class:ScheduleService
 *
 * @author: smile
 * @create: 2023/6/4-10:40
 * @Version: v1.0
 * @Description:
 */
public interface ScheduleService {
    void save(HashMap<String, Object> parameterMap);

    Page<Schedule> getScheduleList(int page, int limit, String hoscode);
    void removeByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode);

    Schedule getById(String id);
}
