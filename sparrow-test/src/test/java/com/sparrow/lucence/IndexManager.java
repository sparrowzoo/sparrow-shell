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

package com.sparrow.lucence;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.search.IndexSearcher;

public class IndexManager {
    /**
     * 创建一个分词器 Analyzer analyzer = new IKAnalyzer(true);
     */
    private Analyzer analyzer;

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * 注意点 在window系统中我们通常使用simpleFSDirectory，
     * <p>
     * 而其他操作系统则使用NIOFSDirectory
     * <p>
     * NIOFSDirectory uses java.nio's FileChannel's positional io when reading to avoid synchronization when reading
     * from the same file. Unfortunately, due to a Windows-only Sun JRE bug this is a poor choice for Windows, but on
     * all other platforms this is the preferred choice.
     *
     * @param path
     * @return
     * @throws Exception
     */
    public IndexWriter initIndexWriter(String path) throws Exception {
        // 在当前路径下创建一个叫indexDir的目录
        File indexDir = new File(path);
        if (!indexDir.exists()) {
            indexDir.mkdir();
        }

        //Directory directory = FSDirectory.open(indexDir);
        // 如果被锁定则解锁
//        if (IndexWriter.isLocked(directory)) {
//            IndexWriter.unlock(directory);
//        }
//
//        // 创建索引配置器
//        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
//                Version.LUCENE_36, analyzer);
        LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
        // 设置segment添加文档(Document)时的合并频率
        // 值较小,建立索引的速度就较慢
        // 值较大,建立索引的速度就较快,>10适合批量建立索引
        mergePolicy.setMergeFactor(50);
        // 设置segment最大合并文档(Document)数
        // 值较小有利于追加索引的速度
        // 值较大,适合批量建立索引和更快的搜索
        mergePolicy.setMaxMergeDocs(5000);
        // 启用复合式索引文件格式,合并多个segment

        //todo check
        //mergePolicy.setUseCompoundFile(true);
//        indexWriterConfig.setMergePolicy(mergePolicy);
//        // 设置索引的打开模式
//        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
//        // 创建索引器
//        return new IndexWriter(directory, indexWriterConfig);
        return null;
    }

    public IndexReader openIndexReader(String indexPath) throws IOException {
        // 打开索引目录
//        File indexDir = new File(indexPath);
//        Directory directory = FSDirectory.open(indexDir);
//        // 获取访问索引的接口,进行搜索
//        return IndexReader.open(directory);
        return null;
    }

    public void closeIndexReader(IndexReader indexReader) {
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException e) {
            }
        }
    }

    public void closeIndexSearch(IndexSearcher indexSearcher) {
//        if (indexSearcher != null) {
//            try {
//                indexSearcher.close();
//            } catch (IOException ignore) {
//            }
//        }
    }

    public void closeIndexWriter(IndexWriter indexWriter) throws Exception {
        // 提交索引到磁盘上的索引库,关闭索引器
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
}
