package com.github.lostizalith.greeting.application.controller

import com.github.lostizalith.greeting.application.dto.GreetingResponse
import com.github.lostizalith.greeting.domain.Greeting
import org.dozer.DozerBeanMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

/**
 * Simple greeting rest endpoint.
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class GreetingController(@Autowired val mapper: DozerBeanMapper) {

    private val counter = AtomicLong()

    @GetMapping(value = ["/greeting"])
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): ResponseEntity<*> {
        val request = Greeting(counter.getAndIncrement(), "Hello $name ! v2")

        val response = mapper.map(request, GreetingResponse::class.java)

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
    }
}
