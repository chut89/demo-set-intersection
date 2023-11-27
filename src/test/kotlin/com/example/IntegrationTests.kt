package com.example

import org.springframework.boot.autoconfigure.EnableAutoConfiguration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

import org.junit.jupiter.api.Test

import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.example.setintersection.SetIntersectionRequestHandler
import com.example.setintersection.SetIntersectionRouter
import com.example.setintersection.SetIntersectionService
import com.example.config.SecurityConfig

import org.assertj.core.api.Assertions.assertThat
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [ SetIntersectionRequestHandler::class, SetIntersectionRouter::class, SetIntersectionService::class, 
    SecurityConfig::class // without this SecurityFilterChain cannot be loaded and therefore every page is prohibited!!!
])
@EnableAutoConfiguration
class IntegrationTests(@Autowired var webTestClient: WebTestClient) {
            
	@Test
	fun contextLoads() {
	}

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Assert set intersection is returned when sets' sizes are small`() {          
      webTestClient
          // Create a GET request to test an endpoint
          .get().uri{ builder -> builder
                .path("/api/setintersection/simple")
                    .queryParam("firstCollection", "1,2,3,4")
                        .queryParam("secondCollection", "3,4").build() }
          .header("Authorization", "Basic ${defaultBase64EncodedCredential()}")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isOk()
          .expectBody().jsonPath("$['first'][0]").isEqualTo(3)

      webTestClient
          // Create a GET request to test an endpoint
          .get().uri{ builder -> builder
                .path("/api/setintersection/simple")
                    .queryParam("firstCollection", "1,2,3,4")
                        .queryParam("secondCollection", "3,4").build() }
          .header("Authorization", "Basic ${defaultBase64EncodedCredential()}")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isOk()
          .expectBody().jsonPath("$['first'][1]").isEqualTo(4)
    }
    
    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Assert set intersection is returned when sets' sizes are big`() {
        webTestClient
          // Create a POST request to test an endpoint        
          .post().uri("/api/setintersection/complex")
          .header("Authorization", "Basic ${defaultBase64EncodedCredential()}")
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isOk()
          .expectBody().jsonPath("$['first'][0]").isEqualTo(3)

        webTestClient
          // Create a POST request to test an endpoint        
          .post().uri("/api/setintersection/complex")
          .header("Authorization", "Basic ${defaultBase64EncodedCredential()}")
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isOk()
          .expectBody().jsonPath("$['first'][1]").isEqualTo(4)           
    }
    
    @Test
    fun `Assert Unauthorized when request is provided without identity credential`() {
        webTestClient
          .post().uri("/api/setintersection/complex")
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isUnauthorized()
    }

    @Test
    fun `Assert Unauthorized with invalid password`() {
        webTestClient
          .post().uri("/api/setintersection/complex")
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
          .exchange()
          // and use the dedicated DSL to test assertions against the response
          .expectStatus().isUnauthorized()
    }
    
    private fun defaultBase64EncodedCredential(): String = Base64.getEncoder().encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

}
