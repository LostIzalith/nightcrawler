package com.github.lostizalith.greeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Simple spring boot greeting application.
 */
@SpringBootApplication
class GreetingApplication

fun main(args: Array<String>) {
    SpringApplication.run(GreetingApplication::class.java, *args)
}
