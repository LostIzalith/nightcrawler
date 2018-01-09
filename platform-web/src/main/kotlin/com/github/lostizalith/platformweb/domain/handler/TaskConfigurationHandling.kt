package com.github.lostizalith.platformweb.domain.handler

import com.github.lostizalith.platformweb.domain.model.TaskResult
import com.github.lostizalith.platformweb.domain.model.configuration.GreetingTaskConfiguration

interface TaskConfigurationHandling {

    fun handle(appName: String, taskConfiguration: GreetingTaskConfiguration): TaskResult
}
