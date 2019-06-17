package com.zzq.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RequestIpLImitCache {

    private static final String PREFIX = "REQUEST_IP_LIMIT:";

    @Autowired
    private RedisTemplate redisTemplate;

    public void add(String key , String value , TimeUnit timeUnit , long t){
        redisTemplate.opsForValue().set(PREFIX + key, value, t, timeUnit);
    }

    public int count(String key ){
        return redisTemplate.keys(PREFIX + key + "*").size();
    }



}
