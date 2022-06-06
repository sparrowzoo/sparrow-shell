package com.sparrow.lesson.lesson3;

/**
 * @author zhanglz
 */

public class RefTypeConverter {

  public static void main(String[] args) {
    User user = new User("zhangsan", 10);
    //拓宽引用直接转 运行时也不会报错
    Object o = user;

    //窄化引用可能会报错,需要测试
    User2 user2 = (User2) o;
    System.out.println(user2);
  }
}
