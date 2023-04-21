package com.example.demoredis.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisConfiguration {

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(10)) //Default time for all cache names
        .disableCachingNullValues()
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return (builder) -> builder
        .withCacheConfiguration("user", // Set custom ttl time for user cache
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(100)));
  }

  @Bean
  public CustomCachedExpressionEvaluator cachedExpressionEvaluator() {
    return new CustomCachedExpressionEvaluator();
  }

  @Bean
  @Primary
  public CacheInterceptor customCacheInterceptor(CacheManager cacheManager, CacheOperationSource cacheOperationSource) {
    CacheInterceptor interceptor = new MyCacheInterceptor(cacheManager, cachedExpressionEvaluator());
    interceptor.setCacheManager(cacheManager);
    interceptor.setCacheOperationSources(cacheOperationSource);
    return interceptor;
  }
}
