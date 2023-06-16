package com.atguigu.syt.order.service;


import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.pay.java.service.refund.model.Refund;

/**
 * <p>
 * 退款信息表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
public interface RefundInfoService extends IService<RefundInfo> {

    void saveRefundInfo(OrderInfo orderInfo, Refund response);
}
