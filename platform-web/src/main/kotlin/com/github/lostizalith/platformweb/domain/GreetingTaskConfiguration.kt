package com.github.lostizalith.platformweb.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.apache.commons.lang3.StringUtils

/**
 * Greeting task configuration.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class GreetingTaskConfiguration : TaskConfiguration {

    @JsonIgnore
    var appName: String = StringUtils.EMPTY;

    var message: String = StringUtils.EMPTY

    override fun appName() = appName

    override fun submit(appName: String, handler: TaskConfigurationHandling) = handler.handle(appName, this)

    override fun executorClass(): Class<out TaskExecutor> = GreetingTaskExecutor::class.java
}
