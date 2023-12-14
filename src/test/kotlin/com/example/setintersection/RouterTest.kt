package com.example.setintersection

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import java.nio.charset.StandardCharsets
import java.util.Base64

@ExtendWith(SpringExtension::class)
@ContextConfiguration(
    classes = [
        SetIntersectionRequestHandler::class,
        SetIntersectionRouter::class,
        TestOverridenConfiguration::class,
    ],
)
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
        webTestClient =
            WebTestClient
                .bindToRouterFunction(route)
                .build()
    }

    @Test
    fun testSetIntersectionSimple() {
        whenever(
            serviceMock.computeIntersection(eq(listOf(9999, 9998, 9997, 9996)), eq(listOf(9995, 9994))),
        ).thenReturn(Pair(setOf(3, 4), "123us"))

        webTestClient
            .get().uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(9999, 9998, 9997, 9996).toStringAsQueryParam())
                    .queryParam("secondCollection", listOf(9995, 9994).toStringAsQueryParam()).build()
            }
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first'].length()").isEqualTo(2)
            .jsonPath("$['first'][0]").isEqualTo(3)
            .jsonPath("$['first'][1]").isEqualTo(4)
            .jsonPath("$['second']").isEqualTo("123us")

        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(9995, 9994).toStringAsQueryParam())
                    .queryParam("secondCollection", listOf(9999, 9998, 9997, 9996).toStringAsQueryParam()).build()
            }
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
        whenever(
            serviceMock.computeIntersection(eq(listOf(9999, 8888, 7777, 6666)), eq(listOf(1111, 2222))),
        ).thenReturn(Pair(setOf(5, 6), "987us"))

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

        webTestClient
            .post().uri("/api/setintersection/complex")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(Pair(listOf(1111, 2222), listOf(9999, 8888, 7777, 6666)))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first'].length()").isEqualTo(2)
            .jsonPath("$['first'][0]").isEqualTo(5)
            .jsonPath("$['first'][1]").isEqualTo(6)
            .jsonPath("$['second']").isEqualTo("987us")
    }

    @Test
    fun testSetIntersectionWithBadRequest() {
        val emptyList: List<Int> = listOf()
        webTestClient
            .get().uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", emptyList.toStringAsQueryParam())
                    .queryParam("secondCollection", listOf(9995, 9994).toStringAsQueryParam()).build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()

        webTestClient
            .get().uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(9995, 9994))
                    .queryParam("secondCollection", emptyList.toStringAsQueryParam()).build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    fun testSetIntersectionWithEmptyFirstCollection() {
        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    // firstCollection is intentionally not passed
                    .queryParam("secondCollection", listOf(9995, 9994).toStringAsQueryParam()).build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()

        webTestClient
            .get().uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(9995, 9994))
                    // secondCollection is intentionally not passed
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    fun testRandomGeneration() {
        whenever(serviceMock.getRandomIntegerList(eq(9999L))).thenReturn(listOf(1, 2, 3, 4, 5))

        val bodyContentSpec =
            webTestClient
                .get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/randomlist")
                        .queryParam("size", 9999L)
                        .build()
                }
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(5)

        IntRange(0, 4).forEach { bodyContentSpec.jsonPath("$[$it]").isEqualTo(it + 1) }
    }

    @Test
    fun testRandomGenerationWithBadRequest() {
        webTestClient
            .get().uri { uriBuilder ->
                uriBuilder
                    .path("/api/randomlist")
                    .queryParam("size", -5)
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    fun testRandomGenerationWithEmptyParam() {
        webTestClient
            .get().uri { uriBuilder ->
                uriBuilder
                    .path("/api/randomlist")
                    // queryParam has not been set
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()
    }

    private fun defaultBase64EncodedCredential(): String =
        Base64
            .getEncoder()
            .encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

    private fun List<Int>.toStringAsQueryParam(): String {
        return this.toString().replace(" ", "").replace("[", "").replace("]", "")
    }
}
