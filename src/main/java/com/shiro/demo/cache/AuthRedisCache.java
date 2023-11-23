package com.shiro.demo.cache;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.util.SerializationUtils;

import java.util.*;

/**
 * 自定义Redis缓存
 *
 * @param <K>
 * @param <V>
 */
@Slf4j
public class AuthRedisCache<K, V> implements Cache<K, V> {

    private String cacheName;
    private String cacheNameKey;
    private RedisService redisService;


    public AuthRedisCache(RedisService redisService, String cacheName) {
        log.info("AuthRedisCache new(),cacheName:{}", cacheName);
        this.cacheName = cacheName;
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
        byte[] valueBytes = SerializationUtils.serialize(v);
        redisService.hashSet(cacheNameKey, Base64.getEncoder().encodeToString(k.toString().getBytes()), valueBytes);
        return v;
    }

    @Override
    public V remove(Object k) throws CacheException {
        log.info("AuthRedisCache remove,k:{}", k);
        V authenticationInfo = getAuthenticationInfoFromRedis(k);
        String key = Base64.getEncoder().encodeToString(k.toString().getBytes());
        if (null != authenticationInfo) {
            redisService.hashDel(cacheNameKey, key);
        }
        return authenticationInfo;
    }

    @Override
    public void clear() throws CacheException {
        log.info("AuthRedisCache clear");
        redisService.hashClear(cacheNameKey);
    }

    @Override
    public int size() {
        return redisService.hashHlen(cacheNameKey).intValue();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) redisService.hashKeys(cacheNameKey);
    }

    @Override
    public Collection<V> values() {
        List<Object> valueList = redisService.hashValues(cacheNameKey);
        List<V> authenticationInfoList = new ArrayList<>();
        for (Object ob : valueList) {
            byte[] valueBytes = (byte[]) ob;
            V authenticationInfo = (V) SerializationUtils.deserialize(valueBytes);
            authenticationInfoList.add(authenticationInfo);
        }
        return authenticationInfoList;
    }

    private V getAuthenticationInfoFromRedis(Object k) {
        V authenticationInfo = null;
        String key = Base64.getEncoder().encodeToString(k.toString().getBytes());
        if (redisService.hashHasKey(cacheNameKey, key)) {
            byte[] valueBytes = (byte[]) redisService.hashGet(cacheNameKey, key);
            authenticationInfo = (V) SerializationUtils.deserialize(valueBytes);
        }
        return authenticationInfo;
    }
}
