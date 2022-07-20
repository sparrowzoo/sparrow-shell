/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.similarity;

import com.sparrow.lucence.KeyAnalyzer;
import com.sparrow.lucence.LexemeWithBoost;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CosineSimilarity {
    protected static Logger logger = LoggerFactory.getLogger(CosineSimilarity.class);
    private KeyAnalyzer analyzer;

    public void setAnalyzer(KeyAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public double getSimilarity(String text1, String text2) {
        //""==""
        if (StringUtility.isNullOrEmpty(text1) && StringUtility.isNullOrEmpty(text2)) {
            return 1.0;
        }
        //"" vs "text"
        if (StringUtility.isNullOrEmpty(text1) || StringUtility.isNullOrEmpty(text2)) {
            return 0.0;
        }
        //"a"=="a"
        if (text1.equalsIgnoreCase(text2)) {
            return 1.0;
        }
        //第一步：进行分词
        List<LexemeWithBoost> word1Segments = analyzer.getKeyList(text1);
        List<LexemeWithBoost> word2Segments = analyzer.getKeyList(text2);
        return getSimilarity(word1Segments, word2Segments);
    }

    /**
     * 2、对于计算出的相似度保留小数点后六位
     */
    public static double getSimilarity(List<LexemeWithBoost> words1, List<LexemeWithBoost> words2) {
        double score = getSimilarityImpl(words1, words2);
        score = (int) (score * 1000000 + 0.5) / (double) 1000000;
        return score;
    }

    public static double getSimilarityImpl(List<LexemeWithBoost> words1, List<LexemeWithBoost> words2) {
        Map<String, AtomicInteger> frequency1 = getFrequency(words1);
        Map<String, AtomicInteger> frequency2 = getFrequency(words2);
        Map<String, AtomicInteger> allWords = new HashMap<>(frequency1.size() + frequency2.size());
        allWords.putAll(frequency1);
        allWords.putAll(frequency2);
        int ab = 0;// a.b =x1x2+y1y2
        int aa = 0;// |a| square
        int bb = 0;// |b| square

        for (String word : allWords.keySet()) {
            AtomicInteger atomicX1 = frequency1.get(word);
            AtomicInteger atomicX2 = frequency2.get(word);
            int x1 = 0;
            int x2 = 0;
            if (atomicX1 != null) {
                x1 = atomicX1.get();
            }
            if (atomicX2 != null) {
                x2 = atomicX2.get();
            }
            if (x1 > 0 && x2 > 0) {
                ab += x1 * x2;
            }
            if (x1 > 0) {
                aa += x1 * x1;
            }
            if (x2 > 0) {
                bb += x2 * x2;
            }
        }

        double sqrta = Math.sqrt(aa);//sqrt a
        double sqrtb = Math.sqrt(bb);//sqrt b
        BigDecimal aabb = BigDecimal.valueOf(sqrta).multiply(BigDecimal.valueOf(sqrtb));
        //similarity=a.b/|a|*|b|
        return BigDecimal.valueOf(ab).divide(aabb, 9, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * getFrequency
     *
     * @return
     */
    private static Map<String, AtomicInteger> getFrequency(List<LexemeWithBoost> words) {
        Map<String, AtomicInteger> freq = new HashMap<>();
        for (LexemeWithBoost lexeme : words) {
            if (!freq.containsKey(lexeme.getLexemeText())) {
                freq.put(lexeme.getLexemeText(), new AtomicInteger());
            }
            freq.get(lexeme.getLexemeText()).incrementAndGet();
        }
        return freq;
    }
}