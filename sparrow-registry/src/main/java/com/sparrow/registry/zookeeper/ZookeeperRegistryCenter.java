/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.registry.zookeeper;

import com.google.common.base.Preconditions;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.registry.RegistryCenter;
import com.sparrow.utility.StringUtility;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ZookeeperRegistryCenter implements RegistryCenter {

    private Logger log = LoggerFactory.getLogger(ZookeeperRegistryCenter.class);

    private Configuration zkConfig;

    private final Map<String, TreeCache> caches = new HashMap<String, TreeCache>();

    private CuratorFramework client;

    public ZookeeperRegistryCenter(final Configuration zkConfig) {
        this.zkConfig = zkConfig;
    }

    public void init() throws InterruptedException, KeeperException.OperationTimeoutException {
        log.debug("Elastic job: zookeeper registry center init, server lists is: {}.", zkConfig.getServerLists());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
            .connectString(zkConfig.getServerLists())
            .retryPolicy(new ExponentialBackoffRetry(zkConfig.getWaitRetryIntervalTime(), zkConfig.getMaxRetryTimes(), zkConfig.getMaxWaitRetryIntervalTime()))
            .namespace(zkConfig.getNamespace());
        if (0 != zkConfig.getSessionTimeout()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeout());
        }
        if (0 != zkConfig.getConnectionTimeout()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeout());
        }
        if (!StringUtility.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charset.forName(Constant.CHARSET_UTF_8)))
                .aclProvider(new ACLProvider() {

                    @Override
                    public List<ACL> getDefaultAcl() {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }

                    @Override
                    public List<ACL> getAclForPath(final String path) {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }
                });
        }
        client = builder.build();
        client.start();
        if (!client.blockUntilConnected(zkConfig.getMaxWaitRetryIntervalTime() * zkConfig.getMaxRetryTimes(), TimeUnit.MILLISECONDS)) {
            client.close();
            throw new KeeperException.OperationTimeoutException();
        }
    }

    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /*
     * TODO wait 500ms,guarantee close client before cache
     * bug：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return this.getFromRemote(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charset.forName(Constant.CHARSET_UTF_8));
        }
        return getFromRemote(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String getFromRemote(final String key) {
        try {
            return new String(client.getData().forPath(key), Charset.forName(Constant.CHARSET_UTF_8));
        } catch (Exception e) {
            log.error("get from remote", e);
            return null;
        }
    }

    @Override
    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            Collections.sort(result, new Comparator<String>() {

                @Override
                public int compare(final String o1, final String o2) {
                    return o2.compareTo(o1);
                }
            });
            return result;
        } catch (final Exception ex) {
            log.error("get child keys", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public int getChildrenCount(final String key) {
        try {
            Stat stat = client.checkExists().forPath(key);
            if (null != stat) {
                return stat.getNumChildren();
            }
        } catch (final Exception ex) {
            log.error("get children count", ex);
        }
        return 0;
    }

    @Override
    public boolean exist(final String key) {
        try {
            return null != client.checkExists().forPath(key);
        } catch (final Exception ex) {
            log.error("exist", ex);
            return false;
        }
    }

    @Override
    public void persist(final String key, final String value) {
        try {
            if (!exist(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charset.forName(Constant.CHARSET_UTF_8)));
            } else {
                modify(key, value);
            }
        } catch (final Exception ex) {
            log.error("persist", ex);
        }
    }

    @Override
    public void modify(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charset.forName(Constant.CHARSET_UTF_8))).and().commit();
        } catch (final Exception ex) {
            log.error("modify", ex);
        }
    }

    @Override
    public void persistEphemeral(final String key, final String value) {
        try {
            if (exist(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName(Constant.CHARSET_UTF_8)));
        } catch (final Exception ex) {
            log.error("persist ephemeral", ex);
        }
    }

    @Override
    public String persistSequential(final String key, final String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charset.forName(Constant.CHARSET_UTF_8)));
        } catch (final Exception ex) {
            log.error("persist sequential", ex);
        }
        return null;
    }

    @Override
    public void persistEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (final Exception ex) {
            log.error("persist ephemeral sequential", ex);
        }
    }

    @Override
    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception ex) {
            log.error("remove", ex);
        }
    }

    @Override
    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            persist(key, "");
            result = client.checkExists().forPath(key).getMtime();
        } catch (final Exception ex) {
            log.error("get registry center time", ex);
        }
        Preconditions.checkState(0L != result, "Cannot get registry center time.");
        return result;
    }

    @Override
    public void addLocalCache(final String cachePath) {
        TreeCache cache = new TreeCache(client, cachePath);
        try {
            cache.start();
        } catch (final Exception ex) {
            log.error("add cache data", ex);
        }
        caches.put(cachePath + "/", cache);
    }

    @Override
    public void evictCacheData(final String cachePath) {
        TreeCache cache = caches.remove(cachePath + "/");
        if (null != cache) {
            cache.close();
        }
    }

    @Override
    public Object getCache(final String cachePath) {
        return caches.get(cachePath + "/");
    }

    @Override
    public Object getRawClient() {
        return this.client;
    }
}
