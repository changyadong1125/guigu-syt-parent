package com.atguigu.syt.user.service.impl;


import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.enums.AuthStatusEnum;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.enums.UserStatusEnum;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.mapper.UserInfoMapper;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserAuthVo;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.atguigu.syt.yun.client.FileFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-07
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    private final DictFeignClient dictFeignClient;
    private final FileFeignClient fileFeignClient;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询用户信息
     */
    @Override
    public UserInfo getByOpenId(String openId) {
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getOpenid, openId);
        return baseMapper.selectOne(userInfoLambdaQueryWrapper);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:用户认证
     */
    @Override
    public boolean updateUserInfo(Long uid, UserAuthVo userAuthVo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(uid);
        BeanUtils.copyProperties(userAuthVo, userInfo);
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        return baseMapper.updateById(userInfo) > 0;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取用户认证信息
     */
    @Override
    public UserInfo getAuthUserInfo(Long aLong) {
        UserInfo userInfo = baseMapper.selectById(aLong);
        this.packageUserInfo(userInfo);
        return userInfo;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:用户信息查询
     */

    @Override
    public Page<UserInfo> selectPage(Integer pageNum, Integer pageSize, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(!StringUtils.isEmpty(userInfoQueryVo.getKeyword()), A -> A.like(UserInfo::getName, userInfoQueryVo.getKeyword())
                        .or()
                        .like(UserInfo::getPhone, userInfoQueryVo.getKeyword()))
                .ge(!StringUtils.isEmpty(userInfoQueryVo.getCreateTimeBegin()), UserInfo::getCreateTime, userInfoQueryVo.getCreateTimeBegin())
                .le(!StringUtils.isEmpty(userInfoQueryVo.getCreateTimeEnd()), UserInfo::getUpdateTime, userInfoQueryVo.getCreateTimeEnd());
        page = baseMapper.selectPage(page, queryWrapper);
        page.getRecords().forEach(this::packageUserInfo);
        return page;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改用户认证状态
     */
    @Override
    public boolean approval(Integer id, Integer authStatus) {
        if (Objects.equals(authStatus, AuthStatusEnum.AUTH_SUCCESS.getStatus())
                || Objects.equals(authStatus, AuthStatusEnum.AUTH_FAIL.getStatus())) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(Long.valueOf(id));
            userInfo.setAuthStatus(authStatus);
            return this.updateById(userInfo);
        }
        return false;
    }

    @Override
    public boolean lock(Integer id, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(Long.valueOf(id));
            userInfo.setStatus(status);
            return this.updateById(userInfo);
        }
        return false;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:封装用户信息
     */
    private void packageUserInfo(UserInfo userInfo) {

        Integer authStatus = userInfo.getAuthStatus();
        if (authStatus != 0) {
            String certificatesTypeString = dictFeignClient.getDicTypeByHosType(userInfo.getCertificatesType(), DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId());
            userInfo.getParam().put("certificatesTypeString", certificatesTypeString);
            String previewUrl = fileFeignClient.getPreviewUrl(userInfo.getCertificatesUrl());
            userInfo.getParam().put("previewUrl", previewUrl);
        }

        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        userInfo.getParam().put("statusString", UserStatusEnum.getStatusNameByStatus(userInfo.getStatus()));
    }
}
