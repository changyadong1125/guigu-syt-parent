package com.atguigu.syt.user.controller.front;

import com.atguigu.syt.user.utils.ConstantProperties;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ThreadLocalRandom;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.controller.front
 * class:FrontWxController
 *
 * @author: smile
 * @create: 2023/6/7-19:34
 * @Version: v1.0
 * @Description:
 */

@Api(tags = "微信扫码登录")
@Controller//注意这里没有配置 @RestController
@RequestMapping("/front/user/wx")
public class FrontWxController {
    @Resource
    private ConstantProperties constantProperties;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:微信登录接口
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        long aLong = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        String hexString = Long.toHexString(aLong);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder append = stringBuilder.append("https://open.weixin.qq.com/connect/qrconnect")
                .append("?appid=%s")
                .append("&redirect_uri=%s")
                .append("&response_type=code")
                .append("&scope=snsapi_login")
                .append("&state=%s")
                .append("#wechat_redirect");
        String format = String.format(append.toString(), constantProperties.getAppId(), constantProperties.getRedirectUri(), hexString);
        HttpSession session = request.getSession();
        session.setAttribute("wx_open_status",hexString);
        return "redirect:" + format;
    }
}
