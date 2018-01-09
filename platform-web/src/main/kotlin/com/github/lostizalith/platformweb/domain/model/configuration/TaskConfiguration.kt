package com.github.lostizalith.platformweb.domain.model.configuration

import com.github.lostizalith.platformweb.domain.handler.TaskConfigurationHandling
import com.github.lostizalith.platformweb.domain.executor.TaskExecutor
import com.github.lostizalith.platformweb.domain.model.TaskResult
import java.io.Serializable

interface TaskConfiguration : Serializable {

    fun appName(): String

    fun submit(appName: String, handler: TaskConfigurationHandling): TaskResult

    fun executorClass(): Class<out TaskExecutor>
}
