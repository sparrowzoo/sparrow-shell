package com.sparrow.redis;

import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.concurrent.DistributionLock;
import com.sparrow.container.Container;
import com.sparrow.container.impl.SparrowContainer;

public class RedisDistributionLockTest {
    public static void main(String[] args) {
        Container container = new SparrowContainer();
        container.setContextConfigLocation("/redis_config.xml");

        container.init();
        DistributionLock distributionLock = container.getBean("distributionLock");
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        long expireTime = distributionLock.acquireLock();
                        if (expireTime > 0) {
                            System.out.println(1);
                            Thread.sleep(1000L);
                        }
                        distributionLock.release(expireTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (CacheConnectionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
