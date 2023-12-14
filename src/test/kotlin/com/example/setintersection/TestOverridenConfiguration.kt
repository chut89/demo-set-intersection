package com.example.setintersection

import org.mockito.kotlin.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient

@TestConfiguration
class TestOverridenConfiguration {
    @Bean
    fun mockSetIntersectionService(): SetIntersectionService = mock<SetIntersectionService>()

    @Bean
    fun mockWebTestClient(): WebTestClient = mock<WebTestClient>()
}
