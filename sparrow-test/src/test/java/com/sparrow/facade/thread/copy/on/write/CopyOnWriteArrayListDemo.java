package com.sparrow.facade.thread.copy.on.write;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 循环 + add vs addAll
 */
public class CopyOnWriteArrayListDemo {

    private static final int COUNT = 100000;
    private static final List<Integer> list1 = new CopyOnWriteArrayList<>();
    private static final List<Integer> list2 = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        List<Integer> dataList = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            dataList.add(i);
        }

        testCopyOnWriteArrayList(dataList);
    }

    private static void testCopyOnWriteArrayList(List<Integer> dataList) {
        long time1 = System.currentTimeMillis();
        for (Integer data : dataList) {
            list1.add(data);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("循环+add 耗时：" + (time2 - time1) / 1000.0 + " 秒");
        list2.addAll(dataList);
        long time3 = System.currentTimeMillis();
        System.out.println("addAll  耗时：" + (time3 - time2) / 1000.0 + " 秒");
    }
}
