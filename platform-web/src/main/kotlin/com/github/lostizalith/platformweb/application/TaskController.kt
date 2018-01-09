package com.github.lostizalith.platformweb.application

import com.github.lostizalith.platformweb.domain.TaskManagement
import org.dozer.DozerBeanMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/{appName}/tasks"], produces = [APPLICATION_JSON_UTF8_VALUE])
class TaskController(
        @Autowired private val dozerBeanMapper: DozerBeanMapper,
        @Autowired private val taskManagement: TaskManagement) {

    @PostMapping
    fun submitTask(@PathVariable appName: String,
                   @RequestBody taskConfigurationRequest: TaskConfigurationRequest): ResponseEntity<*> {

        val taskConfiguration = dozerBeanMapper.map(taskConfigurationRequest, taskConfigurationRequest.configurationClass())

        val taskResult = taskManagement.executeTask(taskConfiguration)

        // TODO: map task result to response entity.

        return ResponseEntity.ok().body(taskResult)
    }
}
