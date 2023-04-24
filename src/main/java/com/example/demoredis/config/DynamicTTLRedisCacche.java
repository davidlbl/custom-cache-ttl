package com.example.demoredis.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class DynamicTTLRedisCacche extends RedisCache {

    Map<Class<?>, TTLResolver<? extends Serializable>> resolvers;
    private RedisCacheWriter cacheWriter;

    public DynamicTTLRedisCacche(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, Map<Class<?>, TTLResolver<?>> resolvers) {
        super(name, cacheWriter, cacheConfig);
        this.cacheWriter = cacheWriter;
        this.resolvers = resolvers;
    }

    @Override
    public void put(Object key, Object value) {
        if (!resolvers.containsKey(value.getClass()) {
            super.put(key, value);
        }

        Optional<Duration> dur = resolvers.get(value.getClass());

        cacheWriter.put

        Math.min(dur.get(), getCacheConfiguration().getTtl());
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return super.putIfAbsent(key, value);
    }
}
