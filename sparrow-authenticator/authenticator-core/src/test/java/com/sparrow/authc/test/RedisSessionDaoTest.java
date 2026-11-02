package com.sparrow.authc.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sparrow.authenticator.SessionStatus;
import com.sparrow.authenticator.session.DefaultSessionParser;
import com.sparrow.authenticator.session.dao.DefaultSession;
import com.sparrow.authenticator.session.dao.RedisSessionDao;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RedisSessionDaoTest {
    private final MemoryRedis redis = new MemoryRedis();
    private final RedisSessionDao dao = new RedisSessionDao(redis, new DefaultSessionParser());

    @Test
    void readsExistingSessionJsonWithoutSparrowJsonProvider() {
        redis.values.put("authc:status:terminal:42:1:PC:device",
                "{\"startTime\":100,\"lastAccessTime\":200,\"expireAt\":300,\"status\":-1}");
        SessionStatus status = dao.get(() -> "42:1:PC:device");
        assertEquals(100L, status.getStartTime());
        assertEquals(200L, status.getLastAccessTime());
        assertEquals(300L, status.getExpireAt());
        assertEquals(-1, status.getStatus());
    }

    @Test
    void missingSessionRemainsNull() {
        assertNull(dao.get(() -> "42:1:PC:device"));
    }

    @Test
    void writesSameSessionKeyAndJsonFields() {
        DefaultSession session = new DefaultSession();
        session.setSessionKey("42:1:PC:device");
        session.setUserId(42L);
        session.setStartTime(100L);
        session.setLastAccessTime(200L);
        session.setExpireAt(300L);
        session.setStatus(1);
        dao.create(session);
        JSONObject stored = JSON.parseObject(redis.values.get("authc:status:terminal:42:1:PC:device"));
        assertEquals(4, stored.size());
        assertEquals(100L, stored.getLongValue("startTime"));
        assertEquals(200L, stored.getLongValue("lastAccessTime"));
        assertEquals(300L, stored.getLongValue("expireAt"));
        assertEquals(1, stored.getIntValue("status"));
    }

    /** Only the external Redis transport is replaced; serialization runs in the real DAO. */
    private static class MemoryRedis extends RedisTemplate<String, String> {
        private final Map<String, String> values = new HashMap<>();

        @Override
        @SuppressWarnings("unchecked")
        public ValueOperations<String, String> opsForValue() {
            return (ValueOperations<String, String>) Proxy.newProxyInstance(
                    ValueOperations.class.getClassLoader(), new Class<?>[]{ValueOperations.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("get")) {
                            return values.get(args[0]);
                        }
                        if (method.getName().equals("set") && args.length == 2) {
                            values.put((String) args[0], (String) args[1]);
                            return null;
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        @Override
        @SuppressWarnings("unchecked")
        public SetOperations<String, String> opsForSet() {
            return (SetOperations<String, String>) Proxy.newProxyInstance(
                    SetOperations.class.getClassLoader(), new Class<?>[]{SetOperations.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("add")) {
                            return 1L;
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        @Override
        public Boolean expireAt(String key, Date date) {
            return true;
        }

        @Override
        public Long getExpire(String key) {
            return -1L;
        }
    }
}
