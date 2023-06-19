package com.atguigu.syt.yun.service;

import com.atguigu.syt.vo.sms.SmsVo;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.service
 * class:SMSService
 *
 * @author: smile
 * @create: 2023/6/17-14:32
 * @Version: v1.0
 * @Description:
 */
public interface SMSService {
    boolean sendCode(String phone);

    void sendMessage(SmsVo smsVo);

    void sendMessageCancel(SmsVo smsVo);

    void sendMessageRemind(SmsVo smsVo);
}
