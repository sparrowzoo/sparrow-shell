package com.sparrow.job.repository.impl;

import com.sparrow.job.po.Job;
import com.sparrow.job.repository.JobRepository;
import com.sparrow.orm.template.impl.ORMStrategy;

import java.util.List;

/**
 * created by harry on 2018/5/28.
 *
 * @author harry
 */
public class JobRepositoryImpl extends ORMStrategy<Job, Long> implements JobRepository {
    @Override
    public List<Job> getAllowAutoCreatableJobList() {
        return null;
    }
}
