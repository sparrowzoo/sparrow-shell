package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.impl.SparrowContainer;

import java.lang.reflect.Method;

/**
 * @author by harry
 */
public class SparrowContainerTest2 {

  public static void main(String[] args) throws Exception {
    Container container = new SparrowContainer();
    container.init();

    User3 user = new User3("zhangsan");
    ((SparrowContainer) container).initProxyBean(User3.class);
    MethodAccessor methodAccessor = container.getProxyBean(User3.class);
    long t = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      methodAccessor.get(user, "user");
    }
    System.out.println(System.currentTimeMillis() - t);

    t = System.currentTimeMillis();
    Method method = User3.class.getMethod("getUser");
    for (int i = 0; i < 10000000; i++) {
      method.invoke(user);
    }
    System.out.println(System.currentTimeMillis() - t);
  }
}
