package com.example.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("springdoc")
data class SpringdocProperties(val version: String, val swaggerUi: SwaggerUi?) {
    data class SwaggerUi(val useRootPath: Boolean?)
}
