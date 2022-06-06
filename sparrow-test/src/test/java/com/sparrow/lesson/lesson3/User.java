package com.sparrow.lesson.lesson3;

/**
 * @author zhanglz
 */

public class User {

  public User(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  String name;
  private Integer age;

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }
}
