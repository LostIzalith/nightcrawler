package com.github.lostizalith.platformweb.domain.model

import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration

data class Task(val id: String, val configuration: TaskConfiguration)
