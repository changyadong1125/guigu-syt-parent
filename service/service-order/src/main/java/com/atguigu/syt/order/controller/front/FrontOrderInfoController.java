package com.atguigu.syt.order.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.order.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.front
 * class:FrontOrderInfoController
 *
 * @author: smile
 * @create: 2023/6/13-15:37
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/order/orderInfo")
@Api("预约订单文档")
@RequiredArgsConstructor
public class FrontOrderInfoController {
    private final OrderInfoService orderInfoService;
    private final AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:保存订单信息
     */
    @PostMapping("/order/{patientId}/{scheduleId}")
    @ApiOperation("保存订单信息")
    public Result<?> orderInfoSave(@PathVariable String scheduleId, @PathVariable Long patientId, HttpServletResponse response, HttpServletRequest request){
        Long uid = authContextHolder.checkAuth(request, response);
        Long orderId = orderInfoService.saveOrderInfo(uid,scheduleId,patientId);
        return Result.ok(orderId);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取订单详情信息
     */
    @GetMapping("/info/{orderId}")
    @ApiOperation("获取订单详情")
    public Result<?> getOrderInfo(@PathVariable Long orderId, HttpServletResponse response, HttpServletRequest request) {
        Long uid = authContextHolder.checkAuth(request, response);
        OrderInfo orderInfo = orderInfoService.getOrderInfo(uid,orderId);
        return Result.ok(orderInfo);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取订单列表
     */
    @ApiOperation("订单列表")
    @GetMapping("/list")
    public Result<List<OrderInfo>> list(HttpServletRequest request, HttpServletResponse response) {
        Long userId = authContextHolder.checkAuth(request, response);
        List<OrderInfo> orderInfoList = orderInfoService.selectList(userId);
        return Result.ok(orderInfoList);
    }

}
