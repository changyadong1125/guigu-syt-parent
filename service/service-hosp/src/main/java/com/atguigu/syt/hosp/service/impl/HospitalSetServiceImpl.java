package com.atguigu.syt.hosp.service.impl;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.hosp.entity.HospitalSet;
import com.atguigu.syt.hosp.mapper.HospitalSetMapper;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 医院设置表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-29
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:医院设置信息分页
     */
    @Override
    public Page<HospitalSet> selectPage(Integer pageNum, Integer pageSize, HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<HospitalSet> hospitalSetLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hospitalSetLambdaQueryWrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname()), HospitalSet::getHosname, hospitalSetQueryVo.getHosname());
        hospitalSetLambdaQueryWrapper.eq(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()), HospitalSet::getHoscode, hospitalSetQueryVo.getHoscode());
        return baseMapper.selectPage(page, hospitalSetLambdaQueryWrapper);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 根据hoscode获取我方的签名信息
     */
    @Override
    public String getSignKey(String hoscode) {
        HospitalSet hospitalSet = getHospitalSet(hoscode);
        if (hospitalSet == null){
            throw new GuiguException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        if (hospitalSet.getStatus() == 0){
            throw new GuiguException(ResultCodeEnum.HOSPITAL_LOCK);
        }
        return hospitalSet.getSignKey();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 根据编号获取医院设置信息
     */
    public HospitalSet getHospitalSet(String hoscode) {
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HospitalSet::getHoscode, hoscode);
        return baseMapper.selectOne(queryWrapper);
    }


}
