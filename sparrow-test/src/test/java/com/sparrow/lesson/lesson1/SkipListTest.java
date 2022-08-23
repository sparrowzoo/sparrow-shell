package com.sparrow.lesson.lesson1;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author zhanglz
 */

public class SkipListTest {

  public static void main(String[] args) {
    nodeDistribution(100000);
  }

  private static void nodeDistribution(int nodeCount) {
    Map<Integer, Integer> map = new TreeMap<>();
    for (int i = 0; i < nodeCount; i++) {
      Integer level = randomLevel(0.5F);
      if (map.containsKey(level)) {
        map.put(level, map.get(level) + 1);
      } else {
        map.put(level, 1);
      }
    }
    for (Integer key : map.keySet()) {
      BigDecimal bigDecimal = BigDecimal.valueOf(map.get(key) / (double) nodeCount);
      bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP);
      System.out.println(
              "level:" + key + ",count:" + map.get(key) + ",p:"
                  + bigDecimal);
    }
  }

  private static int randomLevel(float p) {
    int level = 1;
    int r;
    int bound = 0xFFFF;
    Random random = new Random();
    while (true) {
      r = random.nextInt(bound);
      if (r < bound * p) {
        break;
      }
      level += 1;
    }
    return level;
  }
}