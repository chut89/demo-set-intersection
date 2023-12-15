package com.example

import com.example.config.SpringdocProperties
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(SpringdocProperties::class)
class DemoSetIntersectionApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    SpringApplication(DemoSetIntersectionApplication::class.java).apply { setBannerMode(Banner.Mode.CONSOLE) }.run(*args)
}
