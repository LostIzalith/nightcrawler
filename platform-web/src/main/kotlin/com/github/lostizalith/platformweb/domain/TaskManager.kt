package com.github.lostizalith.platformweb.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskManager(@Autowired private val configurationHandling: TaskConfigurationHandling) : TaskManagement {

    override fun executeTask(taskConfiguration: TaskConfiguration): TaskResult {
        return taskConfiguration.submit(taskConfiguration.appName(), configurationHandling)
    }
}
