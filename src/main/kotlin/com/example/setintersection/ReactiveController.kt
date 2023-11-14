package com.example.setintersection

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions

import reactor.core.publisher.Mono

import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept

import org.springframework.web.reactive.function.client.WebClient

class Greeting {
  var message: String = "" 
  get 
  set
    
  constructor (message: String) {
      this.message = message
  }  

  override fun toString(): String {
    return "Greeting{" +
        "message='" + message + '\'' +
        '}'
  }
}

@Component
class GreetingHandler {

  fun hello(request: ServerRequest): Mono<ServerResponse> {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Greeting("Hello from reactive REST end point"))
  }
}

@Configuration(proxyBeanMethods = false)
class GreetingRouter {

  @Bean
  fun route(greetingHandler: GreetingHandler): RouterFunction<ServerResponse> {

    return RouterFunctions
      .route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello);
  }
}

@Component
class GreetingClient {
  private var client: WebClient = 
     WebClient.builder().baseUrl("http://localhost:8080").build();

  fun getMessage(): Mono<String> {
    return this.client.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(Greeting::class.java)
        .map(Greeting::message::get)
  }

}
