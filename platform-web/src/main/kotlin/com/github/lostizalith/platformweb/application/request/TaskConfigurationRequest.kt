package com.github.lostizalith.platformweb.application.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.lostizalith.platformweb.domain.model.configuration.TaskConfiguration
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(
        JsonSubTypes.Type(value = GreetingTaskConfigurationRequest::class, name = "greeting")
)
interface TaskConfigurationRequest : Serializable {

    /**
     * Return task configuration class.
     */
    fun configurationClass(): Class<out TaskConfiguration>
}
