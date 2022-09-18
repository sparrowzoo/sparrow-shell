package com.sparrow.cache.impl.redis.lettuce;

import com.sparrow.constant.cache.KEY;
import com.sparrow.core.TypeConverter;
import com.sparrow.exception.CacheConnectionException;
import com.sparrow.protocol.POJO;
import com.sparrow.utility.StringUtility;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

public class LuttuceCacheString extends AbstractCommand implements CacheString {
    public LuttuceCacheString(RedisPool pool) {
        this.redisPool = pool;
    }

    @Override
    public String set(final KEY key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(RedisAdvancedClusterCommands<byte[], byte[]> connection) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                String v = typeConverter.convert(value).toString();
                return connection.set(key.key().getBytes(), v.getBytes());
            }
        }, key);
    }

    @Override
    public String get(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(RedisAdvancedClusterCommands<byte[], byte[]> commands) throws CacheConnectionException {
                return new String(commands.get(key.key().getBytes()));
            }
        }, key);
    }

    @Override
    public String get(final KEY key, final CacheDataNotFound<String> hook) {
        try {
            return redisPool.execute(new Executor<String>() {
                @Override
                public String execute(RedisAdvancedClusterCommands<byte[], byte[]> commend) {
                    String value = new String(commend.get(key.key().getBytes()));
                    if (StringUtility.isNullOrEmpty(value)) {
                        value = hook.read(key);
                        try {
                            LuttuceCacheString.this.set(key, value);
                        } catch (CacheConnectionException ignore) {
                        }
                    }
                    return value;
                }
            }, key);
        } catch (CacheConnectionException e) {
            return hook.read(key);
        }
    }

    @Override
    public <T> T get(final KEY key, final Class clazz, final CacheDataNotFound<T> hook) {
        try {
            return redisPool.execute(new Executor<T>() {
                @Override
                public T execute(RedisAdvancedClusterCommands<byte[], byte[]> jedis) throws CacheConnectionException {
                    String json = jedis.get(key.key());
                    if (StringUtility.isNullOrEmpty(json)) {
                        if (redisPool.getCacheMonitor() != null) {
                            redisPool.getCacheMonitor().penetrate(key);
                        }
                        T value = hook.read(key);
                        LuttuceCacheString.this.set(key, value);
                        return hook.read(key);
                    }
                    TypeConverter typeConverter = new TypeConverter(clazz);
                    return (T) typeConverter.convert(json);
                }
            }, key);
        } catch (CacheConnectionException e) {
            if (redisPool.getCacheMonitor() != null) {
                redisPool.getCacheMonitor().penetrate(key);
            }
            return hook.read(key);
        }
    }

    @Override
    public <T> T get(final KEY key, final Class clazz) throws CacheConnectionException {
        return redisPool.execute(new Executor<T>() {
            @Override
            public T execute(RedisAdvancedClusterCommands<String, String> jedis) throws CacheConnectionException {
                String json = jedis.get(key.key());
                if (StringUtility.isNullOrEmpty(json)) {
                    return null;
                }
                if (POJO.class.isAssignableFrom(clazz)) {
                    return (T) jsonProvider.parse(json, clazz);
                }
                TypeConverter typeConverter = new TypeConverter(clazz);
                return (T) typeConverter.convert(json);
            }
        }, key);
    }

    @Override
    public Long append(final KEY key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.append(key.key(), value.toString());
            }
        }, key);
    }

    @Override
    public Long decrease(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.decr(key.key());
            }
        }, key);
    }

    @Override
    public Long decrease(final KEY key, final Long count) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.decrBy(key.key(), count);
            }
        }, key);
    }

    @Override
    public Long increase(final KEY key, final Long count) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.incrBy(key.key(), count);
            }
        }, key);
    }

    @Override
    public Long increase(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.incr(key.key());
            }
        }, key);
    }

    @Override
    public boolean bit(final KEY key, final Integer offset) throws CacheConnectionException {
        return redisPool.execute(new Executor<Boolean>() {
            @Override
            public Boolean execute(ShardedJedis jedis) {
                return jedis.getbit(key.key(), offset);
            }
        }, key);
    }

    @Override
    public String setExpire(final KEY key, final Integer seconds, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(ShardedJedis jedis) {
                return jedis.setex(key.key(), seconds, value.toString());
            }
        }, key);
    }


    @Override
    public Long setIfNotExist(final KEY key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                return jedis.setnx(key.key(), value.toString());
            }
        }, key);
    }
}
