package com.sparrow.milvus;

import io.milvus.client.ConnectParam;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusGrpcClient;

public class CollectionTest {
    public static void main(String[] args) {
        ConnectParam connectParam = new ConnectParam.Builder().withHost("192.168.122.1").withPort(19530).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);
        final String collectionName = "demo_films";

        client.dropCollection(collectionName);
        //client.flush(collectionName);
    }
}
