package com.example.config

import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("springdoc")
// declare var instead of val inside constructor expression, otherwise Springboot may have problem creating String bean for 'version'
data class SpringdocProperties @ConstructorBinding constructor(val version: String, val swaggerUi: SwaggerUi) {
    data class SwaggerUi @ConstructorBinding constructor(val useRootPath: Boolean?) {}
}
