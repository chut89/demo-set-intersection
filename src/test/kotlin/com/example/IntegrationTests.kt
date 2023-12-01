// TODO: test generate random numbers and processing time
package com.example

import org.springframework.boot.autoconfigure.EnableAutoConfiguration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.test.context.ContextConfiguration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

//import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
class IntegrationTests {

    val logger = org.slf4j.LoggerFactory.getLogger(IntegrationTests::class.java)
           
	@Test
	fun contextLoads() {
	}
	
	@Test
	fun testLogging() {
	    logger.info("Test started")
        com.example.setintersection.SetIntersectionService().computeIntersection(listOf(1, 2, 3, 4), listOf(3, 4))
	    Assertions.assertTrue(true)
	}
}
