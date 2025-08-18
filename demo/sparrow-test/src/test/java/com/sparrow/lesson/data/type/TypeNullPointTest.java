package com.sparrow.lesson.data.type;

/**
 * @author zhanglz
 */

public class TypeNullPointTest {

  public static void main(String[] args) {
    Integer i;//int i 则直接报空指针异常
    i = getFromDb();
    if (i == null) {
      System.out.println("null point exception");
    }
  }

  /**
   * 数据可能来源于数据库
   *
   * 文件系统 远程配置文件
   *
   * RPC
   *
   * dubbo
   */
  private static Integer getFromDb() {
    return null;
  }
}
