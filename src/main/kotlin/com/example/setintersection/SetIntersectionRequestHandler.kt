package com.example.setintersection

import org.springframework.context.annotation.Bean

import org.springframework.http.MediaType
import org.springframework.stereotype.Component

import kotlinx.coroutines.reactor.awaitSingle

import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class SetIntersectionRequestHandler(private val setIntersectionService: SetIntersectionService) {

    suspend fun setIntersection(request: ServerRequest): ServerResponse {
        val (firstCollection, secondCollection) = 
            request.body(BodyExtractors.toMono(Pair::class.java)).awaitSingle() 
                as Pair<List<Int>, List<Int> >
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)        
                .bodyValueAndAwait(setIntersectionService.computeIntersection(firstCollection, secondCollection))
    }
    
    suspend fun setIntersectionDefault(request: ServerRequest): ServerResponse {
        /*
        In case queryParams were passed as firstCollection=1&firstCollection=2&... in the URL we would read them in as follows    
        val firstCollection: List<Int>? = request.queryParams().get("firstCollection")?.stream()?.map{ it.toInt(10) }?.toList()
        val secondCollection: List<Int>? = request.queryParams().get("secondCollection")?.stream()?.map{ it.toInt(10) }?.toList()
        We enforce the queryParams to be of format firstCollection=1,2&secondCollection=2,3
        */
        val firstCollection = request.queryParam("firstCollection")?.get()?.split(",")?.stream()?.map{ it.toInt(10) }?.toList()
        val secondCollection = request.queryParam("secondCollection")?.get()?.split(",")?.stream()?.map{ it.toInt(10) }?.toList()
        
        if (firstCollection == null || firstCollection.isEmpty() || secondCollection == null || secondCollection.isEmpty()) {
            return ServerResponse.badRequest().buildAndAwait()
        }
        
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(setIntersectionService.computeIntersection(firstCollection, secondCollection))
    }
}
