package com.github.lostizalith.platformweb.domain.executor

import com.github.lostizalith.platformweb.domain.model.Result
import com.github.lostizalith.platformweb.domain.model.Task
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration

abstract class AbstractTaskExecutor : TaskExecutor {

    override fun execute(task: Task): Result {
        return resolveResult(task.id, task.configuration)
    }

    protected abstract fun resolveResult(taskId: String, configuration: TaskConfiguration): Result
}
