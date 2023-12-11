package com.shiro.demo.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LocalCache {

    private Cache<String, Object> localCache = CacheBuilder.newBuilder()
            //初始化可容纳的缓存项数目，超过后可动态扩容
            .initialCapacity(30)
            //最大可容纳缓存项数
            .maximumSize(1000)
            .concurrencyLevel(4)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public void put(String key, Object object) {
        localCache.put(key, object);
    }

    public Object get(String key) {
        return localCache.getIfPresent(key);
    }

    public void remove(String key) {
        localCache.invalidate(key);
    }
}
