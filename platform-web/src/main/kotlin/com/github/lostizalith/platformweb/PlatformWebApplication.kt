package com.github.lostizalith.platformweb

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Platform web application.
 */
@SpringBootApplication
class PlatformWebApplication

fun main(args: Array<String>) {
    SpringApplication.run(PlatformWebApplication::class.java, *args)
}
