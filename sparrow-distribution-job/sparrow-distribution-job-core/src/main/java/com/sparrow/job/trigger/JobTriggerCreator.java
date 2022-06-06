package com.sparrow.job.trigger;

import com.sparrow.job.po.Task;
import com.sparrow.job.po.Trigger;

import java.util.List;

/**
 * @author harry
 */
public interface JobTriggerCreator {
    Trigger createTrigger(Trigger previousTrigger);

    List<Task> createTask(Trigger trigger);

    List<Task> createChildTask(Trigger trigger, Task previousTask);
}
