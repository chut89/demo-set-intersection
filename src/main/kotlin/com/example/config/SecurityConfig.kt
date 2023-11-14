package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
//@EnableWebFluxSecurity
class SecurityConfig {
    
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            csrf {
                //csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
                disable()
            }
            authorizeRequests {
                authorize(AntPathRequestMatcher("/api/setintersection/**", HttpMethod.GET.name()), hasAuthority("ROLE_USER"))
                authorize(AntPathRequestMatcher("/api/setintersection/**", HttpMethod.POST.name()), permitAll)
                authorize("/**", permitAll)                
            }
            httpBasic {}
        }
        
        return http.build()
    }
    
    fun filterChainWithCSRFEnabled(http: HttpSecurity): SecurityFilterChain {
        TODO("Use frontend and open network traffic to see how X-XSRF-TOKEN header and co. are included")        
        http {
            csrf {
                csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
            }
            cors { }        
        }
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
