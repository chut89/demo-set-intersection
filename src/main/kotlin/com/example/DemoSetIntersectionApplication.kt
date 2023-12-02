package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication	
import org.springframework.boot.SpringApplication
import org.springframework.boot.Banner
import org.springframework.boot.context.properties.EnableConfigurationProperties

import org.slf4j.LoggerFactory

import com.example.config.SpringdocProperties

@SpringBootApplication
@EnableConfigurationProperties(SpringdocProperties::class)
class DemoSetIntersectionApplication

fun main(args: Array<String>) {
    SpringApplication(DemoSetIntersectionApplication::class.java).apply{ setBannerMode(Banner.Mode.CONSOLE) }.run(*args)
}
