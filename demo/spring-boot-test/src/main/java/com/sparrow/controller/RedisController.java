package com.sparrow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("api/redis")
    public String redis() {
        return this.redisTemplate.toString();
    }
}
