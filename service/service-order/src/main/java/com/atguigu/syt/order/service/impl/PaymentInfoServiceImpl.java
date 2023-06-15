package com.atguigu.syt.order.service.impl;


import com.atguigu.syt.enums.PaymentStatusEnum;
import com.atguigu.syt.enums.PaymentTypeEnum;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.PaymentInfo;
import com.atguigu.syt.order.mapper.PaymentInfoMapper;
import com.atguigu.syt.order.service.PaymentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 支付信息表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
@Service
@RequiredArgsConstructor
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:保存订单信息
     */
    @Override
    public void savaPaymentInfo(Transaction transaction, OrderInfo orderInfo) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(PaymentTypeEnum.WEIXIN.getStatus());
        paymentInfo.setTradeNo(transaction.getOutTradeNo());
        paymentInfo.setTotalAmount(new BigDecimal(transaction.getAmount().getTotal()));
        paymentInfo.setSubject(orderInfo.getTitle());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(transaction.toString());
        baseMapper.insert(paymentInfo);
    }
}
