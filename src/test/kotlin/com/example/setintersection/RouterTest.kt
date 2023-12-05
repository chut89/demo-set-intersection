package com.example.setintersection

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
import com.example.setintersection.SetIntersectionService
import com.example.config.SecurityConfig

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ SetIntersectionRequestHandler::class, SetIntersectionRouter::class, SetIntersectionService::class,
    SecurityConfig::class // without this SecurityFilterChain cannot be loaded and therefore every page is prohibited!!!
])
//@WebAppConfiguration
//@EnableAutoConfiguration
class RouterTest {
    //@Autowired
    var webTestClient: WebTestClient? = null
    
    //@MockBean
    var serviceMock: SetIntersectionService = mock<SetIntersectionService>()
    
    @Autowired
    lateinit var route: RouterFunction<ServerResponse>
    
    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient
                        .bindToRouterFunction(route)
                        //.baseUrl("http://localhost:$port/")
                        //.defaultHeader("Authorization", "Basic ${defaultBase64EncodedCredential()}")            
                        .build()
    }
            
	@Test
	fun contextLoads() {
	}
	
	@Test
	fun testSetIntersectionSimple() {
	    whenever(serviceMock?.computeIntersection(any(), any()))?.thenReturn(Pair(setOf(3, 4), "123us"))
	
	    webTestClient
	            ?.get()?.uri{ builder -> builder
                ?.path("/api/setintersection/simple")
                    ?.queryParam("firstCollection", listOf(1,2,3,4).toStringAsQueryParam())
                        ?.queryParam("secondCollection", listOf(3,4).toStringAsQueryParam())?.build() }
          ?.exchange()
          // and use the dedicated DSL to test assertions against the response
          ?.expectStatus()?.isOk()
          ?.expectBody()
                       ?.jsonPath("$['first'].length()")?.isEqualTo(2)
                       ?.jsonPath("$['first'][0]")?.isEqualTo(3)
                       ?.jsonPath("$['first'][1]")?.isEqualTo(4)
                       ?.jsonPath("$['second']")?.isNotEmpty()
	}
	
    private fun defaultBase64EncodedCredential(): String = Base64.getEncoder().encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

    private fun List<Int>.toStringAsQueryParam(): String {
        return this.toString().replace(" ", "").replace("[", "").replace("]", "")
    }
}
