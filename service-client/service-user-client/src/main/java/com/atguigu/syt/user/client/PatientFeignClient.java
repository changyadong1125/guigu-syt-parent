package com.atguigu.syt.user.client;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.client.imp.PatientFeignDegrade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.client
 * class:UserFeignClient
 *
 * @author: smile
 * @create: 2023/6/14-10:27
 * @Version: v1.0
 * @Description:
 */
@FeignClient(name = "service-user",contextId = "PatientFeignClient",path = "/inner/user/patient",fallback = PatientFeignDegrade.class)
public interface PatientFeignClient {
    @GetMapping("/info/{id}")
    public Patient getPatient(@PathVariable("id") Long id) ;
}
