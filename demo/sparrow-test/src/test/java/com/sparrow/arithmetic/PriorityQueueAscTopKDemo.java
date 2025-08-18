package com.sparrow.arithmetic;

import java.util.*;

public class PriorityQueueAscTopKDemo {
    //找出前k个最大数，采用小顶堆实现
    public static int[] findKMax(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer left, Integer right) {
                return right.compareTo(left);
            }
        });
        //队列默认自然顺序排列，小顶堆，不必重写compare
        for (int num : nums) {
            if (pq.size() < k) {
                pq.add(num);
            } else {
                if (pq.peek() > num) {//如果堆顶元素 < 新数，则删除堆顶，加入新数入堆
                    pq.poll();
                    pq.add(num);
                } else {
                    System.out.println("skip" + num);
                }
            }
        }

        int[] result = new int[k];
        int i = 0;
        //真正的排序过程
        while (!pq.isEmpty()) {
            result[i++] = pq.poll();
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 7, 6, 1};
        System.out.println(Arrays.toString(findKMax(arr, 5)));
        Map<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 10);
        treeMap.put(3, 20);
        treeMap.put(2, 5);
        System.out.println(treeMap.keySet());
        System.out.println(treeMap.values());
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        System.out.println(list);
    }
}
