package com.github.lostizalith.platformweb.domain.handler

import com.github.lostizalith.platformweb.domain.model.Task
import com.github.lostizalith.platformweb.domain.model.TaskResult
import com.github.lostizalith.platformweb.domain.model.configuration.GreetingTaskConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class TaskConfigurationHandler(
        @Autowired private val applicationContext: ApplicationContext
) : TaskConfigurationHandling {

    override fun handle(appName: String, taskConfiguration: GreetingTaskConfiguration): TaskResult {
        // TODO: not implemented yet.
        // TODO: some additional info due configManagement

        // TODO: result manager stub like we register(save) task
        val taskResult = TaskResult("string id", "CREATED", taskConfiguration, "SOME RESULT")

        val task = Task(taskResult.id, taskResult.configuration)

        // TODO: producer and consumer
        // TODO: maybe it will be cool to use RabbitMQ as queue

        // TODO: it's like we produce and consume some task
        // TODO: then we execute it ¯\_(ツ)_/¯
        val taskExecutor = applicationContext.getBean(task.configuration.executorClass())
        val result = taskExecutor.execute(task)

        // TODO: then update in db WTF
        taskResult.id = result.id
        taskResult.status = "LIKE IN PROGRESS"
        taskResult.result = result.data

        return taskResult
    }
}
