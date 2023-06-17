package com.atguigu.syt.yun;


import com.atguigu.syt.yun.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun
 * class:SMSTest
 *
 * @author: smile
 * @create: 2023/6/17-11:54
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class SMSTest {
    @Test
    public void test(){
        int a = 0;
        while (a<1){
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
            bodys.put("content", "code:"+"傻蛋");
            bodys.put("phone_number", "15911087639");
            bodys.put("template_id", "CST_ptdie100");


            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                System.out.println(response.toString());
                //获取response的body
                //System.out.println(EntityUtils.toString(response.getEntity()));
                a++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
