package com.example.setintersection

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions

import reactor.core.publisher.Mono

import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept

import org.springframework.web.reactive.function.client.WebClient

import org.springframework.web.reactive.function.server.coRouter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.enums.ParameterIn
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations

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


class Employee {

  constructor(id: String, name: String) {
    this.id = id
    this.name = name
  }
  var id: String
  get
  set

  var name: String
  get
  set
}

@Component
class GreetingHandler {

  suspend fun hello(request: ServerRequest): ServerResponse {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(Greeting("Hello from reactive REST end point"))
  }
}

@Component
class EmployeeHandler {
  suspend fun findEmployeeById(request: ServerRequest): ServerResponse {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(Employee("112233", "John Doe"))
  }
}

@Configuration(proxyBeanMethods = false)
class GreetingRouter {
  @Bean
  // It's very IMPORTANT that without path specified in the individual RouterOperation annotation the documentation for both paths won't show
  @RouterOperations(
    // It's so annoying to accept that In Kotlin an annotation as a parameter of another annotation should not start with @
    // Note that 'in' is a keyword in Kotlin     
    RouterOperation(path = "/employee/{id}", operation = Operation(operationId = "findEmployeeById", summary = "Find purchase order by ID", tags = [ "MyEmployee" ], 
      parameters = [ Parameter(`in` = ParameterIn.PATH, name = "id", description = "Employee Id") ],
      responses = [ ApiResponse(responseCode = "200", description = "successful operation", content = [Content(schema = Schema(implementation = Employee::class))]),
            ApiResponse(responseCode = "400", description = "Invalid Employee ID supplied"),
            ApiResponse(responseCode = "404", description = "Employee not found") ])),
    RouterOperation(path = "/hello", operation = Operation(operationId = "hello", summary = "a simple greeting message", description = "a greeting message", tags = [ "MyGreetingMessage" ],
        responses = [ ApiResponse(responseCode = "200", description = "a successful greeting message"),
                      ApiResponse(responseCode = "404", description = "not found message")  
                    ]
    ))
  )
  fun route(greetingHandler: GreetingHandler, employeeHandler: EmployeeHandler): RouterFunction<ServerResponse> = coRouter {
        GET("/employee/{id}", accept(MediaType.APPLICATION_JSON), employeeHandler::findEmployeeById)
        GET("/hello", greetingHandler::hello)
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
