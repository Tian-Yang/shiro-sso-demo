package com.shiro.demo.cache;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 自定义Redis缓存
 *
 * @param <K>
 * @param <V>
 */
@Slf4j
public class AuthRedisCache<K, V> implements Cache<K, V> {

    private String cacheNameKey;
    private RedisService redisService;


    public AuthRedisCache(RedisService redisService, String cacheName) {
        log.info("AuthRedisCache new(),cacheName:{}", cacheName);
        this.cacheNameKey = cacheName;
        this.redisService = redisService;
    }


    /**
     * key是
     *
     * @param k
     * @return
     * @throws CacheException
     */
    @Override
    public V get(K k) throws CacheException {
        log.info("AuthRedisCache Get,k:{}", k);
        V value = getAuthenticationInfoFromRedis(k);
        log.info("AuthenticateRedisCache Get,k:{},v:{}", k, JSON.toJSONString(value));
        return value;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("AuthRedisCache put,k:{},v:{}", k, v);
        String key = (String) k;
        redisService.saveObjectToRedis(cacheNameKey + "_" + key, v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        log.info("AuthRedisCache remove,k:{}", k);
        V authenticationInfo = getAuthenticationInfoFromRedis(k);
        String key = (String) k;
        if (null != authenticationInfo) {
            redisService.del(cacheNameKey + "_" + key);
        }
        return authenticationInfo;
    }

    @Override
    public void clear() throws CacheException {
        log.info("AuthRedisCache clear");
        //redisService.hashClear(cacheNameKey);
    }

    /**
     * 无需扩展（Shiro默认的缓存是基于Map的，所以会存在这个方法。而扩展成redis后无需扩展这个方法）。
     *
     * @return int
     * @Author TianYang
     * @Date 2023/12/10 23:24
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * 无扩扩展
     *
     * @return java.util.Set<K>
     * @Author TianYang
     * @Date 2023/12/10 23:25
     */
    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }


    private V getAuthenticationInfoFromRedis(K k) {
        V authenticationInfo = null;
        String key = (String) k;
        authenticationInfo = (V) redisService.getObjectFromRedis(cacheNameKey + "_" + key);
        return authenticationInfo;
    }
}
