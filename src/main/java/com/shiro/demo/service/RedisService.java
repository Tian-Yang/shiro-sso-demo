package com.shiro.demo.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
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



    public void saveObjectToRedis(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }


    public Object getObjectFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private byte[] serializeObject(Object object) {
        return new GenericJackson2JsonRedisSerializer().serialize(object);
    }


    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    private Object deserializeObject(byte[] serializedObject) {
        return new GenericJackson2JsonRedisSerializer().deserialize(serializedObject);
    }

    public boolean hasKey(String key) {
        return redisTemplate.opsForValue().getOperations().hasKey(key);
    }


}
