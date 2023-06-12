package com.atguigu.syt.yun.client;

import com.atguigu.syt.yun.client.imp.FileFeignDegrade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.client
 * class:FileFeignClient
 *
 * @author: smile
 * @create: 2023/6/10-21:33
 * @Version: v1.0
 * @Description:
 */
@FeignClient(value = "service-yun", contextId = "FileFeignClient", path = "/inner/yun/file",fallback = FileFeignDegrade.class)
public interface FileFeignClient {
    @GetMapping("/getPreviewUrl")
    public String getPreviewUrl(@RequestParam String objectName);
}
