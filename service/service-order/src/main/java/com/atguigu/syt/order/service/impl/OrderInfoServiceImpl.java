package com.atguigu.syt.order.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.hosp.client.HospSetFeignClient;
import com.atguigu.syt.hosp.client.ScheduleFeignClient;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.order.mapper.OrderInfoMapper;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.user.client.PatientFeignClient;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    private final PatientFeignClient patientFeignClient;
    @Resource
    private  ScheduleFeignClient scheduleFeignClient;
    private final HospSetFeignClient hospSetFeignClient;

    @Override
    public Long saveOrderInfo(Long uid, String scheduleId, Long patientId) {
        //根据就诊人id获取就诊人信息
        Patient patient = patientFeignClient.getPatient(patientId);
        if (patient == null) throw new GuiguException(ResultCodeEnum.PARAM_ERROR);


        //根据排班ID获取排班信息
        ScheduleOrderVo scheduleOrderVo = scheduleFeignClient.getScheduleOrderVo(scheduleId);
        if (scheduleOrderVo == null) throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        Integer availableNumberSyt = scheduleOrderVo.getAvailableNumber();
        if (availableNumberSyt <= 0) throw new GuiguException(ResultCodeEnum.NUMBER_NO);


        //获取第三方医院的url和signKey
        HospitalSet hospitalSet = hospSetFeignClient.getHospitalSet(scheduleOrderVo.getHoscode());
        if (hospitalSet == null) throw new GuiguException(ResultCodeEnum.PARAM_ERROR);

        //请求第三方医院
        Map<String, Object> map = new HashMap<>();

        map.put("hoscode", hospitalSet.getHoscode());
        map.put("depcode", scheduleOrderVo.getDepcode());
        map.put("hosScheduleId", scheduleOrderVo.getHosScheduleId());
        map.put("reserveDate", scheduleOrderVo.getReserveDate());
        map.put("reserveTime", scheduleOrderVo.getReserveTime());
        map.put("amount", scheduleOrderVo.getAmount());
        map.put("name", patient.getName());
        map.put("certificatesType", patient.getCertificatesType());
        map.put("certificatesNo", patient.getCertificatesNo());
        map.put("birthdate", patient.getBirthdate());
        map.put("phone", patient.getPhone());
        map.put("provinceCode", patient.getProvinceCode());
        map.put("cityCode", patient.getCityCode());
        map.put("districtCOde", patient.getDistrictCode());
        map.put("address", patient.getAddress());
        map.put("timestamp", HttpRequestHelper.getTimestamp());
        map.put("sign", HttpRequestHelper.getSign(map, hospitalSet.getSignKey()));

        JSONObject respone = HttpRequestHelper.sendRequest(map, hospitalSet.getApiUrl() + "/order/submitOrder");

        //不同意抛出异常
        if (null == respone || 200 != respone.getIntValue("code")) {
            throw new GuiguException(ResultCodeEnum.NUMBER_NO);
        }

        //同意 封装orderInfo
        OrderInfo orderInfo = new OrderInfo();

        JSONObject data = respone.getJSONObject("data");
        String hosOrderId = data.getString("hosOrderId");
        Integer number = data.getInteger("number");
        String fetchTime = data.getString("fetchTime");
        String fetchAddress = data.getString("fetchAddress");

        //更新平台上该医生的剩余可预约数
        Integer reservedNumber = data.getInteger("reservedNumber");
        Integer availableNumber = data.getInteger("availableNumber");

        BeanUtils.copyProperties(scheduleOrderVo, orderInfo);
        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setScheduleId(scheduleId);
        orderInfo.setPatientId(patient.getId());
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setHosOrderId(hosOrderId);
        orderInfo.setNumber(number);
        orderInfo.setFetchTime(fetchTime);
        orderInfo.setFetchAddress(fetchAddress);
        orderInfo.setAmount(scheduleOrderVo.getAmount());
        orderInfo.setQuitTime(scheduleOrderVo.getQuitTime());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());

        //保存到数据表
        baseMapper.insert(orderInfo);

        return  orderInfo.getId();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取订单详情
     */
    @Override
    public OrderInfo getOrderInfo(Long uid, Long orderId) {
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getId, orderId);
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getUserId, uid);
        OrderInfo orderInfo = baseMapper.selectOne(orderInfoLambdaQueryWrapper);
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}
