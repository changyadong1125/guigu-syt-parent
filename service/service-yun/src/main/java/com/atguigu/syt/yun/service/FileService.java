package com.atguigu.syt.yun.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.service.imp.service
 * class:FrontService
 *
 * @author: smile
 * @create: 2023/6/9-20:24
 * @Version: v1.0
 * @Description:
 */
public interface FileService {
    Map<String, String> upload(MultipartFile file,Long uid );

    String getPreviewUrl(String objectName);
}
