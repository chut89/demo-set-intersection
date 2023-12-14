package com.example

import com.example.config.OpenApiConfig
import com.example.config.SecurityConfig
import com.example.config.SpringdocProperties
import com.example.setintersection.SetIntersectionRequestHandler
import com.example.setintersection.SetIntersectionRouter
import com.example.setintersection.SetIntersectionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets
import java.util.Base64

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    classes = [
        SetIntersectionRequestHandler::class,
        SetIntersectionRouter::class,
        SetIntersectionService::class,
        SecurityConfig::class, // without this SecurityFilterChain cannot be loaded and therefore every page is prohibited!!!
        OpenApiConfig::class,
        SpringdocProperties::class,
    ],
)
@EnableAutoConfiguration
class IntegrationTests {
    lateinit var webTestClient: WebTestClient

    @LocalServerPort
    lateinit var port: Integer

    @BeforeEach
    fun setup() {
        webTestClient =
            WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port/")
                .defaultHeader("Authorization", "Basic ${defaultBase64EncodedCredential()}")
                .build()
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun `Test setintersection simple case when first collection is null`() {
        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(1, 2, 3, 4))
                    .queryParam("secondCollection", listOf(3, 4).toStringAsQueryParam())
                    .build()
            }
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first']").isEmpty()
    }

    @Test
    fun `Test setintersection simple case with bad request parameter`() {
        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    // firstCollection is missing here
                    .queryParam("secondCollection", listOf(3, 4).toStringAsQueryParam())
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()

        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    // secondCollection is missing here
                    .queryParam("firstCollection", listOf(3, 4).toStringAsQueryParam())
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Assert set intersection is returned when sets' sizes are small`() {
        webTestClient
            // Create a GET request to test an endpoint
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", listOf(1, 2, 3, 4).toStringAsQueryParam())
                    .queryParam("secondCollection", listOf(3, 4).toStringAsQueryParam())
                    .build()
            }
            .exchange()
            // and use the dedicated DSL to test assertions against the response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first'].length()").isEqualTo(2)
            .jsonPath("$['first'][0]").isEqualTo(3)
            .jsonPath("$['first'][1]").isEqualTo(4)
            .jsonPath("$['second']").isNotEmpty()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Assert set intersection is returned when sets' sizes are big`() {
        webTestClient
            // Create a POST request to test an endpoint
            .post()
            .uri("/api/setintersection/complex")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
            .exchange()
            // and use the dedicated DSL to test assertions against the response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first'].length()").isEqualTo(2)
            .jsonPath(
                "$['first'][0]",
            ).isEqualTo(3)
            .jsonPath("$['first'][1]").isEqualTo(4)
            .jsonPath("$['second']").isNotEmpty()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Assert set intersection is returned with heavy loaded input`() {
        webTestClient
            // Create a POST request to test an endpoint
            .post()
            .uri("/api/setintersection/complex")
            .bodyValue(
                Pair(
                    listOf(
                        55, 534, 12, 349, 473, 78, 26, 374, 599, 75, 719, 40, 204,
                        149, 234, 102, 845, 182, 223, 592, 195, 623, 43, 496, 969, 747, 751, 414, 546, 239, 170, 942, 635, 643,
                    ),
                    listOf(
                        363, 616, 800, 411, 585, 883, 803, 4, 605, 257, 252, 169, 249, 533, 517, 801, 699, 895, 368, 5, 595, 890, 373,
                        141, 267, 129, 220, 562, 854, 219, 812, 599, 768, 212, 888, 452, 798, 315, 859, 558, 12, 700, 640, 204, 913,
                        893, 706, 480, 383, 740, 463, 573, 776, 466, 110, 49, 648, 699, 338, 824, 477, 715, 443, 482, 955, 435, 388,
                        45, 66, 829, 907, 517, 328, 91, 67, 394, 495, 363, 957, 834, 369, 830, 410, 472, 572, 347, 518, 66, 953, 77,
                        33, 292, 362, 608, 598, 359, 944, 78, 21, 936,
                    ),
                ),
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            // and use the dedicated DSL to test assertions against the response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$['first'].length()").isEqualTo(4) // to verify the intersection has 4 elements
            .jsonPath("$['first'][?(@ in [12,78,599,204])]").isNotEmpty() // to verify its elements
            .jsonPath("$['second']").isNotEmpty()
    }

    @Test
    fun `Test random list generator with bad request parameter`() {
        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/randomlist")
                    .queryParam("size", "-5")
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    fun `Test random list generator without request parameter`() {
        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/randomlist")
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest()
    }

    @Test
    fun `Assert that random controller returns list of integer`() {
        val bodyContentSpec =
            webTestClient
                .get()
                .uri { builder ->
                    builder
                        .path("/api/randomlist")
                        .queryParam("size", "5")
                        .build()
                }
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(5)

        IntRange(0, 4).forEach { bodyContentSpec.jsonPath("$[$it]").isNumber() }
    }

    @Test
    fun `Test random then SetIntersection`() {
        val firstRandomList: MutableList<Int> = mutableListOf()
        var bodyContentSpec =
            webTestClient
                .get()
                .uri { builder ->
                    builder
                        .path("/api/randomlist")
                        .queryParam("size", "5")
                        .build()
                }
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(5)

        IntRange(0, 4).forEach {
            bodyContentSpec
                .jsonPath("$[$it]")
                .isNumber()
                .jsonPath("$[$it]")
                .value({ num: Int -> firstRandomList.add(num) })
        }

        val secondRandomList: MutableList<Int> = mutableListOf()
        bodyContentSpec =
            webTestClient
                .get()
                .uri { builder ->
                    builder
                        .path("/api/randomlist")
                        .queryParam("size", "15")
                        .build()
                }
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(15)

        IntRange(0, 14).forEach {
            bodyContentSpec
                .jsonPath("$[$it]")
                .isNumber()
                .jsonPath("$[$it]")
                .value({ num: Int ->
                    secondRandomList.add(num)
                })
        }

        webTestClient
            .get()
            .uri { builder ->
                builder
                    .path("/api/setintersection/simple")
                    .queryParam("firstCollection", firstRandomList.toStringAsQueryParam())
                    .queryParam("secondCollection", secondRandomList.toStringAsQueryParam())
                    .build()
            }
            .exchange()
            .expectStatus().isOk()

        webTestClient
            .post()
            .uri("/api/setintersection/complex")
            .bodyValue(
                Pair(firstRandomList, secondRandomList),
            )
            .exchange()
            .expectStatus().isOk()
    }

    @Test
    fun `Assert Unauthorized when request is provided without identity credential`() {
        WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port/")
            // without provided Base64 encoded credential in header
            .build()
            .post()
            .uri("/api/setintersection/complex")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
            .exchange()
            // and use the dedicated DSL to test assertions against the response
            .expectStatus().isUnauthorized()
    }

    @Test
    fun `Assert Unauthorized with invalid password`() {
        WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port/")
            // without provided Base64 encoded credential in header
            .build()
            .post()
            .uri("/api/setintersection/complex")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(Pair(listOf(1, 2, 3, 4), listOf(3, 4)))
            .exchange()
            // and use the dedicated DSL to test assertions against the response
            .expectStatus().isUnauthorized()
    }

    @Test
    fun `Assert that Springdoc has been generated`() {
        WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port/")
            .build()
            .get()
            .uri("/swagger-ui.html")
            .exchange()
            .expectStatus().isFound() // it will be redirected to localhost:$port/webjars/swagger-ui/index.html
    }

    private fun defaultBase64EncodedCredential(): String =
        Base64
            .getEncoder()
            .encodeToString("user:password".toByteArray(StandardCharsets.UTF_8))

    private fun List<Int>.toStringAsQueryParam(): String {
        return this.toString().replace(" ", "").replace("[", "").replace("]", "")
    }
}
