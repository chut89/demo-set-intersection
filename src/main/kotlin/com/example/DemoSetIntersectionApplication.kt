package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication	
import org.springframework.boot.SpringApplication
import org.springframework.boot.Banner
import org.springframework.boot.context.properties.EnableConfigurationProperties

//import io.github.oshai.kotlinlogging.KotlinLogging
//import org.apache.logging.log4j.Logger
import org.slf4j.LoggerFactory

@SpringBootApplication
class DemoSetIntersectionApplication

fun main(args: Array<String>) {
    LoggerFactory.getLogger(DemoSetIntersectionApplication::class.java).info("test")
    SpringApplication(DemoSetIntersectionApplication::class.java).apply{ setBannerMode(Banner.Mode.CONSOLE) }.run(*args)
    LoggerFactory.getLogger(DemoSetIntersectionApplication::class.java).info("test")
}
