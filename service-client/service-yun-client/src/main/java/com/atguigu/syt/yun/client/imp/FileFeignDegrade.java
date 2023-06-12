package com.atguigu.syt.yun.client.imp;

import com.atguigu.syt.yun.client.FileFeignClient;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.client.imp
 * class:FileFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/10-21:35
 * @Version: v1.0
 * @Description:
 */
@Component
public class FileFeignDegrade implements FileFeignClient {
    @Override
    public String getPreviewUrl(String objectName) {
        return "获取图片失败《~~》";
    }
}
