package com.github.lostizalith.platformweb.domain

import java.io.Serializable

/**
 * Task configuration to execute.
 */
interface TaskConfiguration : Serializable {

    fun getAppName(): String

    fun submit(appName: String, handler: TaskConfigurationHandling): TaskResult

    fun postLoadHandle(appName: String)

    fun executorClass(): Class<out TaskExecutor>
}
