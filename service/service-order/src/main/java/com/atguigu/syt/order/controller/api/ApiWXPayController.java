package com.atguigu.syt.order.controller.api;

import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.enums.PaymentStatusEnum;
import com.atguigu.syt.enums.RefundStatusEnum;
import com.atguigu.syt.order.config.WxPayConfig;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.order.service.PaymentInfoService;
import com.atguigu.syt.order.service.RefundInfoService;
import com.atguigu.syt.order.utils.RequestUtils;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.api
 * class:ApiWXPayController
 *
 * @author: smile
 * @create: 2023/6/16-18:56
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/api/order/wxpay")
@Api("微信支付接口")
@Slf4j
public class ApiWXPayController {
    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private RefundInfoService refundInfoService;
    @Resource
    private OrderInfoService orderInfoService;


    @PostMapping("/refunds/notify")
    public Map<String,String> callback(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> map = new HashMap<>();
        try {
            String signature = request.getHeader("Wechatpay-Signature");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String wechatPayCertificateSerialNumber = request.getHeader("Wechatpay-Serial");
            String requestBody = RequestUtils.readData(request);
            // 构造 RequestParam
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(wechatPayCertificateSerialNumber)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();
            // 初始化 NotificationParser
            NotificationParser parser = new NotificationParser(wxPayConfig.getConfig());

            // 验签、解密并转换成 Transaction
            RefundNotification refundNotification = parser.parse(requestParam, RefundNotification.class);

            String outTradeNo = refundNotification.getOutTradeNo();
            System.out.println(refundNotification.getRefundStatus().toString());

            if (paymentInfoService.getPaymentInfoStatus(outTradeNo).equals(PaymentStatusEnum.REFUND.getStatus())) {
                response.setStatus(200);
                return map;
            }
            if (refundNotification.getRefundStatus().toString().equals("SUCCESS")) {
                //修改退款记录状态
                refundInfoService.updateRefundInfoStatus(outTradeNo, RefundStatusEnum.REFUND);
                //修改支付订单状态
                paymentInfoService.updateStatus(outTradeNo, PaymentStatusEnum.REFUND.getStatus());
                //修改订单状态
                orderInfoService.updateStatus(outTradeNo, OrderStatusEnum.CANCLE_REFUND.getStatus());
                response.setStatus(200);
                map.put("code", "SUCCESS");
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "系统错误");
            }
            return map;
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "系统错误");
            return map;
        }
    }
}
