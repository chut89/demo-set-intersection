package com.example.setintersection

import kotlinx.coroutines.reactor.awaitSingle
import org.apache.logging.log4j.kotlin.logger
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class SetIntersectionRequestHandler(private val setIntersectionService: SetIntersectionService) {
    companion object {
        val logger: Any = logger()
        const val RADIX_TEN = 10
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun setIntersection(request: ServerRequest): ServerResponse {
        val (firstCollection, secondCollection) =
            request.body(BodyExtractors.toMono(Pair::class.java)).awaitSingle()
                as Pair<List<Int>, List<Int>>

        val innerList: List<Int> = if (firstCollection.size <= secondCollection.size) firstCollection else secondCollection
        val outerList: List<Int> = if (firstCollection.size <= secondCollection.size) secondCollection else firstCollection

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(setIntersectionService.computeIntersection(outerList, innerList))
    }

    suspend fun setIntersectionDefault(request: ServerRequest): ServerResponse {
        /*
        In case queryParams were passed as firstCollection=1&firstCollection=2&... in the URL we would read them in as follows
        val firstCollection: List<Int>? = request.queryParams().get("firstCollection")?.stream()?.map{ it.toInt(10) }?.toList()
        val secondCollection: List<Int>? = request.queryParams().get("secondCollection")?.stream()?.map{ it.toInt(10) }?.toList()
        We enforce the queryParams to be of format firstCollection=1,2&secondCollection=2,3
         */
        var firstCollection: List<Int>? = request.queryParam("firstCollection").orElse(null)?.toIntegerList()
        val secondCollection = request.queryParam("secondCollection").orElse(null)?.toIntegerList()

        if (firstCollection == null || secondCollection == null) {
            return ServerResponse.badRequest().buildAndAwait()
        }

        val innerList: List<Int> = if (firstCollection.size <= secondCollection.size) firstCollection else secondCollection
        val outerList: List<Int> = if (firstCollection.size <= secondCollection.size) secondCollection else firstCollection
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(setIntersectionService.computeIntersection(outerList, innerList))
    }

    suspend fun getRandomIntegerList(request: ServerRequest): ServerResponse {
        val size: Long? = request.queryParam("size").orElse(null)?.toLong()

        if (size == null || size < 0) {
            return ServerResponse.badRequest().buildAndAwait()
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(setIntersectionService.getRandomIntegerList(size))
    }

    private fun String.toIntegerList(): List<Int>? {
        try {
            return this.split(",").stream().map { it.toInt(RADIX_TEN) }.toList()
        } catch (e: NumberFormatException) {
            logger.error { "Exception occurred parsing Integer value from request parameter" }
            return null
        }
    }
}
