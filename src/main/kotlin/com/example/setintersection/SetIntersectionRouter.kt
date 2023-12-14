package com.example.setintersection

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SetIntersectionRouter {
    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/api/setintersection/complex",
            operation =
                Operation(
                    operationId = "setintersectionPost",
                    summary =
                        "Computes intersection of two sets " +
                            "when they are so large that must be put into request body",
                    description = "Returns matching elements of the two sets",
                    tags = [ "Set Intersection complex" ],
                    requestBody =
                        RequestBody(
                            description = "Two sets in form of kotlin Pair",
                            content = [
                                Content(
                                    mediaType = "application/json",
                                    schema = Schema(example = "{ \"first\":[1, 2, 3], \"second\":[2, 3, 4] }"),
                                ),
                            ],
                        ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            description = "Successful computation",
                            content = [
                                Content(
                                    mediaType = "application/json",
                                    schema = Schema(example = "{ \"first\":[1, 2, 3, 4, 5], \"second\": 135ms }"),
                                ),
                            ],
                        ),
                        ApiResponse(
                            responseCode = "400",
                            description = "Bad request, check for example encoding, escape of the two sets as parameters",
                        ),
                    ],
                ),
        ),
        RouterOperation(
            path = "/api/setintersection/simple",
            operation =
                Operation(
                    operationId = "setintersectionGet",
                    summary =
                        "Computes intersection of two sets " +
                            "when they are small in size where the URI does not exceeds 8kB (2048 characters)",
                    description = "Returns matching elements of the two sets",
                    tags = [ "Set Intersection simple" ],
                    parameters = [
                        Parameter(`in` = ParameterIn.QUERY, name = "firstCollection", description = "First input collection"),
                        Parameter(`in` = ParameterIn.QUERY, name = "secondCollection", description = "Second input collection"),
                    ],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            description = "Successful computation",
                            content = [
                                Content(
                                    mediaType = "application/json",
                                    schema =
                                        Schema(
                                            implementation = Pair::class,
                                            example = "{ \"first\":[1, 2, 3, 4, 5], \"second\": 135ms}",
                                        ),
                                ),
                            ],
                        ),
                    ],
                ),
        ),
        RouterOperation(
            path = "/api/randomlist",
            operation =
                Operation(
                    operationId = "randomlist",
                    summary = "Returns a randomly generated list of integers",
                    description =
                        "Returns a randomly generated list of integers given specified size",
                    tags = [ "Random List" ],
                    parameters = [ Parameter(`in` = ParameterIn.QUERY, name = "size", description = "Size of a wish list") ],
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            description = "successfully generated list of Integers",
                            content = [
                                Content(
                                    mediaType = "application/json",
                                    schema = Schema(example = "[1, 2, 3, 4, 5]"),
                                ),
                            ],
                        ),
                    ],
                ),
        ),
    )
    fun route(setIntersectionRequestHandler: SetIntersectionRequestHandler): RouterFunction<ServerResponse> {
        return coRouter {
            "/api/setintersection".nest {
                POST("/complex", accept(MediaType.APPLICATION_JSON), setIntersectionRequestHandler::setIntersection)
                GET("/simple", setIntersectionRequestHandler::setIntersectionDefault)
            }
        }.and(
            coRouter {
                GET("/api/randomlist", setIntersectionRequestHandler::getRandomIntegerList)
            },
        )
    }
}
