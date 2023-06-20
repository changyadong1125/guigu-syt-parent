package com.atguigu.syt.yun.service.imp;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.syt.vo.ImgVo.IMGVo;
import com.atguigu.syt.yun.service.ImgService;
import com.atguigu.syt.yun.utils.OssConstantProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.service.imp
 * class:ImgServiceImp
 *
 * @author: smile
 * @create: 2023/6/19-22:46
 * @Version: v1.0
 * @Description:
 */
@Service
public class ImgServiceImp implements ImgService {
    @Resource
    private OssConstantProperties ossConstantProperties;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;


    @Override
    public void removeImg(IMGVo imgVo) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConstantProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossConstantProperties.getKeyId();
        String accessKeySecret = ossConstantProperties.getKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConstantProperties.getBucketName();
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        Set<Object> objects = imgVo.getObjectsName();
        Long uid = imgVo.getUid();
        objects.forEach(A -> {
            String objectName = (String) A;
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            try {
                // 删除文件或目录。如果要删除目录，目录必须为空。
                ossClient.deleteObject(bucketName, objectName);
                redisTemplate.delete(uid + ":beforeCommit");
                redisTemplate.delete(uid + ":afterCommit");
            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        });
    }
}
