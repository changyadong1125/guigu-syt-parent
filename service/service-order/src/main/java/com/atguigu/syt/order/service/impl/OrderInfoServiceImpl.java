package com.atguigu.syt.order.service.impl;


import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.order.mapper.OrderInfoMapper;
import com.atguigu.syt.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-13
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

}
