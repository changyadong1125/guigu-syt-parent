package com.atguigu.syt.order.service;


import com.atguigu.syt.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
public interface OrderInfoService extends IService<OrderInfo> {

    Long saveOrderInfo(Long uid, String scheduleId, Long patientId);

    OrderInfo getOrderInfo(Long uid, Long orderId);

    List<OrderInfo> selectList(Long userId);

    void updateStatus(String outTradeNo, Integer status);

    void cancelOrder(String outTradeNo,Long uid);
     OrderInfo selectOrderInfoByOutradeNo(String outTradeNo,Long uid) ;

    void remind();
}
