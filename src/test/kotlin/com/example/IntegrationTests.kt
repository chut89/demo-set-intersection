package com.example

import org.springframework.boot.autoconfigure.EnableAutoConfiguration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
//import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import org.springframework.web.context.WebApplicationContext

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.example.setintersection.SetIntersectionController
import com.example.setintersection.SetIntersectionService
import com.example.config.SecurityConfig

import org.assertj.core.api.Assertions.assertThat
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ SetIntersectionController::class, SetIntersectionService::class, 
    SecurityConfig::class // without this SecurityFilterChain cannot be loaded and therefore every page is prohibited!!!
])
//@WebAppConfiguration
@EnableAutoConfiguration
//@WebMvcTest
@AutoConfigureMockMvc
class IntegrationTests(@Autowired var mockMvc: MockMvc) {
    //private var requestBuilder: MockHttpServletRequestBuilder = null
        
    @Autowired
    private lateinit var context: WebApplicationContext    
    
	@BeforeEach
	fun setup() {
	    /*
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity()) 
            .build()		*/
	}

	@Test
	fun contextLoads() {
	}

    @Test
    fun `Assert set intersection is returned when sets' sizes are big`() {
        
        val base64EncodedCredential = Base64.getEncoder().encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

        mockMvc.get(URI("/api/setintersection/simple"), {
            //this.header("Authorization", "Basic ${base64EncodedCredential}")
            this.accept(MediaType.APPLICATION_JSON)
            this.param("firstCollection", "1,2,3,4")
            this.param("secondCollection", "3,4")
        }).andExpect({ status{ isOk() } })
        
      /*  
      webTestClient
          // Create a GET request to test an endpoint
          .get().uri("/api/setintersection/simple")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isOk()
          .expectBody(Pair::class.java).value{ pair -> {
            val (first, second) = pair
            assertThat(first).isEqualTo("[3,4]")
              
        }};
        */
    }
    
    @Test
    fun `Assert Unauthorized with invalid password`() {
        val base64EncodedCredential = Base64.getEncoder().encodeToString("user:invalid".toByteArray(StandardCharsets.UTF_8))

        mockMvc.get(URI("/api/setintersection/simple"), {
            this.header("Authorization", "Basic ${base64EncodedCredential}")
            this.accept(MediaType.APPLICATION_JSON)
            this.param("firstCollection", "1,2,3,4")
            this.param("secondCollection", "3,4")
        }).andExpect({ status{ isUnauthorized() } })    
    }

}
