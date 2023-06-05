package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.hosp.repository.HospitalRepository;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Optional;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service.impl
 * class:HospitalServiceIMp
 *
 * @author: smile
 * @create: 2023/6/3-21:06
 * @Version: v1.0
 * @Description:
 */
@Service
public class HospitalServiceImp implements HospitalService {
    @Resource
    private HospitalRepository hospitalRepository;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:将数据保存到mongo
     */
    @Override
    public void save(HashMap<String, Object> parameterMap) {
        //将JSON转换成hospital对象
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(parameterMap), Hospital.class);
        //根据医院编号查询该医院是否已经添加过
        Hospital exitHosp = hospitalRepository.getByHoscode(hospital.getHoscode());
        if (exitHosp != null) {
            hospital.setId(exitHosp.getId());
            hospital.setStatus(exitHosp.getStatus());
            hospitalRepository.save(hospital);
        } else {
            hospital.setStatus(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询医院信息
     */
    @Override
    public Hospital getHospital(String hoscode) {
        Hospital hospital = new Hospital();
        hospital.setHoscode(hoscode);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Optional<Hospital> hospitalOptional = hospitalRepository.findOne(example);
        if (hospitalOptional.isPresent()) {
            return hospitalOptional.get();
        } else {
            throw new GuiguException(ResultCodeEnum.HOSPITAL_NOT_EXIST);
        }

    }


}
