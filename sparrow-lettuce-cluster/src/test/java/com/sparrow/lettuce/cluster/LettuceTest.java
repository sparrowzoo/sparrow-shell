package com.sparrow.lettuce.cluster;

import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

public class LettuceTest {
    public static void main(String[] args) {
        RedisClusterClient clusterClient = RedisClusterClient.create("redis://10.2.2.13:8030");
        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
        System.out.println(connection.sync().get("redis-cluster"));


    }
}
