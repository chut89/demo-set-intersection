package com.example.setintersection

import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith



import com.example.setintersection.SetIntersectionRequestHandler
import com.example.setintersection.SetIntersectionRouter

import com.example.config.SecurityConfig

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ SetIntersectionRequestHandler::class, SetIntersectionRouter::class, TestOverridenConfiguration::class
])
@EnableAutoConfiguration
class RouterTest {

    lateinit var webTestClient: WebTestClient
    
    // @MockBean or @Autowired works equally well
    @MockBean
    lateinit var serviceMock: SetIntersectionService
    
    @Autowired
    lateinit var route: RouterFunction<ServerResponse>
    
    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
                        .bindToRouterFunction(route)
                        .build()
    }
            
	@Test
	fun contextLoads() {
	}
	
	@Test
	fun testSetIntersectionSimple() {
	    whenever(serviceMock.computeIntersection(eq(listOf(9999, 9998, 9997, 9996)), eq(listOf(9995, 9994)))).thenReturn(Pair(setOf(3, 4), "123us"))
	
	    webTestClient
	            .get().uri{ builder -> builder
                .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(9999, 9998, 9997, 9996).toStringAsQueryParam())
                        .queryParam("secondCollection", listOf(9995, 9994).toStringAsQueryParam()).build() }
          .exchange()
          .expectStatus().isOk()
          .expectBody()
                       .jsonPath("$['first'].length()").isEqualTo(2)
                       .jsonPath("$['first'][0]").isEqualTo(3)
                       .jsonPath("$['first'][1]").isEqualTo(4)
                       .jsonPath("$['second']").isEqualTo("123us")
	}
	
	@Test
	fun testSetIntersectionComplex() {
	    whenever(serviceMock.computeIntersection(eq(listOf(9999, 8888, 7777, 6666)), eq(listOf(1111, 2222)))).thenReturn(Pair(setOf(5, 6), "987us"))
	
	    webTestClient
	            .post().uri("/api/setintersection/complex")
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(Pair(listOf(9999, 8888, 7777, 6666), listOf(1111, 2222)))
          .exchange()
          .expectStatus().isOk()
          .expectBody()
                       .jsonPath("$['first'].length()").isEqualTo(2)
                       .jsonPath("$['first'][0]").isEqualTo(5)
                       .jsonPath("$['first'][1]").isEqualTo(6)
                       .jsonPath("$['second']").isEqualTo("987us")	
	}

    private fun defaultBase64EncodedCredential(): String = Base64.getEncoder().encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

    private fun List<Int>.toStringAsQueryParam(): String {
        return this.toString().replace(" ", "").replace("[", "").replace("]", "")
    }
}
