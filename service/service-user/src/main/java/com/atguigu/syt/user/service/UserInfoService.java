package com.atguigu.syt.user.service;


import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.vo.user.UserAuthVo;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-07
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getByOpenId(String openId);

    boolean updateUserInfo(Long uid, UserAuthVo userAuthVo);

    UserInfo getAuthUserInfo(Long aLong);

    Page<UserInfo> selectPage(Integer pageNum, Integer pageSize, UserInfoQueryVo userInfoQueryVo);

    boolean approval(Integer id, Integer authStatus);

    boolean lock(Integer id, Integer status);

    void bindPhone(String phone, String code,Long uid);
}
