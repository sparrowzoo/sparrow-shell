package com.sparrow.job.leader;

import com.sparrow.job.JobInstance;
import com.sparrow.registry.RegistryCenter;
import com.sparrow.utility.BlockUtility;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author harry
 * @date 2018/5/25
 */
public class LeaderElection {
    private static Logger logger = LoggerFactory.getLogger(LeaderElection.class);

    private RegistryCenter registryCenter;

    private JobInstance jobInstance;

    public LeaderElection(final RegistryCenter registryCenter, JobInstance jobInstance) {
        this.registryCenter = registryCenter;
        this.jobInstance = jobInstance;
    }

    /**
     * 选举主节点.
     */
    public void electLeader() {
        logger.debug("Elect a new leader now. {}", Thread.currentThread().getName());
        try (LeaderLatch latch = new LeaderLatch(this.getClient(), LeaderNode.LATCH)) {
            latch.start();
            latch.await();
            logger.debug("leader election successful {}", LeaderNode.LATCH);
            if (!hasLeader()) {
                registryCenter.persistEphemeral(LeaderNode.INSTANCE, this.jobInstance.getIp());
            }
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
        logger.debug("Leader election completed.");
    }

    private CuratorFramework getClient() {
        return (CuratorFramework) registryCenter.getRawClient();
    }


    /**
     * 是否为leader
     */
    public boolean isLeaderUntilBlock() {
        while (!hasLeader()) {
            logger.info("Leader is electing, waiting for {} ms", 100);
            BlockUtility.waitingShortTime();
            electLeader();
        }
        return isLeader();
    }

    /**
     * 判断当前节点是否是主节点.
     */
    public boolean isLeader() {
        return registryCenter.getCache(LeaderNode.INSTANCE).equals(this.jobInstance.getJobInstanceId());
    }

    /**
     * 判断是否已经有主节点.
     */
    public boolean hasLeader() {
        return registryCenter.exist(LeaderNode.INSTANCE);
    }

    /**
     * 删除主节点供重新选举.
     */
    public void removeLeader() {
        if (registryCenter.exist(LeaderNode.INSTANCE)) {
            registryCenter.remove(LeaderNode.INSTANCE);
        }
    }
}
