package com.atguigu.syt.order.config;

import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
//import com.wechat.pay.java.core.util.PemUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.InputStream;
import java.security.PrivateKey;


/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.controller.config
 * class:WxPayConfig
 *
 * @author: smile
 * @create: 2023/6/14-20:59
 * @Version: v1.0
 * @Description: propertySource支持properties文件   不支持@EnableConfigrationProperties注解
 */
@Configuration
@ConfigurationProperties(prefix = "wxpay")
@PropertySource("classpath:wxpay.properties")
@Data
public class WxPayConfig {
    // 商户号
    private String mchId;

    // 商户API证书序列号
    private String mchSerialNo;

    // 商户私钥文件
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key;

    // APPID
    private String appid;

    // 接收支付结果通知地址
    private String notifyUrl;

    // 接收退款结果通知地址
    private String notifyRefundUrl;

    private PrivateKey getPrivateKey(String fileName) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/" + fileName);
            return PemUtil.loadPrivateKey(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("秘钥不存在！");
        }

    }

    /**
     * 获取微信支付配置对象
     */
    @Bean
    public RSAAutoCertificateConfig getConfig() {

        return new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
//                .privateKeyFromPath(privateKeyPath)
                .privateKey(getPrivateKey(privateKeyPath))
                .merchantSerialNumber(mchSerialNo)
                .apiV3Key(apiV3Key)
                .build();
    }

}
