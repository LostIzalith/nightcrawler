package com.github.lostizalith.platformweb.domain.management

import com.github.lostizalith.platformweb.domain.handler.TaskConfigurationHandling
import com.github.lostizalith.platformweb.domain.model.TaskResult
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskManager(@Autowired private val configurationHandling: TaskConfigurationHandling) : TaskManagement {

    override fun executeTask(taskConfiguration: TaskConfiguration): TaskResult {
        return taskConfiguration.submit(taskConfiguration.appName(), configurationHandling)
    }
}
