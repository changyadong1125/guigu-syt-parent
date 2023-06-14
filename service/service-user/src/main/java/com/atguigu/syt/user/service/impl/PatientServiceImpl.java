package com.atguigu.syt.user.service.impl;


import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.cmn.client.RegionFeignClient;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.mapper.PatientMapper;
import com.atguigu.syt.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-07
 */
@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    private final DictFeignClient dictFeignClient;
    private final RegionFeignClient regionFeignClient;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件删除就诊人信息
     */
    @Override
    public boolean delete(Long uid, Long id) {
        LambdaQueryWrapper<Patient> patientLambdaQueryWrapper = new LambdaQueryWrapper<Patient>();
        patientLambdaQueryWrapper.eq(Patient::getUserId, uid).eq(Patient::getId, id);
        return baseMapper.delete(patientLambdaQueryWrapper) > 0;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件查询就诊人信息
     */
    @Override
    public Patient selectByIdAndUid(Long id, Long uid) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getUserId,uid).eq(Patient::getId,id);
        Patient patient = baseMapper.selectOne(queryWrapper);
        this.packagePatient(patient);
        return patient;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有就诊人信息
     */
    @Override
    public List<Patient> getAll(Long uid) {
        LambdaQueryWrapper<Patient> patientLambdaQueryWrapper = new LambdaQueryWrapper<>();
        patientLambdaQueryWrapper.eq(Patient::getUserId,uid);
        List<Patient> patients = baseMapper.selectList(patientLambdaQueryWrapper);
        patients.forEach(this::packagePatient);
        return patients;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据就诊人id获取就诊人信息
     */
    @Override
    public Patient getPatient(Long id) {
        return this.getById(id);

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:封装就诊人信息
     */
    private void packagePatient(Patient patient) {
        String certificatesType = patient.getCertificatesType();
        String contactsCertificatesType = patient.getContactsCertificatesType();
        String provinceCode = patient.getProvinceCode();
        String cityCode = patient.getCityCode();
        String districtCode = patient.getDistrictCode();

        String certificatesTypeString = dictFeignClient.getDicTypeByHosType(certificatesType, DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId());
        String contactsCertificatesTypeString = dictFeignClient.getDicTypeByHosType(contactsCertificatesType, DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId());

        String provinceString = regionFeignClient.getRegionNameByCode(provinceCode);
        String cityString = regionFeignClient.getRegionNameByCode(cityCode);
        String districtString = regionFeignClient.getRegionNameByCode(districtCode);

        patient.getParam().put("expenseMethod", patient.getIsInsure() == 0 ? "自费" : "医保");
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString",contactsCertificatesTypeString);
        patient.getParam().put("provinceString",provinceString);
        patient.getParam().put("cityString",cityString);
        patient.getParam().put("districtString",districtString);
        patient.getParam().put("fullAddress",provinceString+cityString+districtString+patient.getAddress());




    }
}
