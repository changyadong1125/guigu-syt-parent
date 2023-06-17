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
import com.atguigu.syt.order.service.WeiPayService;
import com.atguigu.syt.user.client.PatientFeignClient;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private final ScheduleFeignClient scheduleFeignClient;
    private final HospSetFeignClient hospSetFeignClient;
    @Resource
    private WeiPayService weiPayService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:预约挂号
     */
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

        return orderInfo.getId();
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
        packageOrderInfo(orderInfo);
        return orderInfo;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取订单列表
     */
    @Override
    public List<OrderInfo> selectList(Long userId) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getUserId, userId);
        List<OrderInfo> infoList = baseMapper.selectList(queryWrapper);
        infoList.forEach(this::packageOrderInfo);
        return infoList;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:更新订单状态
     */
    @Override
    public void updateStatus(String outTradeNo, Integer status) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getOutTradeNo, outTradeNo);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(status);
        baseMapper.update(orderInfo, orderInfoLambdaUpdateWrapper);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:取消预约订单
     */
    @Override
    public void cancelOrder(String outTradeNo,Long uid) {

        OrderInfo orderInfo = selectOrderInfoByOutradeNo(outTradeNo, uid);

        if (orderInfo == null) throw new GuiguException(ResultCodeEnum.PARAM_ERROR);

        Integer orderStatus = orderInfo.getOrderStatus();
        if (OrderStatusEnum.PAID.getStatus().equals(orderStatus))
            this.cancelOrderWM(orderInfo, OrderStatusEnum.CANCLE_UNREFUND.getStatus());
        else cancelOrderNM(orderInfo, OrderStatusEnum.CANCLE.getStatus());

        //更新平台剩余可预约数 给用户发短信提示预约成功

        //todo：
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:未支付取消订单
     */
    private void cancelOrderNM(OrderInfo orderInfo, Integer orderStatus) {

        //判断当前时间是否超过可退好时间
        Date quitTime = orderInfo.getQuitTime();
        if (new DateTime(quitTime).isBeforeNow()) {
            throw new GuiguException(ResultCodeEnum.CANCEL_ORDER_NO);
        }
        orderInfo.setOrderStatus(orderStatus);
        updateByHospital(orderInfo);
        baseMapper.updateById(orderInfo);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:已支付取消订单
     */
    private void cancelOrderWM(OrderInfo orderInfo, Integer orderStatus) {
        //微信退款
        weiPayService.refund(orderInfo.getOutTradeNo(),orderInfo.getUserId());
        //取消预约
        this.cancelOrderNM(orderInfo, orderStatus);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通知第三方医院修改订单状态
     */
    private void updateByHospital(OrderInfo orderInfo) {
        HospitalSet hospitalSet = hospSetFeignClient.getHospitalSet(orderInfo.getHoscode());
        Map<String, Object> map = new HashMap<>();
        map.put("hoscode", hospitalSet.getHoscode());
        map.put("hosOrderId", orderInfo.getHosOrderId());
        map.put("timestamp", System.currentTimeMillis());
        map.put("hosScheduleId", orderInfo.getHosScheduleId());
        map.put("sign", HttpRequestHelper.getSign(map, hospitalSet.getSignKey()));
        JSONObject response = HttpRequestHelper.sendRequest(map, hospitalSet.getApiUrl() + "/order/updateCancelStatus");
        if (response.getInteger("code") != 200) {
            log.error("查单失败，"+ "code：" + response.getInteger("code") + "，message：" + response.getString("message"));
            throw new GuiguException(ResultCodeEnum.CANCEL_ORDER_FAIL);
        }
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:封装订单数据
     */
    private void packageOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据订单号查询到订单信息
     */
    public OrderInfo selectOrderInfoByOutradeNo(String outTradeNo,Long uid) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOutTradeNo, outTradeNo);
        queryWrapper.eq(OrderInfo::getUserId, uid);
        return this.getOne(queryWrapper);
    }
}
