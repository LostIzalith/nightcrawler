package com.github.lostizalith.platformweb.domain.model.configuration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.lostizalith.platformweb.domain.executor.GreetingTaskExecutor
import com.github.lostizalith.platformweb.domain.handler.TaskConfigurationHandling
import com.github.lostizalith.platformweb.domain.executor.TaskExecutor
import org.apache.commons.lang3.StringUtils

@JsonInclude(JsonInclude.Include.NON_NULL)
class GreetingTaskConfiguration : TaskConfiguration {

    @JsonIgnore
    var appName: String = StringUtils.EMPTY;

    var message: String = StringUtils.EMPTY

    override fun appName() = appName

    override fun submit(appName: String, handler: TaskConfigurationHandling) = handler.handle(appName, this)

    override fun executorClass(): Class<out TaskExecutor> = GreetingTaskExecutor::class.java
}
