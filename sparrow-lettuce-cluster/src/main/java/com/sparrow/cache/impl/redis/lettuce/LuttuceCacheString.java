package com.sparrow.cache.impl.redis.lettuce;

import com.sparrow.cache.CacheDataNotFound;
import com.sparrow.cache.CacheString;
import com.sparrow.constant.cache.KEY;
import com.sparrow.core.TypeConverter;
import com.sparrow.exception.CacheConnectionException;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

public class LuttuceCacheString extends AbstractCommand implements CacheString {
    @Override
    public String set(KEY key, Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(RedisAdvancedClusterCommands<String, String> sync) throws CacheConnectionException {
                String v = new TypeConverter(String.class).convert(value).toString();
                return sync.set(key.key(), v);
            }
        }, key);
    }

    @Override
    public String get(KEY key) throws CacheConnectionException {
        return null;
    }

    @Override
    public String get(KEY key, CacheDataNotFound<String> hook) {
        return null;
    }

    @Override
    public <T> T get(KEY key, Class clazz, CacheDataNotFound<T> hook) {
        return null;
    }

    @Override
    public <T> T get(KEY key, Class clazz) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long append(KEY key, Object value) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long decrease(KEY key) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long decrease(KEY key, Long count) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long increase(KEY key, Long count) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long increase(KEY key) throws CacheConnectionException {
        return null;
    }

    @Override
    public boolean bit(KEY key, Integer offset) throws CacheConnectionException {
        return false;
    }

    @Override
    public String setExpire(KEY key, Integer seconds, Object value) throws CacheConnectionException {
        return null;
    }

    @Override
    public Long setIfNotExist(KEY key, Object value) throws CacheConnectionException {
        return null;
    }
}
