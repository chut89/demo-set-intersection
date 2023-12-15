package com.example.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("springdoc")
/* declare var instead of val inside constructor expression, otherwise Springboot will have problem creating String bean for 'version'
in test environment even when @ConstructorBinding works on the fly since Springboot 3
 */
data class SpringdocProperties(var version: String = "", var swaggerUi: SwaggerUi? = null) {
    data class SwaggerUi(var useRootPath: Boolean? = true)
}
