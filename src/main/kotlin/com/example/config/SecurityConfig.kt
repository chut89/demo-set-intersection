package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke // without this kotlin won't compile
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.provisioning.InMemoryUserDetailsManager
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository
/*
    Important: For spring to be able to scan and find the beans, this Configuration class must be nested in a subfolder to DemoSetIntersectionApplication.kt
*/
@Configuration
@EnableWebSecurity
class SecurityConfig {
    
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            //TODO("Use frontend and open network traffic to see how X-XSRF-TOKEN header and co. are included")                   
            //cors { }
            httpBasic {}
            authorizeRequests {
                authorize("/api/setintersection/**", hasAuthority("ROLE_USER"))
                authorize("/**", permitAll)                            
            }
            csrf {
                //csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
                disable()
            }            
        }
        return http.build()
    }
    
    @Bean
    fun userDetailsService(): UserDetailsService {
        val encoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val userDetails = User
                .withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(userDetails)
    }
}
