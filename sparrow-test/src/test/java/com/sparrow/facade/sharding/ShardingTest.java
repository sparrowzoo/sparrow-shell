package com.sparrow.facade.sharding;

import com.sparrow.core.algorithm.Sharding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 2018/4/13.
 */
public class ShardingTest {
    public static void main(String[] args) {
        List bucketList= Arrays.asList(1,2,3,4,5,6);
        Sharding<Integer> sharding=new Sharding<Integer>(bucketList,16);
        Map<Integer,List<Integer>> result= sharding.shardingAliquot();
        for(Integer sd:result.keySet()){
            System.out.println(sd);
            for (Integer item:result.get(sd)){
                System.out.print(item+",");
            }
            System.out.println();
        }
    }
}
