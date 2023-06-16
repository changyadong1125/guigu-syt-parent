package com.atguigu.common.service.utils;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.vo.user.UserVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.common.service.utils
 * class:AuthContextHolder
 *
 * @author: smile
 * @create: 2023/6/9-20:41
 * @Version: v1.0
 * @Description:授权校验
 */
@Component
public class AuthContextHolder {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:验证用户是否登录
     */
    public Long checkAuth(HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtils.getCookie(request, "token");
        if (!StringUtils.isEmpty(token)) return this.refreshToken(request,response);
        UserVo userVo = (UserVo) redisTemplate.opsForValue().get("user:token" + token);
        if (userVo == null) return this.refreshToken(request,response);
        return userVo.getUserId();
    }

    private Long refreshToken(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookie(request, "refreshToken");
        if (StringUtils.isEmpty(refreshToken)) throw new GuiguException(ResultCodeEnum.LOGIN_TIMEOUT);
        UserVo userVo = (UserVo) redisTemplate.opsForValue().get("user:refreshToken:" + refreshToken);
        if (userVo == null) throw new GuiguException(ResultCodeEnum.LOGIN_TIMEOUT);
        setToken(response,userVo);
        return userVo.getUserId();
    }

    public void setToken(HttpServletResponse response, UserVo userVo) {
        //生成token
        String token = getToken();
        String refreshToken = getToken();
        //将token做key，用户id做值存入redis
        int maxRedisTime = 3000000;
        redisTemplate.opsForValue().set("user:token:" + token, userVo, maxRedisTime, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("user:refreshToken:" + refreshToken, userVo, maxRedisTime * 2, TimeUnit.MINUTES);
        //将token和name存入cookie
        //将"资料>微信登录>CookieUtils.java"放入service-utils模块
        int cookieMaxTime = 60000000 * 30;//30分钟
        CookieUtils.setCookie(response, "token", token, cookieMaxTime);
        CookieUtils.setCookie(response, "refreshToken", refreshToken, cookieMaxTime * 2);
        CookieUtils.setCookie(response, "name", URLEncoder.encode(userVo.getName()), cookieMaxTime * 2);
        CookieUtils.setCookie(response, "headimgurl", URLEncoder.encode(userVo.getHeadimgurl()), cookieMaxTime * 2);
    }

    private static String getToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
