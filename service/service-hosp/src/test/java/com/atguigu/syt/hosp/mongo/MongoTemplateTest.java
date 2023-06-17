package com.atguigu.syt.hosp.mongo;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:MongoTemplateTest
 *
 * @author: smile
 * @create: 2023/6/3-10:23
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class MongoTemplateTest {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * insert和save都可以新增
     * 如果做修改只能用save   默认采用覆盖式的修改
     */
    @Test
    public void testAdd(){
        mongoTemplate.insert(new User1(null, "zhuzhuxia", 18, "wang@gmail.com", new Date()));
        mongoTemplate.insert(new User1(null, "zhuzhuxia", 18, "wang@gmail.com", new Date()));
    }
    @Test
    public void testFind(){
        User user = mongoTemplate.findById(new ObjectId(""), User.class);
        user.setName("999999999999");
        mongoTemplate.save(user);

    }
    @Test
    public void testRemove(){
        Query query = new Query(Criteria.where("name").is("jerry").and("age").is(13));
        Criteria criteria = new Criteria();
        Criteria criteria1 = criteria.orOperator(Criteria.where("name").is("jerry").and("age").is(13), Criteria.where("name").is("jerry"));
        mongoTemplate.remove(query,User.class);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:upsert没有就添加 有的话就进行修改
     */
    @Test
    public void testUpdate(){
        Query query = new Query(Criteria.where("name").is("88888"));
        Update updateDefinition = new Update();
        updateDefinition.set("age",555555);
        mongoTemplate.upsert(query,updateDefinition,User.class);
    }
    @Test
    public void test(){
        Query query = new Query(Criteria.where("age").gt(0));
        Update updateDefinition = new Update();
        updateDefinition.set("age",555555);
        mongoTemplate.updateFirst(query,updateDefinition, User.class);
        mongoTemplate.updateMulti(query,updateDefinition, User.class);
    }
    @Test
    public void testQuery(){
        mongoTemplate.find(new Query(Criteria.where("age").gt(5)), User.class).forEach(System.out::println);
        System.out.println("=========================================================================================");
        mongoTemplate.find(new Query(Criteria.where("name").regex(Pattern.compile("超"))), User.class).forEach(System.out::println);
        System.out.println("============================================================================================");
        mongoTemplate.find(new Query(Criteria.where("name").regex(Pattern.compile("J",Pattern.CASE_INSENSITIVE))), User.class).forEach(System.out::println);
    }
    @Test
    public void testPage(){
        int pageNum = 1;
        int pageSize = 2;
        Query query = new Query(Criteria.where("name").regex("i"));
        long count = mongoTemplate.count(query, User.class);
        List<User> users = mongoTemplate.find(query.skip((pageNum - 1) * pageSize).limit(pageSize), User.class);
        Map<String,Object> map = new HashMap<>();
        map.put("total",count);
        map.put("list",users);
        System.out.println("count = " + count);
        System.out.println("users = " + users);
    }
}





















