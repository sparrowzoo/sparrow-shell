package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.impl.SparrowContainer;

/**
 * @author by harry
 */
public class SparrowContainerTest {

  public static void main(String[] args) throws Exception {
    Container container = new SparrowContainer();
    ContainerBuilder builder=new ContainerBuilder().contextConfigLocation("/sparrow_application_context.xml");
    container.init(builder);

    User3 user = new User3("zhangsan");
    ((SparrowContainer) container).initProxyBean(User3.class);
    MethodAccessor methodAccessor = container.getProxyBean(User3.class);
    System.out.println(methodAccessor.get(user, "user"));
//
//    Initializer initializer = container.getBean("initializer");
//    System.out.println(initializer);
//
//    ErrorSupport errorSupport = SparrowError.ACTIVITY_RULE_GIFT_TIMES_OUT;
//    System.out.println(errorSupport.module());
//    System.out.println(errorSupport.getCode());
  }
}
