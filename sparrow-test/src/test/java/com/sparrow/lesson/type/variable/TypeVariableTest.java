package com.sparrow.lesson.type.variable;

/**
 * @author zhanglz
 */

public class TypeVariableTest {

  public static void main(String[] args) {
    C2 c = new C2();
    test(c);
  }

  static <B1 extends C> void test(B1 t) {
    t.print();
  }
}

class C2 extends C {

  @Override
  public void print() {
    System.out.println("helli world 2");
  }
}

class C {

  public void print() {
    System.out.println("helli world");
  }
}
