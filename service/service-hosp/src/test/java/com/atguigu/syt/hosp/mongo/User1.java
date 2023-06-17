package com.atguigu.syt.hosp.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "user1")
public class User1 {

    @Id
    private ObjectId id;

    private String name;
    private Integer age;
    private String email;
    private Date createDate;
}