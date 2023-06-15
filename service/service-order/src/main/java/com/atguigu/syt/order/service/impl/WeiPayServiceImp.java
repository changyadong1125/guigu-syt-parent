package com.atguigu.syt.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.hosp.client.HospSetFeignClient;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.order.controller.config.WxPayConfig;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.order.service.PaymentInfoService;
import com.atguigu.syt.order.service.WeiPayService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.service.impl
 * class:WeiPayServiceImp
 *
 * @author: smile
 * @create: 2023/6/14-20:56
 * @Version: v1.0
 * @Description:
 */
@Service
public class WeiPayServiceImp implements WeiPayService {
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private HospSetFeignClient hospSetFeignClient;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取微信支付二维码url
     */
    @Override
    public String createNative(String outTradeNo) {
        // 构建service
        NativePayService service = new NativePayService.Builder().config(wxPayConfig.getConfig()).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();

        Amount amount = new Amount();
        amount.setTotal(1);
        amount.setCurrency("CNY");
        request.setAmount(amount);

        request.setAppid(wxPayConfig.getAppid());
        request.setMchid(wxPayConfig.getMchId());
        request.setDescription("挂号费");
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
        return response.getCodeUrl();
    }

    @Override
    public Boolean queryPayStatus(String outTradeNo) {
        // 构建service
        NativePayService service = new NativePayService.Builder().config(wxPayConfig.getConfig()).build();


        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        queryRequest.setOutTradeNo(outTradeNo);
        queryRequest.setMchid(wxPayConfig.getMchId());

        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        request.setOutTradeNo(outTradeNo);
        request.setMchid(wxPayConfig.getMchId());

        Transaction result = service.queryOrderByOutTradeNo(queryRequest);
        Transaction.TradeStateEnum tradeState = result.getTradeState();
        // 调用接口
        Transaction transaction = service.queryOrderByOutTradeNo(request);
        //支付成功
        if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
            this.wxPaySuccess(transaction,outTradeNo);
            return true;
        }

        return false;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:支付成功 保存订单信息
     */
    private void wxPaySuccess(Transaction transaction,String outTradeNo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOutTradeNo,outTradeNo);
        OrderInfo orderInfo = orderInfoService.getOne(queryWrapper);
        //更新订单状态
        orderInfoService.updateStatus(outTradeNo, OrderStatusEnum.PAID.getStatus());

        //保存支付信息
        paymentInfoService.savaPaymentInfo(transaction,orderInfo);

        //通知医院修改订单状态
        HospitalSet hospitalSet = hospSetFeignClient.getHospitalSet(orderInfo.getHoscode());

        Map<String,Object> map = new HashMap<>();
        map.put("hoscode",hospitalSet.getHoscode());
        map.put("hosOrderId",orderInfo.getHosOrderId());
        map.put("timestamp",System.currentTimeMillis());
        map.put("sign",HttpRequestHelper.getSign(map,hospitalSet.getSignKey()));
        JSONObject response = HttpRequestHelper.sendRequest(map, hospitalSet.getApiUrl() + "/order/updatePayStatus");
        //解析响应
        if(response.getInteger("code") != 200) {
            throw new GuiguException(ResultCodeEnum.FAIL.getCode(), response.getString("message"));
        }
    }
}
