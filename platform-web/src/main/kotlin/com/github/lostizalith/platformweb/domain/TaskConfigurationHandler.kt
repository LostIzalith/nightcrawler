package com.github.lostizalith.platformweb.domain

import org.springframework.stereotype.Service

@Service
class TaskConfigurationHandler : TaskConfigurationHandling {

    override fun handle(appName: String, taskConfiguration: GreetingTaskConfiguration): TaskResult {
        // TODO: not implemented yet.
        // TODO: producer and consumer
        // TODO: result manager

        return TaskResult("string id", "CREATED", taskConfiguration, "SOME RESULT")
    }
}
