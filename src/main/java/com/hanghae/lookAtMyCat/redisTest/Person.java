package com.hanghae.lookAtMyCat.redisTest;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("person")
@Data
public class Person {
    @Id
    String id;
    String name;
    int age;

    List<String> tag;
}
