package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication	
import org.springframework.boot.SpringApplication
import org.springframework.boot.Banner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ConfigurableApplicationContext;

import com.example.setintersection.GreetingClient

import com.example.config.SpringdocProperties

@SpringBootApplication
@EnableConfigurationProperties(SpringdocProperties::class)
class DemoSetIntersectionApplication

fun main(args: Array<String>) {
    //SpringApplication(DemoSetIntersectionApplication::class.java).apply{ setBannerMode(Banner.Mode.CONSOLE) }.run(*args)
    val context: ConfigurableApplicationContext = SpringApplication.run(DemoSetIntersectionApplication::class.java, *args)
    val greetingClient: GreetingClient = context.getBean(GreetingClient::class.java)
    // We need to block for the content here or the JVM might exit before the message is logged
    println(greetingClient.getMessage().block())
}
