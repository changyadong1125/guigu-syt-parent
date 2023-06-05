package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.data.domain.Page;

import java.util.HashMap;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service
 * class:HospitalService
 *
 * @author: smile
 * @create: 2023/6/3-21:05
 * @Version: v1.0
 * @Description:
 */
public interface HospitalService {
    void save(HashMap<String, Object> parameterMap);


    Hospital getHospital(String hoscode);

    Page<Hospital> getHospitalPageList(Integer pageNum, Integer pageSize, String hosname);

    void updateStatus(String hoscode, Integer status);
}