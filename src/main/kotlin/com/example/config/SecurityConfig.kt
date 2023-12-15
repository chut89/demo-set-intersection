package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke // without this kotlin won't compile
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
// import org.springframework.security.web.csrf.CookieCsrfTokenRepository

/* Important: For spring to be able to scan and find the beans, this Configuration class must be nested in a subfolder to
DemoSetIntersectionApplication.kt
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            // TODO("Use frontend and open network traffic to see how X-XSRF-TOKEN header and co. are included")
            // cors { }
            httpBasic {}
            authorizeExchange {
                authorize("/api/setintersection/**", hasAuthority("ROLE_USER"))
                authorize("/**", permitAll)
            }
            csrf {
                // csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
                disable()
            }
        }
    }

    @Bean
    fun userDetailsService(): ReactiveUserDetailsService {
        val encoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val userDetails =
            User
                .withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build()
        return MapReactiveUserDetailsService(userDetails)
    }
}
