package com.sparrow.milvus;

import io.milvus.client.*;

public class ConnectionTest {
    public static void main(String[] args) {
        ConnectParam connectParam = new ConnectParam.Builder().withHost("192.168.122.1").withPort(19530).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);


        // Basic create collection:
        // We will create a collection with three fields: film duration, release_year and an
        // embedding which is essentially a float vector.
        // CollectionMapping will be used to create a collection. When adding vector fields, the
        // dimension must be specified. `auto_id` is set to false so we can provide custom ids.
        final int dimension = 8;
        final String collectionName = "demo_films";
        CollectionMapping collectionMapping = CollectionMapping
                .create(collectionName)
                .addField("duration", DataType.INT32)
                .addField("release_year", DataType.INT64)
                .addVectorField("embedding", DataType.VECTOR_FLOAT, dimension)
                .setParamsInJson("{\"segment_row_limit\": 4096, \"auto_id\": false}");

        Index index = Index
                .create(collectionName, "embedding")
                .setIndexType(IndexType.IVF_FLAT)
                .setMetricType(MetricType.L2)
                .setParamsInJson(new JsonBuilder().param("nlist", 100).build());
        //client.dropCollection(collectionName);
        client.createCollection(collectionMapping);
        client.createIndex(index);

    }
}
