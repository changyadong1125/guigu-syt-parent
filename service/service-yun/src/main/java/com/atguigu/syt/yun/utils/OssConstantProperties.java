package com.atguigu.syt.yun.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.utils
 * class:OssConstantProperties
 *
 * @author: smile
 * @create: 2023/6/9-20:19
 * @Version: v1.0
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix="aliyun.oss") //读取节点
@Data
public class OssConstantProperties {
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
}