package com.example.setintersection

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.reactive.server.WebTestClient

import org.mockito.kotlin.mock

import com.example.setintersection.SetIntersectionService

@TestConfiguration
class TestOverridenConfiguration {

    @Bean
    //@Primary
    fun mockSetIntersectionService(): SetIntersectionService = mock<SetIntersectionService>()
    
    @Bean
    fun mockWebTestClient(): WebTestClient = mock<WebTestClient>()
}