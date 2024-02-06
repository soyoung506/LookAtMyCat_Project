package com.hanghae.lookAtMyCat.redisTest;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Person, String> {
}
