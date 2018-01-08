package com.github.lostizalith.platformweb.domain

/**
 * Task configuration handler submit task due configuration.
 */
interface TaskConfigurationHandling {

    fun handle(appName: String, configuration: GreetingTaskConfiguration): TaskResult
}
