package com.github.lostizalith.platformweb.domain.model

import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration
import java.io.Serializable

data class TaskResult(
        var id: String,

        var status: String,

        var configuration: TaskConfiguration,

        var result: Serializable
) : Serializable
