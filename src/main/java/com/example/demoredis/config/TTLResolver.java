package com.example.demoredis.config;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

public interface TTLResolver<T> extends Function<T, Duration> {

    Optional<Duration> resolveTTL(T object);

}
