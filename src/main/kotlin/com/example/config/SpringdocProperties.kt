package com.example.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("springdoc")
// declare var instead of val inside constructor expression, otherwise Springboot may have problem creating String bean for 'version'
data class SpringdocProperties(var version: String = "", var swaggerUi: SwaggerUi? = null) {
    data class SwaggerUi(var useRootPath: Boolean? = true)
}
