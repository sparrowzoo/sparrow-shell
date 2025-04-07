package com.sparrow.es;

import java.util.*;

/**
 * @author: zh_harry@163.coms
 * @date: 2019/6/21 17:24
 * @description:
 */
public class getAllCategoryWeight {
    public static void main(String[] args) {
        getAllCategoryWeight(new ArrayList<String>(Arrays.asList("4444","3333","22","1")),2);
    }

    private static Map<String, Double> getAllCategoryWeight(List<String> list,int step) {
        if(step<=1){
            throw new IllegalArgumentException("step can't <1");
        }
        if(list==null||list.size()==0){
            throw new IllegalArgumentException("list can't be null");
        }
        int min = step;
        int max = list.size() * step;
        Stack<Integer> stack = new Stack<>();

        double sum = 0;
        while (min <= max) {
            stack.push(min);
            sum += min;
            min += step;
        }
        Map<String, Double> scoreMap = new HashMap<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            scoreMap.put(list.get(i), stack.pop() / sum);
        }
        return scoreMap;
    }
}
