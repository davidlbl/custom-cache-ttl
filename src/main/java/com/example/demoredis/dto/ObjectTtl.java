package com.example.demoredis.dto;

import java.io.Serializable;

public interface ObjectTtl extends Serializable {

  Long getTtl();
  void setTtl(Long value);

}
