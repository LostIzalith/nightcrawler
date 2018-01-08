package com.github.lostizalith.platformweb.domain

import org.springframework.stereotype.Service

/**
 * Greeting configuration task executor.
 */
@Service
class GreetingTaskExecutor : AbstractTaskExecutor() {

    override fun resolveResult(taskId: String, configuration: TaskConfiguration): Result {
        return Result(taskId, "SOME MESSAGE FROM EXECUTOR")
    }
}