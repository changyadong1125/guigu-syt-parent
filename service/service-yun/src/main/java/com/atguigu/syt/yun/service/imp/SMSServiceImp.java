package com.atguigu.syt.yun.service.imp;


import com.atguigu.syt.vo.sms.SmsVo;
import com.atguigu.syt.yun.service.SMSService;
import com.atguigu.syt.yun.utils.HttpUtils;
import com.atguigu.syt.yun.utils.RandomUtil;
import org.apache.http.HttpResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.order.service.impl
 * class:SMSServiceImp
 *
 * @author: smile
 * @create: 2023/6/17-14:32
 * @Version: v1.0
 * @Description:
 */
@Service
@SuppressWarnings("all")
public class SMSServiceImp implements SMSService {
    /*
    redis 的自动配置类中要求 键和值都得是string或者是object 不能混用
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:发送短信验证码
     */
    @Override
    public boolean sendCode(String phone) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "2e1efc197f3e46e39962eef2cf640ded";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        String fourBitRandom = RandomUtil.getFourBitRandom();
        System.out.println(fourBitRandom);
        redisTemplate.opsForValue().set("code:" + phone, "高振开傻蛋", 5, TimeUnit.MINUTES);

        bodys.put("content", "code:" + "高振开傻蛋");
        bodys.put("phone_number", phone);
        bodys.put("template_id", "CST_ptdie100");
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:发送短信验证码
     */
    @Override
    public void sendMessage(SmsVo smsVo) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "2e1efc197f3e46e39962eef2cf640ded";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
//        String fourBitRandom = RandomUtil.getFourBitRandom();
        String number = (String) smsVo.getParam().get("number");
        redisTemplate.opsForValue().set("code:" + smsVo.getPhone(), number, 5, TimeUnit.MINUTES);
        bodys.put("content", "code:" + number);
        bodys.put("phone_number", smsVo.getPhone());
        bodys.put("template_id", smsVo.getTemplateCode());
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("发送成功··································");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:取消预约通知
     */
    @Override
    public void sendMessageCancel(SmsVo smsVo) {
        System.out.println("==================================Cancel=======================================");
        System.out.println(smsVo.getPhone() + "取消成功！！！！！");
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:挂号成功 定时通知
     */
    @Override
    public void sendMessageRemind(SmsVo smsVo) {
        System.out.println("==================================Remind=======================================");
        System.out.println("预约成功->" + smsVo.getPhone());
    }
}
