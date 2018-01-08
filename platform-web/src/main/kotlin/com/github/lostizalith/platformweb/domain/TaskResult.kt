package com.github.lostizalith.platformweb.domain

import java.io.Serializable

data class TaskResult(
        var id: String,

        var status: String,

        var configuration: TaskConfiguration,

        var result: Serializable
) : Serializable
