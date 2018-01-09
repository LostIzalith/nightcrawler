package com.github.lostizalith.platformweb.domain.executor

import com.github.lostizalith.platformweb.domain.model.Result
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration
import org.springframework.stereotype.Service

@Service
class GreetingTaskExecutor : AbstractTaskExecutor() {

    override fun resolveResult(taskId: String, configuration: TaskConfiguration): Result {
        return Result(taskId, "SOME MESSAGE FROM EXECUTOR")
    }
}
