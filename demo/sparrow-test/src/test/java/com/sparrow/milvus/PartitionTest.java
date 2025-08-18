package com.sparrow.milvus;

import io.milvus.client.ConnectParam;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusGrpcClient;

public class PartitionTest {
    public static void main(String[] args) {
        ConnectParam connectParam = new ConnectParam.Builder().withHost("192.168.122.1").withPort(19530).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);
        final String collectionName = "demo_films";

        // Here we create a partition called "American"
        // because the films to insert are American films.
        final String partitionTag = "a0";
        System.out.println(client.hasPartition(collectionName,partitionTag));
        System.out.println(client.countEntities(collectionName));
        //client.createPartition(collectionName, partitionTag);
        //client.dropPartition(collectionName, partitionTag);
    }
}
