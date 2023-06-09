package com.atguigu.syt.order.service.impl;


import com.atguigu.syt.enums.PaymentTypeEnum;
import com.atguigu.syt.enums.RefundStatusEnum;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.RefundInfo;
import com.atguigu.syt.order.mapper.RefundInfoMapper;
import com.atguigu.syt.order.service.RefundInfoService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.service.refund.model.Refund;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 退款信息表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:保存退款信息
     */
    @Override
    public void saveRefundInfo(OrderInfo orderInfo, Refund response) {
        // 保存退款记录
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        refundInfo.setOrderId(orderInfo.getId());
        refundInfo.setPaymentType(PaymentTypeEnum.WEIXIN.getStatus());
        refundInfo.setTradeNo(response.getOutRefundNo());
        refundInfo.setTotalAmount(new BigDecimal(response.getAmount().getRefund()));
        refundInfo.setSubject(orderInfo.getTitle());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());//退款中
        baseMapper.insert(refundInfo);
    }

    @Override
    public void updateRefundInfoStatus(String outTradeNo, RefundStatusEnum refund) {
        LambdaUpdateWrapper<RefundInfo> refundInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        refundInfoLambdaUpdateWrapper.eq(RefundInfo::getOutTradeNo,outTradeNo);
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setRefundStatus(refund.getStatus());
        refundInfo.setCallbackTime(new Date());
        baseMapper.update(refundInfo,refundInfoLambdaUpdateWrapper);
    }
}
