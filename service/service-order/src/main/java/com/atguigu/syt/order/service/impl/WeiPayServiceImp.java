package com.atguigu.syt.order.service.impl;

import com.atguigu.syt.order.controller.config.WxPayConfig;
import com.atguigu.syt.order.service.WeiPayService;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
