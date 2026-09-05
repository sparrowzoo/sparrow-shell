package com.sparrow.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Top100 {
    public static void main(String[] args) {
        List<Integer> nums=new ArrayList<>();
        for(int i=1;i<=10;i++){
            nums.add(100+new Random().nextInt(100));
        }
        nums.sort(Collections.reverseOrder());
        int num=30;
        for(int i=11;i<num;i++) {
            nums.add((int) (nums.get(nums.size() - 1)-(num-i)*0.7));
        }
        System.out.println(nums);
    }

    public static int total(List<Integer> nums){
        int sum=0;
        for(int i=0;i<nums.size();i++){
            sum+=nums.get(i);
        }
        return sum;
    }
}
