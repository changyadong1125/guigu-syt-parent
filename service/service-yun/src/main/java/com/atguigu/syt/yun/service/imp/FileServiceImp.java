package com.atguigu.syt.yun.service.imp;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.yun.service.FileService;
import com.atguigu.syt.yun.utils.OssConstantProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.service.imp
 * class:FileServiceImp
 *
 * @author: smile
 * @create: 2023/6/9-20:24
 * @Version: v1.0
 * @Description:
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImp implements FileService {
    private final OssConstantProperties ossConstantProperties;
    private final RedisTemplate<Object,Object> redisTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:文件上传
     */
    @Override
    public Map<String, String> upload(MultipartFile file,Long uid) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConstantProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossConstantProperties.getKeyId();
        String accessKeySecret = ossConstantProperties.getKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConstantProperties.getBucketName();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String objectName = new DateTime().toString("yyyy/MM/dd") + "/" + UUID.randomUUID().toString().replaceAll("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
//            log.info(Integer.toString(result.getResponse().getStatusCode()));
//            if (result.getResponse().getStatusCode() != 200) {
//                throw new GuiguException(ResultCodeEnum.FAIL);
//            }
            // 设置签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            HashMap<String, String> map = new HashMap<>();
            map.put("previewUrl", url.toString()); //页面中授权预览图片
            map.put("url", objectName); //数据库存储
//           保存到redis中
            redisTemplate.opsForSet().add(uid+":beforeCommit",objectName);
            return map;
        } catch (Exception oe) {
            log.info(ExceptionUtils.getStackTrace(oe));
            throw new GuiguException(ResultCodeEnum.FAIL, oe);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据objectName获取文件的地址
     */
    @Override
    public String getPreviewUrl(String objectName) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConstantProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossConstantProperties.getKeyId();
        String accessKeySecret = ossConstantProperties.getKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConstantProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return url.toString();
    }
}
