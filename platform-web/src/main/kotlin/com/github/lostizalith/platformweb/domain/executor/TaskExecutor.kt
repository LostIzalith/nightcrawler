package com.github.lostizalith.platformweb.domain.executor

import com.github.lostizalith.platformweb.domain.model.Result
import com.github.lostizalith.platformweb.domain.model.Task

interface TaskExecutor {

    fun execute(task: Task): Result
}
