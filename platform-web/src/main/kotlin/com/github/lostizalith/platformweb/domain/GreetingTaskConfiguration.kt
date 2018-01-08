package com.github.lostizalith.platformweb.domain

import org.apache.commons.lang3.StringUtils

/**
 * Greeting task configuration.
 */
class GreetingTaskConfiguration : TaskConfiguration {

    private var appName: String = StringUtils.EMPTY;

    private var message: String = StringUtils.EMPTY

    override fun appName() = appName

    override fun submit(appName: String, handler: TaskConfigurationHandling) = handler.handle(appName, this)

    override fun executorClass(): Class<out TaskExecutor> = GreetingTaskExecutor::class.java
}
