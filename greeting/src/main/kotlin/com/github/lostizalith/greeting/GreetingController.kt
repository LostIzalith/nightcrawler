package com.github.lostizalith.greeting

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
class GreetingController {

    private val counter = AtomicLong()

    @GetMapping(value = ["/greeting"])
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.getAndIncrement(), "Hello $name !")
}
