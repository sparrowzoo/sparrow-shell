package com.sparrow.flink.controller;

import com.sparrow.flink.vo.WordCount;
import com.sparrow.vo.ChartConfigVO;
import com.sparrow.vo.DataVO;

import java.util.*;

public class WordCountController {
    public ChartConfigVO wordCount() {
        Map<String, String> workCountMap = new HashMap<>();//cacheClient.hash().getAll(key);
        List<WordCount> wordCounts = new ArrayList<>();
        for (String word : workCountMap.keySet()) {
            wordCounts.add(new WordCount(word, Integer.valueOf(workCountMap.get(word))));
        }
        Collections.sort(wordCounts, new Comparator<WordCount>() {
            @Override
            public int compare(WordCount o1, WordCount o2) {
                return -o1.getCount().compareTo(o2.getCount());
            }
        });
        ChartConfigVO chartConfig = new ChartConfigVO();
        chartConfig.setTitle("word count");
        String[] legend = new String[]{"次数"};
        String[] x = new String[wordCounts.size()];
        Integer[] data = new Integer[wordCounts.size()];
        for (int i = 0; i < wordCounts.size(); i++) {
            WordCount wordCount = wordCounts.get(i);
            String word = wordCount.getWord();
            x[i] = word;
            data[i] = wordCount.getCount();
        }
        DataVO dataConfig = new DataVO("次数", "bar", data);
        chartConfig.setLegend(legend);
        chartConfig.setX(x);
        chartConfig.setData(new DataVO[]{dataConfig});
        return chartConfig;
    }
}
