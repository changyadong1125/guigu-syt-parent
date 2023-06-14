package com.atguigu.syt.hosp.client;

import com.atguigu.syt.hosp.client.imp.HospSetFeignDegrade;
import com.atguigu.syt.model.hosp.HospitalSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.client
 * class:HospSetFeignClient
 *
 * @author: smile
 * @create: 2023/6/14-10:36
 * @Version: v1.0
 * @Description:
 */
@FeignClient(value = "service-hosp", contextId = "hospSetFeignClient", path = "/inner/hosp/hospset",fallback = HospSetFeignDegrade.class)
public interface HospSetFeignClient {
    @GetMapping("/info/{hoscode}")
    public HospitalSet getHospitalSet(@PathVariable("hoscode") String hoscode) ;
}
