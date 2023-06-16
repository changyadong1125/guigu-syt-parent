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
import com.atguigu.syt.order.service.RefundInfoService;
import com.atguigu.syt.order.service.WeiPayService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.Status;
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
    @Resource
    private RefundInfoService refundInfoService;


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
            OrderInfo orderInfo = orderInfoService.selectOrderInfoByOutradeNo(outTradeNo);
            //处理支付成功后重复查单
            //保证接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的
            Integer orderStatus = orderInfo.getOrderStatus();
            if (OrderStatusEnum.UNPAID.getStatus().intValue() != orderStatus.intValue()) {
                return true;//支付成功、关单、。。。
            }
            this.wxPaySuccess(transaction, outTradeNo);
            return true;
        }
        return false;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:微信退款
     */
    @Override
    public void refund(String outTradeNo) {
        RSAAutoCertificateConfig rsaAutoCertificateConfig = wxPayConfig.getConfig();
        // 初始化服务
        RefundService service = new RefundService.Builder().config(rsaAutoCertificateConfig).build();

        // 调用接口
        try {

            //获取订单
            OrderInfo orderInfo = orderInfoService.selectOrderInfoByOutradeNo(outTradeNo);

            CreateRequest request = new CreateRequest();
            // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
            request.setOutTradeNo(outTradeNo);
            request.setOutRefundNo("TK_" + outTradeNo);
            AmountReq amount = new AmountReq();
            //amount.setTotal(orderInfo.getAmount().multiply(new BigDecimal(100)).intValue());
            amount.setTotal(1L);//1分钱
            amount.setRefund(1L);
            amount.setCurrency("CNY");
            request.setAmount(amount);
            // 调用接口
            Refund response = service.create(request);

            Status status = response.getStatus();

            //            SUCCESS：退款成功（退款申请成功）
            //            CLOSED：退款关闭
            //            PROCESSING：退款处理中
            //            ABNORMAL：退款异常
            if(Status.CLOSED.equals(status)){

                throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "退款已关闭，无法退款");

            }else if(Status.ABNORMAL.equals(status)){

                throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "退款异常");

            } else{
                //SUCCESS：退款成功（退款申请成功） || PROCESSING：退款处理中
                //记录支退款日志
                refundInfoService.saveRefundInfo(orderInfo, response);
            }

        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            throw new GuiguException(ResultCodeEnum.FAIL);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            throw new GuiguException(ResultCodeEnum.FAIL);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            throw new GuiguException(ResultCodeEnum.FAIL);
        }












    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:支付成功 保存订单信息
     */
    private void wxPaySuccess(Transaction transaction, String outTradeNo) {
        OrderInfo orderInfo = orderInfoService.selectOrderInfoByOutradeNo(outTradeNo);
        //更新订单状态
        orderInfoService.updateStatus(outTradeNo, OrderStatusEnum.PAID.getStatus());

        //保存支付信息
        paymentInfoService.savaPaymentInfo(transaction, orderInfo);

        //通知医院修改订单状态
        HospitalSet hospitalSet = hospSetFeignClient.getHospitalSet(orderInfo.getHoscode());

        Map<String, Object> map = new HashMap<>();
        map.put("hoscode", hospitalSet.getHoscode());
        map.put("hosOrderId", orderInfo.getHosOrderId());
        map.put("timestamp", System.currentTimeMillis());
        map.put("sign", HttpRequestHelper.getSign(map, hospitalSet.getSignKey()));
        JSONObject response = HttpRequestHelper.sendRequest(map, hospitalSet.getApiUrl() + "/order/updatePayStatus");
        //解析响应
        if (response.getInteger("code") != 200) {
            throw new GuiguException(ResultCodeEnum.FAIL.getCode(), response.getString("message"));
        }
    }

}
