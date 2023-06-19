package com.atguigu.syt.user.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.controller.front
 * class:FrontUserInfoController
 *
 * @author: smile
 * @create: 2023/6/9-20:51
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/front/user/userInfo")
public class FrontUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:用户认证
     */
    @ApiOperation(value = "用户认证")
    @ApiImplicitParam(name = "userAuthVo", value = "用户实名认证对象", required = true)
    @PutMapping("/auth")
    public Result<?> userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        boolean flag = userInfoService.updateUserInfo(uid, userAuthVo);
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取用户认证信息
     */
    @GetMapping("/getUserAuthInfo")
    @ApiOperation("获取用户认证信息")
    public Result<?> getAuthUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Long aLong = authContextHolder.checkAuth(request, response);
        UserInfo userInfo = userInfoService.getAuthUserInfo(aLong);
        return Result.ok(userInfo);
    }

    @PutMapping("/bind/{phone}/{code}")
    @ApiOperation("绑定手机号")
    public Result<?> bindPhone(@PathVariable String phone, @PathVariable String code, HttpServletRequest request, HttpServletResponse response) {
        Long uid = authContextHolder.checkAuth(request, response);
        userInfoService.bindPhone(phone, code,uid);
        return Result.ok();
    }
}
