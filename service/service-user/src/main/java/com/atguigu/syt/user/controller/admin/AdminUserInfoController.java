package com.atguigu.syt.user.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/admin/user/userInfo")
@Api("用户接口文档")
@RequiredArgsConstructor
public class AdminUserInfoController {
    private final UserInfoService userInfoService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有用户信息
     */
    @GetMapping("/page/{pageNum}/{pageSize}")
    @ApiOperation("用户分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "limit", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "userInfoQueryVo", value = "查询对象", required = false)
    })
    public Result<?> page(@PathVariable Integer pageNum, @PathVariable Integer pageSize, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = userInfoService.selectPage(pageNum, pageSize, userInfoQueryVo);
        return Result.ok(page);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改用户认证状态
     */
    @GetMapping("/approval/{id}/{authStatus}")
    @ApiOperation("修改用户认证状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true),
            @ApiImplicitParam(name = "authStatus", value = "用户认证状态", required = true),
    })
    public Result<?> approval(@PathVariable Integer id, @PathVariable Integer authStatus) {
        boolean flag = userInfoService.approval(id, authStatus);
        return flag ? Result.ok() : Result.fail();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改用户状态
     */
    @GetMapping("/lock/{id}/{status}")
    @ApiOperation("修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true),
            @ApiImplicitParam(name = "authStatus", value = "用户认证状态", required = true),
    })
    public Result<?> lock(@PathVariable Integer id, @PathVariable Integer status) {
        boolean flag = userInfoService.lock(id, status);
        return flag ? Result.ok() : Result.fail();
    }
}

