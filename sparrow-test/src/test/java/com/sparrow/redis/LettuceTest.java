package com.sparrow.redis;


import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LettuceTest implements InitializingBean {
    @Value("${spring.redis.cluster.config.urls")
    //192.168.2.10:9000,192.168.2.14:9000,192.168.2.13:9000
    private String redisConfig;

    private RedisClusterClient clusterClient;

    public void main2() {
        /**
         * A scalable and thread-safe <a href="http://redis.io/">Redis</a> client supporting synchronous, asynchronous and reactive
         *  execution models. Multiple threads may share one connection if they avoid blocking and transactional operations such as BLPOP
         *  and MULTI/EXEC.
         */
        List<RedisURI> redisURIList = new ArrayList<>();
        redisURIList.add(RedisURI.create("redis://localhost:6379/0"));
        redisURIList.add(RedisURI.create("redis://localhost:6379/0"));
       // RedisClusterClient clusterClient = RedisClusterClient.create(redisURIList);

        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
       // System.out.println(connection.sync().set("abc", "test"));
       // System.out.println(connection.sync().get("abc"));

        StatefulRedisClusterConnection<String, String> clusterConnection = clusterClient.connect();
        RedisAdvancedClusterAsyncCommands<String, String> commands = clusterConnection.async();
        RedisAdvancedClusterCommands<String, String> sync = commands.getStatefulConnection().sync();
        Set<String> allKeys = new HashSet<>();

        KeyScanCursor<String> scanCursor = null;

        try {
            do {
                if (scanCursor == null) {
                    scanCursor = sync.scan(ScanArgs.Builder.matches("mc:smbi:*"));
                } else {
                    scanCursor = sync.scan(scanCursor, ScanArgs.Builder.matches("mc:smbi:*"));
                }
                allKeys.addAll(scanCursor.getKeys());
            } while (!scanCursor.isFinished());
        } finally {
            clusterConnection.close();
        }
        System.out.println(allKeys);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<RedisURI> redisURIList = new ArrayList<>();
        redisURIList.add(RedisURI.create("redis://localhost:6379/0"));
        redisURIList.add(RedisURI.create("redis://localhost:6379/0"));
        clusterClient = RedisClusterClient.create(redisURIList);
    }
}
