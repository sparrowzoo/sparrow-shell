package com.sparrow.interview;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author zhanglz
 */

public class RandomTest {

  private static class User {

    private String name;
    private Double random;

    public String getName() {
      return name;
    }
    

    public User(String name, Double random) {
      this.name = name;
      this.random = random;
    }
  }

  public static void main(String[] args) {
    long t = System.currentTimeMillis();
    int userCount = 10000;
    int userCountOfTerm = 5;
    User users[] = new User[userCount];
    List<User[]> terms = new ArrayList<>();

    for (int i = 0; i < userCount; i++) {
      User user = new User("u+" + i, new Random().nextDouble());
      users[i] = user;
    }

    Arrays.sort(users, new Comparator<User>() {
      @Override
      public int compare(User o1, User o2) {
        return o1.random.compareTo(o2.random);
      }
    });

    User[] usersOfTerm = null;
    int indexOfTerm = 0;
    for (int i = 0; i < users.length; i++) {
      if (i % userCountOfTerm == 0) {
        usersOfTerm = new User[userCountOfTerm];
        indexOfTerm = 0;
        terms.add(usersOfTerm);
      }
      usersOfTerm[indexOfTerm++] = users[i];
    }

    System.out.println(JSON.toJSONString(terms));
    System.out.println(System.currentTimeMillis() - t);
  }
}
