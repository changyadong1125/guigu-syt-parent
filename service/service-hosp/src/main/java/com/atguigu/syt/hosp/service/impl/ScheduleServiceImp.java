package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.repository.DepartmentRepository;
import com.atguigu.syt.hosp.repository.HospitalRepository;
import com.atguigu.syt.hosp.repository.ScheduleRepository;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.hosp.utils.DateUtil;
import com.atguigu.syt.model.hosp.BookingRule;
import com.atguigu.syt.model.hosp.Hospital;
import com.atguigu.syt.model.hosp.Schedule;
import com.atguigu.syt.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.syt.vo.hosp.ScheduleRuleVo;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private HospitalRepository hospitalRepository;
    @Resource
    private DepartmentRepository departmentRepository;


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
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
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
        List<Schedule> list = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(
                hoscode,
                depcode,
                new DateTime(workDate).toDate());
        list.forEach(S -> S.getParam().put("id", S.getId().toString()));
        return list;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取排班信息
     */
    @Override
    public Map<String, Object> getBookingScheduleRule(Integer pageNum, Integer pageSize, String hoscode, String depcode) {
        Hospital hospital = hospitalRepository.getByHoscode(hoscode);
        BookingRule bookingRule = hospital.getBookingRule();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> datePage = this.getDateList(pageNum, pageSize, bookingRule);
        List<Date> currentPageDateList = datePage.getRecords();
        long pages = datePage.getPages();
        //mongoTemplate 支持聚合  mongoRepository查询方便
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(currentPageDateList)),
                Aggregation.group("workDate").first("workDate").as("workDate").sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC, "workDate")
        );

        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregationResults.getMappedResults();
        Map<Date, BookingScheduleRuleVo> collect = null;
        if (!CollectionUtils.isEmpty(mappedResults)) {
            collect = mappedResults.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, V -> V));
        } else {
            collect = new HashMap<>();
        }

        ArrayList<BookingScheduleRuleVo> BookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0; i < currentPageDateList.size(); i++) {
            Date date = currentPageDateList.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = collect.get(date);
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setWorkDate(date);
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDateMd(date);
            bookingScheduleRuleVo.setDayOfWeek(DateUtil.getDayOfWeek(new DateTime(date)));
            bookingScheduleRuleVo.setStatus(0);
            //判断是否为最后一页 如果是改为即将放号
            if (pageNum == pages && i == (currentPageDateList.size()) - 1) {
                bookingScheduleRuleVo.setStatus(1);
            }
            //判断是否是第一页 如果是改为即将放号
            if (pageNum == 1 && i == 0) {
                DateTime dateTime = this.parseDateToDateTime(new Date(), bookingRule.getStopTime());
                if (dateTime.isBeforeNow()) {
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            BookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", BookingScheduleRuleVoList);
        resultMap.put("total", datePage.getTotal());
        HashMap<String, Object> baseMap = new HashMap<>();
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        baseMap.put("depname", departmentRepository.findByHoscodeAndDepcode(hoscode, depcode).getDepname());
        baseMap.put("bigname", departmentRepository.findByHoscodeAndDepcode(hoscode, depcode).getBigname());
        baseMap.put("hosname", hospitalRepository.getByHoscode(hoscode).getHosname());
        resultMap.put("baseMap", baseMap);
        return resultMap;
    }

    @Override
    public Schedule getById(String id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(new ObjectId(id));
        if (optionalSchedule.isPresent()){
            Schedule schedule = optionalSchedule.get();
            this.packageSchedule(schedule);
            return schedule;
        }
        return null;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:封装排班信息
     */
    private void packageSchedule(Schedule schedule) {
        String hoscode = schedule.getHoscode();
        String depcode = schedule.getDepcode();
        Date workDate = schedule.getWorkDate();
        Integer workTime = schedule.getWorkTime();

        schedule.getParam().put("hosname", hospitalRepository.getByHoscode(hoscode).getHosname());
        schedule.getParam().put("depname", departmentRepository.findByHoscodeAndDepcode(hoscode, depcode).getDepname());
        schedule.getParam().put("workTime", workTime == 0 ? "上午" : "下午");
        schedule.getParam().put("dayOfWeek", DateUtil.getDayOfWeek(new DateTime(workDate)));
        schedule.getParam().put("id", schedule.getId().toString());

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取连续时间列表
     */
    private com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> getDateList(Integer pageNum, Integer pageSize, BookingRule bookingRule) {
        //获取当前医院的挂号周期
        Integer cycle = bookingRule.getCycle();
        //获取挂号的起始时间
        String releaseTime = bookingRule.getReleaseTime();
        //获取连续时间 的起始时间
        DateTime startDateTime = this.parseDateToDateTime(new Date(), releaseTime);
        //判断是否开始挂号
        if (startDateTime.isBeforeNow()) cycle++;

        List<Date> dateList = new ArrayList<>();

        for (int i = 0; i < cycle; i++) {
            DateTime dateTime = new DateTime().plusDays(i);
            String dateTimeString = dateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateTimeString).toDate());
        }
        //获取当前页的连续时间
        List<Date> currentPageDateList = new ArrayList<>();
        int start = (pageNum - 1) * pageSize;
        int end = start + pageSize;
        if(end> dateList.size()) end = dateList.size();

        for (int i = start; i < end; i++) {
            currentPageDateList.add(dateList.get(i));
        }

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> page
                = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize, dateList.size());
        page.setRecords(currentPageDateList);

        return page;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:解析挂号的具体起始时间
     */
    private DateTime parseDateToDateTime(Date date, String releaseTime) {
        String startTime = new DateTime(date).toString("yyyy-MM-dd") + " " + releaseTime;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(startTime);
    }
}
