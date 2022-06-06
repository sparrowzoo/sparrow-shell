package com.sparrow.job.api;


/**
 * @author harry
 */
public interface TaskService {
    /**
     *  task execute success
     *
     * @param taskId task id
     * @return
     */
    Boolean successTask(Long taskId);

    /**
     * task execute fail
     *
     * @param taskId task id
     * @param error  error
     * @return
     */
    Boolean failTask(Long taskId, String error);
}
