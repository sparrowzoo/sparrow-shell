package com.sparrow.lesson.lesson2;

import java.math.BigDecimal;

/**
 * @author zhanglz
 */

public class FloatTest {

  public static void main(String[] args) {
    // 如果第2位是奇数,则做ROUND_HALF_UP
    BigDecimal c = new BigDecimal("31.1150");
    System.out.println("c=" + c.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    // 如果第2位是偶数,则做ROUND_HALF_DOWN
    BigDecimal d = new BigDecimal("31.1250");
    System.out.println("d=" + d.setScale(2, BigDecimal.ROUND_HALF_EVEN));
  }
}
