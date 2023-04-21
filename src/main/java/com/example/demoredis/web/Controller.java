package com.example.demoredis.web;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.example.demoredis.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Cacheable(value = "user", key = "#userId", unless="#result == null", condition = "#result != null")
  @GetMapping(value = "/{userId}")
  public User getUser(@PathVariable String userId) {
    LOG.info("Getting user with ID {}.", userId);
    User user = new User();
    user.setName("David");
    user.setNumber(123);
    user.setTtl(buildTllDuration());
    return null;
  }

  private long buildTllDuration(){
    LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);

    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    return zdt.toInstant().toEpochMilli();
  }
}
