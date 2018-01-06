package com.github.lostizalith.greeting.application.mapping

import com.github.lostizalith.greeting.application.dto.GreetingResponse
import com.github.lostizalith.greeting.domain.Greeting
import org.dozer.loader.api.BeanMappingBuilder
import org.dozer.loader.api.TypeMappingOptions

/**
 * Mapping rules for Greeting to GreetingResponse.
 */
class Greeting2GreetingResponse : BeanMappingBuilder() {

    override fun configure() {
        mapping(Greeting::class.java, GreetingResponse::class.java, TypeMappingOptions.oneWay())
                .fields("id", "id")
                .fields("name", "name");
    }
}
