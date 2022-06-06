package com.sparrow.container;

import com.sparrow.protocol.POJO;

/**
 * @author zhanglz
 */

public class User3 implements POJO {

  private String user;

  public User3(String user) {
    this.user = user;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
