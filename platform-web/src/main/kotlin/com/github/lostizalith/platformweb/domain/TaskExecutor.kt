package com.github.lostizalith.platformweb.domain

interface TaskExecutor {

    fun execute(task: Task): Result
}
