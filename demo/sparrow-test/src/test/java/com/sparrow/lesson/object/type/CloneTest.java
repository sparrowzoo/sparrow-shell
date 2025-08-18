package com.sparrow.lesson.object.type;

/**
 * @author zhanglz
 */

public class CloneTest {

  public static void main(String[] args) throws CloneNotSupportedException {
    Dog dog = new Dog("hi");
    Dog cloneDog = dog.clone();
    System.out.println(dog == cloneDog);
    System.out.println(dog.getName() == cloneDog.getName());

    User user = new User("zhangsan", dog);
    User cloneUser = user.clone();
    System.out.println(user == cloneUser);
    System.out.println(user.getDog() == cloneUser.getDog());
  }
}

class User implements Cloneable {

  private String name;
  private Dog dog;

  public User(String name, Dog dog) {
    this.name = name;
    this.dog = dog;
  }

  public String getName() {
    return name;
  }

  public Dog getDog() {
    return dog;
  }

  @Override
  protected User clone() throws CloneNotSupportedException {
    User user = (User) super.clone();
    user.dog = user.dog.clone();
    return user;
  }
}

class Dog implements Cloneable {

  public Dog(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

  @Override
  protected Dog clone() throws CloneNotSupportedException {
    return (Dog) super.clone();
  }
}
