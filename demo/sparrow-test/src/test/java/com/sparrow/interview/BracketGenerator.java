package com.sparrow.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglz
 */

public class BracketGenerator {

  public static void main(String[] args) {
    System.out.println(generateParenthesis(2));
  }

  public static List<String> generateParenthesis(int n) {
    List<String> result = new ArrayList<>();
    dfs(result, n, "", 0, 0);
    return result;
  }

  /**
   * 回溯的本质=dfs+状态重置
   */
  private static void dfs(List<String> result, int n, String sb, int left, int right) {
    System.out.println(sb);

    //左一定会小于右
    if (left > n || right > n || left < right) {
      System.out.println("-----");
      return;
    }
    //如果长度满足则一定是满足的
    if (sb.length() == 2 * n) {
      result.add(sb);
      System.out.println();
      return;
    }
    //加"(" 并将left++
    dfs(result, n, sb + "(", ++left, right);
    //加")"并将right++
    left--;
    dfs(result, n, sb + ")", left, ++right);
  }
}
