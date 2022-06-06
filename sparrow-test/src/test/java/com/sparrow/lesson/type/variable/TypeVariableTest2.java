package com.sparrow.lesson.type.variable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhanglz
 */

public class TypeVariableTest2 {

  public static void main(String[] args) {
    Collection<String> strList = new ArrayList<>();
    strList.add("1");
    strList.add("2");
    printCollection(strList);

    Collection<Integer> intList = new ArrayList<>();
    intList.add(1);
    intList.add(2);
    printCollection(intList);
  }

  static void printCollection(Collection<?> collection) {
    for (Object o : collection) {
      System.out.println(o);
    }
  }
}
