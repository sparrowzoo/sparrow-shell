package com.sparrow.jdk.volatilekey;


/*
 * 使用汇编语言来验证volatile
 *
 * @author harry
 * @since 2016/12/17
 *  javac -encoding utf8 *.java
 * @params
 *
 * java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=print com.sparrow.jdk.volatilekey.Test.method
 *  java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=print com.sparrow.jdk.volatilekey.Test.test PrintOptoAssembly
 *
 *
 */
public class Test {
    private static User user = new User(100);

    public static void main(String[] args) {
        test();
        System.out.println("over");
    }

    public static void test() {
        user.setAge(user.getAge() + 1);
    }
}
