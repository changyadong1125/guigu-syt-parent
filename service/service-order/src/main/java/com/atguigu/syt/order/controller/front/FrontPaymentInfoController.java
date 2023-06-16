package com.atguigu.syt.order.controller.front;

import com.atguigu.syt.order.service.PaymentInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.front
 * class:FrontPaymentInfoController
 *
 * @author: smile
 * @create: 2023/6/15-10:39
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/order/paymentInfo")
@Api("支付记录文档")
@RequiredArgsConstructor
public class FrontPaymentInfoController {
    private final PaymentInfoService paymentInfoService;

}
