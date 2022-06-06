package com.sparrow.job.repository;

import com.sparrow.job.po.Job;
import com.sparrow.protocol.db.DaoSupport;

import java.util.List;

/**
 * @author harry
 * @date 2018/5/25
 */
public interface JobRepository extends DaoSupport<Job, Long> {
    /**
     * 获取所有可自动创建的job 列表
     */
    List<Job> getAllowAutoCreatableJobList();
}
