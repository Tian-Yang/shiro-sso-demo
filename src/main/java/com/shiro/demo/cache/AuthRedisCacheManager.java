package com.shiro.demo.cache;

import com.shiro.demo.service.RedisService;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import javax.annotation.Resource;

/**
 * Redis缓存扩展
 * Author TianYang
 * Date 2023/11/16 11:37
 */
public class AuthRedisCacheManager extends AbstractCacheManager {

    @Resource
    private RedisService redisService;

    public AuthRedisCacheManager(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new AuthRedisCache(redisService, name);
    }


}
