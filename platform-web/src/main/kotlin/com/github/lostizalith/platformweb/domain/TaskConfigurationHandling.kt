package com.github.lostizalith.platformweb.domain

interface TaskConfigurationHandling {

    fun handle(appName: String, configuration: GreetingTaskConfiguration): TaskResult
}
