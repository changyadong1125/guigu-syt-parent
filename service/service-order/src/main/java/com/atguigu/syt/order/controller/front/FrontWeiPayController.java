package com.atguigu.syt.order.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.order.service.WeiPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.front
 * class:WX
 *
 * @author: smile
 * @create: 2023/6/14-16:33
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/order/weipay")
@RequiredArgsConstructor
public class FrontWeiPayController {
    private final WeiPayService weiPayService;
    private final AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:微信支付 二维码url
     */
    @GetMapping("/url/{outTradeNo}")
    public Result<?> createNative(@PathVariable String outTradeNo, HttpServletRequest request, HttpServletResponse response) {
        authContextHolder.checkAuth(request, response);
        String url = weiPayService.createNative(outTradeNo);
        return Result.ok(url);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询用户是否支付成功
     */
    @GetMapping("/queryPayStatus/{outTradeNo}")
    public Result<?> queryPayStatus(@PathVariable String outTradeNo, HttpServletRequest request, HttpServletResponse response){
        authContextHolder.checkAuth(request, response);
        Boolean flag = weiPayService.queryPayStatus(outTradeNo);
        if (flag){
            return Result.ok().message("支付成功");
        }else{
            return Result.fail().message("支付中").code(250);
        }
    }
}
