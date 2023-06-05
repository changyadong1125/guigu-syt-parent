package com.atguigu.syt.cmn.client;

import com.atguigu.syt.cmn.client.imp.RegionFeignDegrade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.client
 * class:RegionFeignClient
 *
 * @author: smile
 * @create: 2023/6/5-19:05
 * @Version: v1.0
 * @Description:
 */
@FeignClient(value = "service-cmn", contextId = "RegionFeignClient",fallbackFactory = RegionFeignDegrade.class)
public interface RegionFeignClient {
    @GetMapping("/inner/cmn/region/getName/{code}")
    String getRegionNameByCode(@PathVariable(value = "code") String code);
}
