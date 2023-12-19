package com.example.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@TestConfiguration
class TestOverridenConfigurationForSecurityConfigTest {
    @TestComponent
    class TestRequestHandler {
        suspend fun test(request: ServerRequest): ServerResponse {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait("Authentication passed!")
        }
    }

    @Bean
    fun route(testRequestHandler: TestRequestHandler): RouterFunction<ServerResponse> {
        return coRouter {
            GET("/api/setintersection/simple", testRequestHandler::test)
        }
    }
}

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestOverridenConfigurationForSecurityConfigTest::class, SecurityConfig::class])
// Without this annotation, webHandler cannot be found!!!
@WebFluxTest
class SecurityConfigTest {
    @Autowired
    lateinit var context: ApplicationContext

    // @Autowired is done automatically
    lateinit var rest: WebTestClient

    @BeforeEach
    fun setup() {
        this.rest =
            WebTestClient
                .bindToApplicationContext(this.context)
                // when @WebFluxTest is on, SecurityConfig is automatically scanned, therefore the following application is redundant
                // .apply(springSecurity())
                .build()
    }

    @Test
    fun `Without @WithMockUser server should response 401 Unauthorized`() {
        this.rest.get().uri("/api/setintersection/simple")
            .exchange()
            .expectStatus().isUnauthorized()
    }

    @Test
    @WithMockUser(roles = ["USER"]) // This is the same as @WithMockUser
    fun `If user has USER role then server should let authentication pass`() {
        this.rest.get().uri("/api/setintersection/simple")
            .exchange()
            .expectStatus().isOk()
            .expectBody<String>().isEqualTo("Authentication passed!")
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `If user has ADMIN role then server should forbid request`() {
        this.rest.get().uri("/api/setintersection/simple")
            .exchange()
            .expectStatus().isForbidden()
    }
}
