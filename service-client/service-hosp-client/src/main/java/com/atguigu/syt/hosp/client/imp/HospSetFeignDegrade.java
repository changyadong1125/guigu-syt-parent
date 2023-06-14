package com.atguigu.syt.hosp.client.imp;

import com.atguigu.syt.hosp.client.HospSetFeignClient;
import com.atguigu.syt.model.hosp.HospitalSet;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.client.imp
 * class:HospSetFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/14-10:47
 * @Version: v1.0
 * @Description:
 */
@Component
public class HospSetFeignDegrade implements HospSetFeignClient {

    @Override
    public HospitalSet getHospitalSet(String hoscode) {
        return null;
    }
}
