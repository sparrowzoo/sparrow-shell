package com.sparrow.concurrent;

import com.sparrow.cache.CacheClient;
import com.sparrow.constant.cache.KEY;
import com.sparrow.exception.CacheConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributionLock {
    private static Logger logger = LoggerFactory.getLogger(DistributionLock.class);

    private CacheClient cacheClient;
    private KEY LOCK;
    private int expireMillis = 60 * 1000;

    private final int MAX_SLEEP_MILLIS = 1024;

    private volatile boolean locked = false;

    public DistributionLock(CacheClient cacheClient, String lockKey) {

        this.cacheClient = cacheClient;
        this.LOCK = KEY.parse(lockKey);
    }

    public DistributionLock(CacheClient cacheClient, String lockKey, Integer expireSecs) {
        this(cacheClient, lockKey);
        this.expireMillis = expireSecs * 1000;
    }

    /**
     * @return lock key
     */
    public String getLockKey() {
        return this.LOCK.key();
    }

    public long acquireLock() throws InterruptedException, CacheConnectionException {
        int times = 0;
        while (true) {
            //+1保证每次请求的expire 不同
            long expires = System.currentTimeMillis() + expireMillis + 1;
            if (cacheClient.string().setIfNotExist(LOCK, expires + "") > 0) { //同步的
                // lock acquired
                locked = true;
                return expires;
            }
            String currentExpireTimeMillis = cacheClient.string().get(LOCK); //redis里的时间
            // lock is expired
            if (currentExpireTimeMillis != null && Long.parseLong(currentExpireTimeMillis) < System.currentTimeMillis()) {
                //multi thread
                /**
                 * e.g ..
                 * current time 1
                 * thread new_time old_time
                 * t1       2         1         equals 1
                 * t2       3         2         last_new=3 略大于2
                 */
                String oldExpire = cacheClient.string().getSet(LOCK, expires);
                if (oldExpire != null && oldExpire.equals(currentExpireTimeMillis)) {
                    locked = true;
                    return expires;
                }
            }
            //加随机数线程相对公平
            int randomMillis = (int) (Math.random() * 10);
            int sleepTime = (1 << times++) + randomMillis;
            if (sleepTime >= MAX_SLEEP_MILLIS) {
                sleepTime = MAX_SLEEP_MILLIS + randomMillis;
            }
            Thread.sleep(sleepTime);
        }
    }

    public void release(long expire) throws CacheConnectionException {
        if (locked) {
            String oldExpire = cacheClient.string().get(LOCK);
            if (oldExpire != null && Long.valueOf(oldExpire) == expire) {
                cacheClient.key().delete(LOCK);
            }
            locked = false;
        }
    }
}
