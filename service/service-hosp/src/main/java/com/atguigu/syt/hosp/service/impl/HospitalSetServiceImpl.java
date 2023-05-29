package com.atguigu.syt.hosp.service.impl;

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
}
