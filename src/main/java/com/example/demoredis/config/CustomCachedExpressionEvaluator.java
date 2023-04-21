package com.example.demoredis.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

public class CustomCachedExpressionEvaluator extends CachedExpressionEvaluator {

  private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> keyCache = new ConcurrentHashMap(64);
  private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap(64);
  private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> unlessCache = new ConcurrentHashMap(64);

  @Nullable
  public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
    return this.getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
  }

  public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
    return Boolean.TRUE.equals(this.getExpression(this.conditionCache, methodKey, conditionExpression).getValue(evalContext, Boolean.class));
  }

  public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
    return Boolean.TRUE.equals(this.getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, Boolean.class));
  }

  void clear() {
    this.keyCache.clear();
    this.conditionCache.clear();
    this.unlessCache.clear();
  }
}
