package com.github.lostizalith.greeting.config

import org.dozer.DozerBeanMapper
import org.dozer.loader.api.BeanMappingBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AssignableTypeFilter

/**
 * Configuration for Dozer
 */
@Configuration
class DozerConfiguration {

    companion object {
        private const val PACKAGE_TO_SCAN = "com.github.lostizalith.greeting.application.mapping"
    }

    @Bean
    fun mapper(): DozerBeanMapper {
        val mapper = DozerBeanMapper()
        val registeredMappings = registeredMappings()
        registeredMappings.forEach(mapper::addMapping)

        return mapper
    }

    private fun registeredMappings(): List<BeanMappingBuilder> {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AssignableTypeFilter(BeanMappingBuilder::class.java))

        val customMapping = ArrayList<BeanMappingBuilder>()
        scanner.findCandidateComponents(PACKAGE_TO_SCAN)
                .mapTo(customMapping) { (Class.forName(it.beanClassName).newInstance() as BeanMappingBuilder) }

        return customMapping
    }
}
