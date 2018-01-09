package com.github.lostizalith.platformweb.application.mapping

import com.github.lostizalith.platformweb.application.request.GreetingTaskConfigurationRequest
import com.github.lostizalith.platformweb.domain.model.configuration.GreetingTaskConfiguration
import org.dozer.loader.api.BeanMappingBuilder
import org.dozer.loader.api.TypeMappingOptions.oneWay

class TaskConfigRequest2TaskConfig : BeanMappingBuilder() {

    override fun configure() {
        mapping(GreetingTaskConfigurationRequest::class.java, GreetingTaskConfiguration::class.java, oneWay())
                .fields("message", "message")
    }
}
