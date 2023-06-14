package com.atguigu.syt.user.client.imp;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.client.PatientFeignClient;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.client.imp
 * class:PatientFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/14-10:43
 * @Version: v1.0
 * @Description:
 */
@Component
public class PatientFeignDegrade implements PatientFeignClient {

    @Override
    public Patient getPatient(Long id) {
        return null;
    }
}
