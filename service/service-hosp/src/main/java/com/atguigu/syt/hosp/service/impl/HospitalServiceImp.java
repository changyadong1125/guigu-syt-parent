package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.cmn.client.RegionFeignClient;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.hosp.repository.HospitalRepository;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.data.domain.*;
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
    @Resource
    private DictFeignClient dictFeignClient;
    @Resource
    private RegionFeignClient regionFeignClient;

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

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:医院信息分页查询
     */
    @Override
    public Page<Hospital> getHospitalPageList(Integer pageNum, Integer pageSize, String hosname) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.asc("hoscode")));
        Hospital hospital = new Hospital();
        if (hosname != null) {
            hospital.setHosname(hosname);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> page = hospitalRepository.findAll(example, pageRequest);
        page.stream().forEach(this::pageRegionInfoByCode);
        return page;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改医院状态
     */
    @Override
    public void updateStatus(String hoscode, Integer status) {
        if (status == 0 | status == 1) {
            Hospital hospital = new Hospital();
            hospital.setHoscode(hoscode);
            Example<Hospital> example = Example.of(hospital);
            Optional<Hospital> optionalHospital = hospitalRepository.findOne(example);
            if (optionalHospital.isPresent()) {
                Hospital mongoHospital = optionalHospital.get();
                mongoHospital.setStatus(status);
                hospitalRepository.save(mongoHospital);
            }
        } else {
            throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        }

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 根据地区编码获取地区名称
     * 整合OpenFeign调用Cmn微服务查询地区名称
     */
    private void pageRegionInfoByCode(Hospital mongoHospital) {
        String hostype = mongoHospital.getHostype();
        String provinceCode = mongoHospital.getProvinceCode();
        String cityCode = mongoHospital.getCityCode();
        String districtCode = mongoHospital.getDistrictCode();

        String hosTypeName = dictFeignClient.getDicTypeByHosType(hostype, DictTypeEnum.HOSTYPE.getDictTypeId());
        String provinceName = regionFeignClient.getRegionNameByCode(provinceCode);
        String cityName = regionFeignClient.getRegionNameByCode(cityCode);
        String districtName = regionFeignClient.getRegionNameByCode(districtCode);
        if (cityName.equals(provinceName)) cityName = "";
        mongoHospital.setHostype(hosTypeName);
        mongoHospital.getParam().put("hosTypeName", mongoHospital.getHostype());
        mongoHospital.getParam().put("address", provinceName + cityName + districtName + mongoHospital.getAddress());
    }
}
