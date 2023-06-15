package com.atguigu.syt.order.service;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.service
 * class:WeiPayService
 *
 * @author: smile
 * @create: 2023/6/14-20:56
 * @Version: v1.0
 * @Description:
 */
public interface WeiPayService {
    String createNative(String outTradeNo);

    Boolean queryPayStatus(String outTradeNo);
}
