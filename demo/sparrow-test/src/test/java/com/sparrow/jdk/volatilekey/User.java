package com.sparrow.jdk.volatilekey;

/**
 * Created by TCLDUSER on 2018/4/9.
 */
public class User {
    private Integer age;
    private byte[] bytes;

    public User(Integer age) {
        this.age = age;
    }

    public User(Integer age, byte[] bytes) {
        this.age = age;
        this.bytes=bytes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
