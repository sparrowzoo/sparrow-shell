package com.sparrow.registry;

/**
 * @author harrt
 * @date 2018/5/8
 */
public interface ElectionCandidate {
    /**
     * start
     *
     * @throws Exception throw exception
     */
    void startLeadership() throws Exception;

    /**
     * stop
     */
    void stopLeadership();
}
