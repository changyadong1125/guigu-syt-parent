package com.atguigu.syt.yun.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.yun.service.SMSService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.front
 * class:FrontSMSController
 *
 * @author: smile
 * @create: 2023/6/17-14:29
 * @Version: v1.0
 * @Description:
 */
@RequestMapping("/front/yun/sms")
@RestController
public class FrontSMSController {
    @Resource
    private SMSService smsService;

    @Resource
    private AuthContextHolder authContextHolder;

    @PostMapping("/code/{phone}")
    @ApiOperation("给手机发送验证码")
    public Result<?> sendCode(@PathVariable String phone, HttpServletRequest request, HttpServletResponse response) {
        authContextHolder.checkAuth(request, response);
        boolean flag = smsService.sendCode(phone);
        return flag ? Result.ok().message("发送成功") : Result.fail().message("发送失败");
    }

}
