package com.example.demoredis.dto;

public class User implements ObjectTtl {

  private Long ttl;
  private String name;
  private Integer number;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  @Override
  public Long getTtl() {
    return ttl;
  }

  @Override
  public void setTtl(Long ttl) {
    this.ttl = ttl;
  }
}
