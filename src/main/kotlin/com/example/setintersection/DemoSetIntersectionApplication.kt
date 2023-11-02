package com.example.setintersection

import org.springframework.boot.autoconfigure.SpringBootApplication	
import org.springframework.boot.SpringApplication
import org.springframework.boot.Banner

@SpringBootApplication
class DemoSetIntersectionApplication

fun main(args: Array<String>) {
    SpringApplication(DemoSetIntersectionApplication::class.java).apply{ setBannerMode(Banner.Mode.CONSOLE) }.run(*args)
}
