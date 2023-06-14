package com.atguigu.syt.hosp.service;


import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-29
 */
public interface HospitalSetService extends IService<HospitalSet> {

    Page<HospitalSet> selectPage(Integer pageNum, Integer pageSize, HospitalSetQueryVo hospitalSetQueryVo);

    String getSignKey(String hoscode);
     HospitalSet getHospitalSet(String hoscode);
}
