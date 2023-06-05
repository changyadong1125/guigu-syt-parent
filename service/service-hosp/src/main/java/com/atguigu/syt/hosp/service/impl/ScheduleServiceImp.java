package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.repository.ScheduleRepository;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Schedule;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service.impl
 * class:ScheduleServiceImp
 *
 * @author: smile
 * @create: 2023/6/4-10:40
 * @Version: v1.0
 * @Description:
 */
@Service
public class ScheduleServiceImp implements ScheduleService {
    @Resource
    private ScheduleRepository scheduleRepository;


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:医院排班信息上传
     */
    @Override
    public void save(HashMap<String, Object> parameterMap) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(parameterMap), Schedule.class);
        Schedule exitSchedule = scheduleRepository.findByHoscodeAndDepcodeAndHosScheduleId(schedule.getHoscode(), schedule.getDepcode(), schedule.getHosScheduleId());
        if (exitSchedule == null) {
            scheduleRepository.save(schedule);
        } else {
            schedule.setId(exitSchedule.getId());
            schedule.setStatus(exitSchedule.getStatus());
            scheduleRepository.save(schedule);
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:科室排班分页
     */
    @Override
    public Page<Schedule> getScheduleList(int page, int limit, String hoscode) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        return scheduleRepository.findAll(example, pageRequest);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除排班信息
     */
    @Override
    public void removeByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        scheduleRepository.delete(schedule);
    }
}
