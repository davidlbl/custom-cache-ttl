package com.example.demoredis.config;

import java.lang.reflect.Method;

import com.example.demoredis.dto.ObjectTtl;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class MyCacheInterceptor extends CacheInterceptor {

 private final CacheManager cacheManager;
  private CustomCachedExpressionEvaluator evaluator;

  public MyCacheInterceptor(CacheManager cacheManager, CustomCachedExpressionEvaluator evaluator) {
    this.cacheManager = cacheManager;
    this.evaluator = evaluator;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {

    Method method = invocation.getMethod();

    Cacheable annotation = method.getAnnotation(Cacheable.class);
    String cacheName = annotation.value()[0];
    String cacheManagerName = annotation.cacheManager();

    Object cacheKey = getKeyValueFromParameter(annotation.key(), invocation);
    CacheManager cacheManager = getCacheManagerByName(cacheManagerName);
    Cache cache = cacheManager.getCache(cacheName);

    ObjectTtl result = cache.get(cacheKey, ObjectTtl.class);

    //Comparing object ttl with time system
    long currentTimeMillis = System.currentTimeMillis();
    if(result == null || result.getTtl().compareTo(currentTimeMillis) < 0) {
      Object proceed = invocation.proceed();
      cache.put(cacheKey, proceed);
      return proceed;
    }
    return result;
  }

  private CacheManager getCacheManagerByName(String cacheManagerName) {
    if ("".equals(cacheManagerName))
      return cacheManager;
    return getBean(cacheManagerName, CacheManager.class);
  }

  private Object getKeyValueFromParameter(String key, MethodInvocation invocation) {

    AnnotatedElementKey annotatedElementKey = buildAnnotatedElementKey(invocation);

    EvaluationContext context = createEvaluatorContext(invocation.getMethod(), invocation.getArguments());

    Object keyValue = evaluator.key(key, annotatedElementKey, context);

    if(keyValue == null) {
      throw new RuntimeException("Key name doesn't exist in param names");
    }
    return keyValue;
  }

  private EvaluationContext createEvaluatorContext(Method method, Object[] arguments) {
    //Related param names with their values
    EvaluationContext context = new StandardEvaluationContext();
    for (int i = 0; i < arguments.length; i++) {
      String argumentName = method.getParameters()[0].getName();
      Object argumentValue = arguments[i];
      context.setVariable(argumentName, argumentValue);
    }
    return context;
  }

  private AnnotatedElementKey buildAnnotatedElementKey(MethodInvocation invocation) {
    Method method = invocation.getMethod();
    return new AnnotatedElementKey(method, invocation.getThis().getClass());
  }
}
