package com.sparrow.job.leader;

/**
 * 主节点路径.
 *
 * @author harry
 */
public final class LeaderNode {

    /**
     * 主节点根路径.
     */
    public static final String ROOT = "leader";

    public static final String ELECTION_ROOT = ROOT + "/election";

    public static final String INSTANCE = ELECTION_ROOT + "/instance";

    public static final String LATCH = ELECTION_ROOT + "/latch";
}
