package com.github.lostizalith.platformweb.domain

import org.apache.commons.lang3.StringUtils

/**
 * Greeting task configuration.
 */
class GreetingTaskConfiguration : TaskConfiguration {

    var message: String = StringUtils.EMPTY

    override fun getAppName(): String {
        TODO("not implemented")
    }

    override fun taskHandle(appName: String) {
        TODO("not implemented")
    }

    override fun postLoadHandle(appName: String) {
        TODO("not implemented")
    }

    override fun executorClass(): Class<out TaskExecutor> = GreetingTaskExecutor::class.java
}
