package com.atguigu.syt.user.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.service.utils.HttpUtil;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.UserStatusEnum;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.user.utils.ConstantProperties;
import com.atguigu.syt.vo.user.UserVo;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.controller.api
 * class:ApiWxController
 *
 * @author: smile
 * @create: 2023/6/8-10:35
 * @Version: v1.0
 * @Description:
 */
@Api("/微信回调接口")
@RequestMapping("/api/user/wx")
@Controller
public class ApiWxController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ConstantProperties constantProperties;
    @Resource
    private AuthContextHolder authContextHolder;

    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, HttpServletRequest request, HttpServletResponse response) {


        try {
            HttpSession session = request.getSession();
            String openStatus = (String) session.getAttribute("wx_open_status");
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state) || StringUtils.isEmpty(openStatus)) {
                throw new GuiguException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            //使用code和appid以及appscrect换取access_token
            StringBuilder baseAccessTokenUrl = new StringBuilder()
                    .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                    .append("?appid=%s")
                    .append("&secret=%s")
                    .append("&code=%s")
                    .append("&grant_type=authorization_code");

            String accessTokenUrl = String.format(baseAccessTokenUrl.toString(), constantProperties.getAppId(), constantProperties.getAppSecret(), code);
            //使用httpclient发送请求
            byte[] respdata = HttpUtil.doGet(accessTokenUrl);
            String stringResponseDate = new String(respdata);

            JSONObject respJson = JSONObject.parseObject(stringResponseDate);
            if (respJson.getString("errcode") != null) {
                throw new GuiguException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }

            String accessToken = respJson.getString("access_token");
            String openId = respJson.getString("openid");

            UserInfo userInfo = userInfoService.getByOpenId(openId);
            if (userInfo != null) { //存在

                if (Objects.equals(userInfo.getStatus(), UserStatusEnum.LOCK.getStatus())) {
                    throw new GuiguException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
                }
            } else {
                //使用access_token换取受保护的资源：微信的个人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //使用httpclient发送请求
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
                byte[] respdataUser = HttpUtil.doGet(userInfoUrl);
                String resultUserInfo = new String(respdataUser);

                JSONObject resultUserInfoJson = JSONObject.parseObject(resultUserInfo);
                if (resultUserInfoJson.getString("errcode") != null) {
                    throw new GuiguException(ResultCodeEnum.FETCH_USERINFO_ERROR);
                }

                //解析用户信息
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                userInfo = new UserInfo();
                userInfo.setOpenid(openId);
                userInfo.setNickName(nickname);
                userInfo.setHeadImgUrl(headimgurl);
                userInfo.setStatus(UserStatusEnum.NORMAL.getStatus());
                userInfoService.save(userInfo);
            }

            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            UserVo userVo = new UserVo();
            userVo.setUserId(userInfo.getId());
            userVo.setName(name);
            userVo.setHeadimgurl(userInfo.getHeadImgUrl());
            authContextHolder.setToken(response,userVo);

            return "redirect:" + constantProperties.getSytBaseUrl();

        } catch (GuiguException e) {
            return "redirect:" + constantProperties.getSytBaseUrl()
                    + "?code=201&message=" + URLEncoder.encode(e.getMsg());
        } catch (Exception e) {
            return "redirect:" + constantProperties.getSytBaseUrl()
                    + "?code=201&message=" + URLEncoder.encode("登录失败");
        }
    }
}
