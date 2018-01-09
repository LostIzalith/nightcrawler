package com.github.lostizalith.platformweb.domain

import java.io.Serializable

interface TaskConfiguration : Serializable {

    fun appName(): String

    fun submit(appName: String, handler: TaskConfigurationHandling): TaskResult

    fun executorClass(): Class<out TaskExecutor>
}
