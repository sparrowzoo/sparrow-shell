package com.sparrow.jdk.sort;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhanglizhi@kanzhun.com
 * @date: 2019-03-03 11:31
 * @description:
 */
public class SortTest {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        Integer size = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
//           Thread t= new Thread(new Runnable() {
//                @Override
//                public void run() {

                        Long mark = System.nanoTime();


                        Map<Integer, Long> map = new ConcurrentHashMap<>();
                        for (int i = 0; i < size; i++) {
                            //map.put(new Random().nextInt(size), Long.MAX_VALUE);
                            map.put(Integer.MAX_VALUE - i, Long.MAX_VALUE - i);
                        }


                        long mapDuraton = System.nanoTime() - mark;

                        List<Map.Entry<Integer, Long>> tpEntryList = new ArrayList<>(map.entrySet());
                        mark = System.nanoTime();
                        tpEntryList.sort(Comparator.comparing(Map.Entry::getKey));
                        long sortDuration = System.nanoTime() - mark;


                        mark = System.nanoTime();
                        ConcurrentSkipListMap<Integer, Long> treeMap = new ConcurrentSkipListMap<>();
                        for (int i = 0; i < size; i++) {
                            treeMap.put(Integer.MAX_VALUE - i, Long.MAX_VALUE - i);
                            // treeMap.put(new Random().nextInt(size), Long.MAX_VALUE);
                        }

                        long treeMapDuration = System.nanoTime() - mark;
                        if (mapDuraton > 0 && treeMapDuration > 0) {
                            atomicInteger.incrementAndGet();
                        }
                        System.out.println(String.format(
                                "map size=%s,duration=%8s,sort duration=%8s,map+sort duration=%8s,tree map size=%s,duration=%8s,better map=%s",
                                map.size(), mapDuraton, sortDuration, mapDuraton + sortDuration, treeMap.size(), treeMapDuration, (double) treeMapDuration / (mapDuraton + sortDuration)));

                        // map=null;
                        // map1=null;


                    countDownLatch.countDown();
              //  }
            //});
//           t.setPriority(1);
//           t.start();
        }
        countDownLatch.await();
        System.out.println(atomicInteger.get());

    }
}
