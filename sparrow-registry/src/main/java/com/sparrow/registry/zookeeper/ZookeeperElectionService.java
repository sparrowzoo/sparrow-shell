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

import com.sparrow.registry.ElectionCandidate;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ZookeeperElectionService {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperElectionService.class);
    private final CountDownLatch leaderLatch = new CountDownLatch(1);

    private final LeaderSelector leaderSelector;

    public ZookeeperElectionService(final String identity, final CuratorFramework client, final String electionPath,
        final ElectionCandidate electionCandidate) {
        leaderSelector = new LeaderSelector(client, electionPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(final CuratorFramework client) throws Exception {
                logger.info(" {} has leadership", identity);
                electionCandidate.startLeadership();
                leaderLatch.await();
                logger.warn("{} lost leadership.", identity);
                electionCandidate.stopLeadership();
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.setId(identity);
    }

    /**
     * start elect
     */
    public void start() {
        logger.debug("{} start to elect leadership", leaderSelector.getId());
        leaderSelector.start();
    }

    /**
     * stop elect
     */
    public void stop() {
        logger.info(" stop leadership election");
        leaderLatch.countDown();
        leaderSelector.close();
    }
}
