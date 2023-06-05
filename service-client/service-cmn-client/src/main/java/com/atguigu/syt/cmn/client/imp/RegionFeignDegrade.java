package com.atguigu.syt.cmn.client.imp;

import com.atguigu.syt.cmn.client.RegionFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.client.imp
 * class:RegionFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/5-20:35
 * @Version: v1.0
 * @Description:
 */
@Component
public class RegionFeignDegrade implements FallbackFactory<RegionFeignClient> {
    @Override
    public RegionFeignClient create(Throwable throwable) {
        return new RegionFeignClient() {
            @Override
            public String getRegionNameByCode(String code) {
                return "sorry,data is failed! please try latter!";
            }
        };
    }
}
