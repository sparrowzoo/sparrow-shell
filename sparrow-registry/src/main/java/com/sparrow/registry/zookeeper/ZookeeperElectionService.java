package com.sparrow.registry.zookeeper;

import com.sparrow.registry.ElectionCandidate;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author harry
 * @date 2018/5/8
 */
public class ZookeeperElectionService {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperElectionService.class);
    private final CountDownLatch leaderLatch = new CountDownLatch(1);

    private final LeaderSelector leaderSelector;

    public ZookeeperElectionService(final String identity, final CuratorFramework client, final String electionPath, final ElectionCandidate electionCandidate) {
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
