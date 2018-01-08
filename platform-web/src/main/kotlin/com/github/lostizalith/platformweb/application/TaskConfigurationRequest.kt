package com.github.lostizalith.platformweb.application

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.lostizalith.platformweb.domain.TaskConfiguration
import java.io.Serializable

/**
 * Task configuration request entity.
 */
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
