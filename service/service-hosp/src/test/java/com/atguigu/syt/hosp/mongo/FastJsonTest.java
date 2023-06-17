package com.atguigu.syt.hosp.mongo;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:FastJsonTest
 *
 * @author: smile
 * @create: 2023/6/3-14:37
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class FastJsonTest {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 可以将任意java对象转换成---------------------> Json字符串 String str = JSONObject.toJsonString(java对象)
     * Json字符串------------------------------>任意java对象
     */
    @Test
    public void testJson(){
        User1 user = new User1(null, "zhuzhuxia", 18, "wang@gmail.com", new Date());
        HashMap<String, Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");
        String jsonString = JSONObject.toJSONString(user);
        String jsonString_map = JSONObject.toJSONString(map);
        System.out.println("jsonString = " + jsonString);
        System.out.println("jsonString_map = " + jsonString_map);
    }
    @Test
    public void test(){
        List<User1> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User1(null, "王超"+i, i, "wang@gmail.com", new Date())) ;
        }
        String jsonString = JSONObject.toJSONString(users);
        System.out.println("jsonString = " + jsonString);
        JSONObject.parseArray(jsonString,User.class).forEach(System.out::println);
        JSONObject.parseArray(jsonString,Map.class).forEach(System.out::println);
    }
}
