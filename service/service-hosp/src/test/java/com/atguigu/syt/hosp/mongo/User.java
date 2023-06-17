package com.atguigu.syt.hosp.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.mongo
 * class:User
 *
 * @author: smile
 * @create: 2023/6/2-18:33
 * @Version: v1.0
 * @Description:
 */
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    ObjectId id;
    @Field(value = "username")
    String name;
    String email;
    Integer age;
    Boolean gender;
}
