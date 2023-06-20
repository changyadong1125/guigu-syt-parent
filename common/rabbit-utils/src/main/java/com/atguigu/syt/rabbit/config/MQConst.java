package com.atguigu.syt.rabbit.config;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.rabbit.config
 * class:MQConst
 *
 * @author: smile
 * @create: 2023/6/19-9:25
 * @Version: v1.0
 * @Description:
 */
public class MQConst {

    /**
     * 预约/取消订单
     */
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_KEY_ORDER = "routing.key.order";
    public static final String QUEUE_ORDER  = "queue.order";

    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_SMS = "exchange.direct.sms";
    public static final String ROUTING_KEY_SMS = "routing.sms";
    public static final String QUEUE_SMS  = "queue.sms";

    public static final String EXCHANGE_DIRECT_SMS_CANCEL = "exchange.direct.sms.cancel";
    public static final String ROUTING_KEY_SMS_CANCEL = "routing.sms.cancel";
    public static final String QUEUE_SMS_CANCEL  = "queue.sms.cancel";

    public static final String EXCHANGE_DIRECT_SMS_REMIND = "exchange.direct.sms.remind";
    public static final String ROUTING_KEY_SMS_REMIND = "routing.sms.remind";
    public static final String QUEUE_SMS_REMIND  = "queue.sms.remind";

    public static final String EXCHANGE_DIRECT_USER_IMG = "exchange.direct.user.img";
    public static final String ROUTING_KEY_USER_IMG = "routing.user.img";
    public static final String QUEUE_USER_IMG  = "queue.user.img";
}
