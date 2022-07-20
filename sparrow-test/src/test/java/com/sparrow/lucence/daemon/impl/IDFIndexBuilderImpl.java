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

package com.sparrow.lucence.daemon.impl;

import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.constant.Config;
import com.sparrow.lucence.IndexManager;
import com.sparrow.utility.ConfigUtility;
import org.apache.lucene.index.IndexReader;

import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class IDFIndexBuilderImpl implements Runnable {
    private IndexManager indexManager;
    private IndexReader indexReader;

    public void setIndexManager(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    public void setIndexReader(IndexReader indexReader) {
        this.indexReader = indexReader;
    }

    static String idfIndexPath = null;
    static String idfWordPath = null;

    static {
        //可以设置为本地lucence索引
        idfIndexPath = ConfigUtility
            .getValue(Config.LUCENCE_INDEX_PATH_FOR_SEARCH);
        idfWordPath = ConfigUtility.getValue(Config.LUCENCE_IDF_KEYWORDS_PATH);
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long delay = calendar.getTimeInMillis() - currentTime;
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new SparrowThreadFactory.Builder().namingPattern("idf-index-builder-%d").daemon(true).build());

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {

                    if (indexReader == null) {
                        indexReader = indexManager.openIndexReader(idfIndexPath);
                    }
//                    TermEnum termEnum = indexReader.
//                    StringBuilder sb = new StringBuilder();
//                    int maxDocs = indexReader.maxDoc();
//                    while (termEnum.next()) {
//                        String term = termEnum.term().text();
//                        if (term.length() == 1) {
//                            continue;
//                        }
//                        // 包含term的文档数
//                        int termCount = termEnum.docFreq();
//                        //某一特定词语的IDF，可以由总文件数目除以包含该词语之文件的数目，再将得到的商取对数得到
//                        // idf(t) = 1 +log(文档总数/(包含t的文档数+1))
//                        double idf = 1 + Math.log((double) maxDocs / (termCount + 1));
//                        sb.append(term + "|" + idf);
//                        sb.append(CONSTANT.ENTER_TEXT);
//                    }
//                    FileUtility.getInstance().writeFile(idfWordPath, sb.toString());
                } catch (Exception ignore) {
                }
            }
        }, delay, 24 * 60 * 60, TimeUnit.SECONDS);
    }
}
