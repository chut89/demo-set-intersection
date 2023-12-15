package com.example.config

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig(private val springdocProperties: SpringdocProperties) {
    @Bean
    @Suppress("SpreadOperator")
    fun demoSetIntersectionOpenApi(): GroupedOpenApi {
        println("Sprpingdoc version is ${springdocProperties.version}")
        println("spring-doc.swagger-ui.use-root-path is ${springdocProperties.swaggerUi?.useRootPath}")

        var paths = arrayOf("/api/**")
        return GroupedOpenApi.builder().group("api")
            .addOpenApiCustomizer {
                    openApi ->
                openApi
                    .info(
                        Info()
                            .title("Demo Set Intersection API")
                            .version(springdocProperties.version),
                    )
            }
            .pathsToMatch(*paths)
            .build()
    }
}
