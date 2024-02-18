package com.hanghae.lookAtMyCat.activity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "activityService", url = "http://localhost:8081/user/members")
public interface UserClient {

    // userKey로 유저 닉네임 추출
    @RequestMapping(method = RequestMethod.POST, value = "/userNick/{userKey}")
    String getUserNickByUserKey(@PathVariable("userKey") Long userKey);
}
