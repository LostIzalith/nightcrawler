package com.github.lostizalith.platformweb.application.mapping

import com.github.lostizalith.platformweb.application.GreetingTaskConfigurationRequest
import com.github.lostizalith.platformweb.domain.GreetingTaskConfiguration
import org.dozer.loader.api.BeanMappingBuilder
import org.dozer.loader.api.TypeMappingOptions.oneWay

/**
 * Mapping rules for task config request to domain task config entity.
 */
class TaskConfigRequest2TaskConfig : BeanMappingBuilder() {

    override fun configure() {
        mapping(GreetingTaskConfigurationRequest::class.java, GreetingTaskConfiguration::class.java, oneWay())
                .fields("message", "message")
    }
}
