package com.atguigu.syt.cmn.client;

import com.atguigu.syt.cmn.client.imp.DicFeignDegrade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.client
 * class:DictFeignClient
 *
 * @author: smile
 * @create: 2023/6/5-19:04
 * @Version: v1.0
 * @Description:
 */
@FeignClient(value = "service-cmn", contextId = "DictFeignClient",fallback = DicFeignDegrade.class)
public interface DictFeignClient {

    @GetMapping("/inner/cmn/dict/getName/{hosType}/{dicTypeId}")
    public String getDicTypeByHosType(@PathVariable(value = "hosType") String hosType,@PathVariable(value = "dicTypeId") Long dicTypeId);
}
