package com.example.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi

@Configuration
class OpenApiConfig(private val springdocProperties: SpringdocProperties) {
    @Bean
    fun demoSetIntersectionOpenApi(): GroupedOpenApi {
        println("Sprpingdoc version is ${springdocProperties.version}")
        println("spring-doc.swagger-ui.use-root-path is ${springdocProperties.swaggerUi?.useRootPath}")
        
	    var paths = arrayOf("/api/**", "/employee/**", "/hello/**")
	    return GroupedOpenApi.builder().group("api")
			    .addOpenApiCustomizer{ openApi -> openApi.info(Info().title("Demo Set Intersection API").version(springdocProperties.version)) }
			    .pathsToMatch(*paths)
			    .build()
    }
}
