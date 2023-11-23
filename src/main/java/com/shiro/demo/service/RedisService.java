package com.shiro.demo.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void hashSet(String key, Object field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object hashGet(String key, Object field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public boolean hashHasKey(String key, Object field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    public Long hashDel(String key, Object field) {
        return redisTemplate.opsForHash().delete(key, field);
    }

    public Long hashClear(String key) {
        return redisTemplate.opsForHash().delete(key);
    }

    public Long hashHlen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public List<Object> hashValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    public void set(String key, Object value, long millSeconds) {
        redisTemplate.opsForValue().set(key, value, millSeconds, TimeUnit.MILLISECONDS);
    }


}
