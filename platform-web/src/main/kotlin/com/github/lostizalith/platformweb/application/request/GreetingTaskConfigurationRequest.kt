package com.github.lostizalith.platformweb.application.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.lostizalith.platformweb.domain.model.configuration.GreetingTaskConfiguration
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration
import org.apache.commons.lang3.StringUtils

@JsonInclude(JsonInclude.Include.NON_NULL)
class GreetingTaskConfigurationRequest : TaskConfigurationRequest {

    @JsonProperty(value = "message")
    var message: String = StringUtils.EMPTY

    override fun configurationClass(): Class<out TaskConfiguration> = GreetingTaskConfiguration::class.java
}
