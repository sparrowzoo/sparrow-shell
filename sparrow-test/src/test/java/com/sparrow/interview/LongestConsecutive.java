package com.sparrow.interview;

import java.util.Arrays;

/**
 * @author zhanglz
 */

public class LongestConsecutive {

  public static void main(String[] args) {
    int[] array = new int[]{1};
    System.out.println(longestConsecutive(array));
  }

  private static int longestConsecutive(int[] array) {
    if (array == null || array.length == 0) {
      return 0;
    }
    Arrays.sort(array);
    int longestConsecutive = 1;
    int maxLongestConsecutive = 1;
    for (int i = 1; i < array.length; i++) {
      if (array[i] == array[i - 1]) {
        continue;
      }
      if (array[i] == array[i - 1] + 1) {
        longestConsecutive++;
        maxLongestConsecutive = Math.max(longestConsecutive, maxLongestConsecutive);
        continue;
      }
      longestConsecutive = 1;
    }
    return Math.max(longestConsecutive, maxLongestConsecutive);
  }
}
