package com.sparrow.jdk.collections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * -Djava.util.Arrays.useLegacyMergeSort=true
 */
public class DictionaryTest {
    public static void main(String[] args) {
        //exception
        int[] array = new int[]{1, 2, 3, 2, 2, 3, 2, 3, 2, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        array = new int[]{2, 3, 2, 2, 3, 2, 3, 2, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        List<DictionaryEntry> list = new ArrayList<>();
        Set<DictionaryEntry> set = new TreeSet<>();
        for (int i = 0; i < array.length; i++) {
            DictionaryEntry entry = new DictionaryEntry();
            entry.setItemId(i);
            entry.setScore(new BigDecimal(array[i]));
            list.add(entry);
            set.add(entry);
        }
        long current = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            //System.out.println(set.size());
            Collections.sort(list);
            //System.out.println(list.size());
        }
        System.out.println(System.currentTimeMillis() - current);
    }

    public static class DictionaryEntry implements Comparable<DictionaryEntry> {
        public DictionaryEntry() {
        }

        private Integer itemId;
        private BigDecimal score;

        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            if (score != null) {
                this.score = score.setScale(5, RoundingMode.HALF_UP);
            } else {
                this.score = new BigDecimal(0);
            }
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        @Override
        public int compareTo(DictionaryEntry o) {
            //tree set o.score.compareTo(this.score)==0时去重
            //手动设置非0时,throw new IllegalArgumentException("Comparison method violates its general contract!");
            //-Djava.util.Arrays.useLegacyMergeSort=true 解决兼容
            return o.score.compareTo(this.score) == 0 ? -1 : o.score.compareTo(this.score);
        }
    }
}
