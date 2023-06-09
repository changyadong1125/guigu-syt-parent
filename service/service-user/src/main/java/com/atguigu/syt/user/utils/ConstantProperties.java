package com.atguigu.syt.user.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.utils
 * class:ConstantProperties
 *
 * @author: smile
 * @create: 2023/6/7-19:32
 * @Version: v1.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "wx.open")
@Data
public class ConstantProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
    private String sytBaseUrl;
}
