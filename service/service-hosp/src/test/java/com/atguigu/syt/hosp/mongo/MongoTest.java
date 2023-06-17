package com.atguigu.syt.hosp.mongo;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:MongoTest
 *
 * @author: smile
 * @create: 2023/6/2-15:41
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class  MongoTest {
    @Resource
    private UserRepository userRepository;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 使用MongoRepository操作MongoDB的步骤
     * 导入依赖 spring-data 整合 MongoDB
     * yml文件配置mongo的五个连接信息
     * 创建pojo类 @Document @Id @filed
     * 自定义持久化层接口继承MongoRepository<User,ObjectId>
     * 在测试类或者业务逻辑层注入自定义持久化层接口代理类对象 调用现成的增删改查方法
     */
    @Test
    public void test(){
        List<User1> users = new ArrayList<>();
        ArrayList<User1> users1 = new ArrayList<>();
        User1 user = new User1(null, "王超", 18, "wang@gmail.com", new Date());
        for (int i = 0; i < 20; i++) {
           users.add(new User1(null, "王超"+i, i, "wang@gmail.com", new Date())) ;
           users.add(new User1(null, "王超"+i, i, "wang@gmail.com", new Date())) ;
        }
        userRepository.saveAll(users);
        userRepository.saveAll(users1);
    }
    @Test
    public void testDelete(){
        User1 user = new User1(new ObjectId("647a9b20525b2d46d0a7e054"), "", 45, "", new Date());

        userRepository.delete(user);
    }
    @Test
    public void testUpdate(){
        Optional<User1> op = userRepository.findById(new ObjectId("647a9b20525b2d46d0a7e056"));
        if (op.isPresent()){
            User1 user = op.get();
            user.setName("77777777777777");
            userRepository.save(user);
        }
    }
    @Test
    public void testPage(){
        List<User1> age = userRepository.findAll(Sort.by("age").descending());
        for (User1 user : age) {
            System.out.println(user);
        }
    }
    @Test
    public void testQuery(){
        PageRequest page = PageRequest.of(0, 3);
        User1 user = new User1();
        user.setName("王");
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        //ExampleMatcher.matching().withMatcher("age",ExampleMatcher.GenericPropertyMatchers.);
        Example<User1> example = Example.of(user,exampleMatcher);
        Page<User1> all = userRepository.findAll(example, page);
        System.out.println("总记录数"+all.getTotalElements());
        System.out.println("总页数"+all.getTotalPages());
        System.out.println("当前页列表数据"+all.getContent());

    }
    @Test
    public void testSpringData(){
        List<User1> jerry = userRepository.findByName("jerry");
        System.out.println("jerry = " + jerry);
        userRepository.findByNameLike("超").stream().sorted(new Comparator<User1>() {
            @Override
            public int compare(User1 o1, User1 o2) {
                return o1.getAge()-o2.getAge();
            }
        }).forEach(new Consumer<User1>() {
            @Override
            public void accept(User1 user) {
                System.out.println(user);
            }
        });

    }
}

