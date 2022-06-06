package com.sparrow.milvus;

import io.milvus.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

public class VectorTest {
    static final String collectionName = "demo_films";

    // Here we create a partition called "American"

    private static List<Integer> randomDurations(int skuCount) {
        List<Integer> randomDurations = new ArrayList<>();
        for (int i = 0; i < skuCount; i++) {
            randomDurations.add(i);
        }
        return randomDurations;
    }

    private static List<List<Float>> randomFloatVectors(int skuCount) {
        SplittableRandom splitCollectionRandom = new SplittableRandom();
        List<List<Float>> vectors = new ArrayList<>(skuCount);
        for (int i = 0; i < skuCount; ++i) {
            splitCollectionRandom = splitCollectionRandom.split();
            DoubleStream doubleStream = splitCollectionRandom.doubles(8);
            List<Float> vector =
                    doubleStream.boxed().map(Double::floatValue).collect(Collectors.toList());
            vectors.add(vector);
        }
        return vectors;
    }

    public static void main(String[] args) {

        ConnectParam connectParam = new ConnectParam.Builder().withHost("192.168.122.1").withPort(19530).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);
        // because the films to insert are American films.
        for (int i = 0; i < 1; i++) {
            initPartition(client, "a" + i);
        }
    }

    private static void initPartition(MilvusClient client, String partitionTag) {
        int skuCount = 200000;
        //Insert three films with their IDs, duration, release year, and fake embeddings into the partition "American".
        List<Long> ids = LongStream.range(0, skuCount).boxed().collect(Collectors.toList());
        List<Integer> durations = randomDurations(skuCount); /* A list of 1,000 Integers. */
        List<Long> releaseYears = LongStream.range(0, skuCount).boxed().collect(Collectors.toList());
        List<List<Float>> embeddings = randomFloatVectors(skuCount);

        if (client.hasPartition(collectionName, partitionTag)) {
            client.dropPartition(collectionName, partitionTag);
        }
        client.createPartition(collectionName, partitionTag);
        InsertParam insertParam = InsertParam
                .create(collectionName)
                .addField("duration", DataType.INT32, durations)
                .addField("release_year", DataType.INT64, releaseYears)
                .addVectorField("embedding", DataType.VECTOR_FLOAT, embeddings)
                .setEntityIds(ids)
                .setPartitionTag(partitionTag);
        client.insert(insertParam);
    }
}
