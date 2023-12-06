package com.example.setintersection

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

import org.mockito.kotlin.mock

import com.example.setintersection.SetIntersectionService

@TestConfiguration
class TestOverridenConfiguration {

    @Bean
    //@Primary
    fun mockSetIntersectionService(): SetIntersectionService = mock<SetIntersectionService>()
}
