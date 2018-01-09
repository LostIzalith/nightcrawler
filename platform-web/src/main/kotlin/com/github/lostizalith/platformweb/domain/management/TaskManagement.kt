package com.github.lostizalith.platformweb.domain.management

import com.github.lostizalith.platformweb.domain.model.TaskResult
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration

interface TaskManagement {

    fun executeTask(taskConfiguration: TaskConfiguration): TaskResult
}
