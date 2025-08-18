package com.sparrow.milvus;

import io.milvus.client.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchTest {
    //https://milvus.io/tools/sizing
    public static void main(String[] args) {
        // Basic hybrid search:
        // Let's say we have a film with its `embedding` and we want to find `top1` film that is
        // most similar to it by L2 metric_type (Euclidean Distance).
        // In addition to vector similarities, we also want to filter films such that:
        // - `term` is 1, 2, or 5,
        // - `duration` larger than 250 minutes.
        List<List<Float>> queryEmbedding = new ArrayList<>()/* your query vectors */;
        queryEmbedding.add(Arrays.asList(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F));
        //queryEmbedding.add(Arrays.asList(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F));

        final long topK = 100;
        String dsl = String.format("{\n" +
                "    \"bool\":{\n" +
                "        \"must\":[\n" +
                "            {\n" +
                "                \"vector\":{\n" +
                "                    \"embedding\":{\n" +
                "                        \"topk\":%d,\n" +
                "                        \"params\":{\"nprobe\":%d},\n" +
                "                        \"metric_type\":\"L2\",\n" +
                "                        \"type\":\"float\",\n" +
                "                        \"query\":%s}\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}", topK, 2, queryEmbedding.toString());

        ConnectParam connectParam = new ConnectParam.Builder().withHost("192.168.122.1").withPort(19530).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);

        final String collectionName = "demo_films";
        List<String> partitionTagList = new ArrayList<>();
        partitionTagList.add("a0");

        SearchParam searchParam = SearchParam
                .create(collectionName)
                .setDsl(dsl)
                .setPartitionTags(partitionTagList);
        //.setParamsInJson("{\"fields\": [\"B\"]}");
        for (int i = 0; i < 100; i++) {
            long t = System.currentTimeMillis();
            SearchResult searchResult = client.search(searchParam);
            System.out.println((System.currentTimeMillis() - t) + "ms");
        }
        //System.out.println(searchResult.getResultDistancesList());
    }
}
