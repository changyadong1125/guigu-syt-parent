package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.repository.ScheduleRepository;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.hosp.utils.DateUtil;
import com.atguigu.syt.model.hosp.Schedule;
import com.atguigu.syt.vo.hosp.ScheduleRuleVo;
import org.joda.time.DateTime;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private MongoTemplate mongoTemplate;


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

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据 医编号 和 科室编号 查询 医院排班信息
     */
    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        //设置查询条件 根据医院编号 和 科室编号查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        /*
        Aggregation:封装的是聚合信息
        InputType：指定输入的类型，说白了就是根据指定的输入类型找到对应的集合
        OutPutType：指定输出类型，把聚合之后的信息封装到什么对象中
        */
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );

        AggregationResults<ScheduleRuleVo> scheduleRuleVos = mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRuleVo.class);


        List<ScheduleRuleVo> scheduleRuleVoList = scheduleRuleVos.getMappedResults();
        for (ScheduleRuleVo scheduleRuleVo : scheduleRuleVoList) {
            scheduleRuleVo.setDayOfWeek(DateUtil.getDayOfWeek(new DateTime(scheduleRuleVo.getWorkDate())));
        }

        Aggregation aggregationWithCount = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );

        AggregationResults<ScheduleRuleVo> scheduleRuleVoWithCount = mongoTemplate.aggregate(aggregationWithCount, Schedule.class, ScheduleRuleVo.class);
        List<ScheduleRuleVo> mappedResults = scheduleRuleVoWithCount.getMappedResults();


        Map<String, Object> map = new HashMap<>();
        map.put("total", mappedResults.size());
        map.put("list", scheduleRuleVoList);
        return map;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取排版的详细信息
     */
    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        return scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(
                hoscode,
                depcode,
                new DateTime(workDate).toDate());
    }
}
