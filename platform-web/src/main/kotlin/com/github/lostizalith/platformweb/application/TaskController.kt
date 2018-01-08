package com.github.lostizalith.platformweb.application

import org.dozer.DozerBeanMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Rest endpoint to handle task execution.
 */
@RestController
@RequestMapping(value = ["/api/v1/{appName}/tasks"])
class TaskController(val dozerBeanMapper: DozerBeanMapper) {

    @PostMapping
    fun submitTask(@PathVariable appName: String,
                   @RequestBody taskConfigurationRequest: TaskConfigurationRequest): ResponseEntity<*> {

        val taskConfiguration = dozerBeanMapper.map(taskConfigurationRequest, taskConfigurationRequest.configurationClass())

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(taskConfiguration)
    }
}
