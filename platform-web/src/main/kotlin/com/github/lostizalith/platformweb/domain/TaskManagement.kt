package com.github.lostizalith.platformweb.domain

interface TaskManagement {

    fun executeTask(taskConfiguration: TaskConfiguration): TaskResult
}
