package com.github.lostizalith.platformweb.domain

/**
 * Execute task due configuration.
 */
interface TaskExecutor {

    fun execute(task: Task): Result
}
