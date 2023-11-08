package com.example.setintersection

import kotlin.time.Duration

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api/setintersection")
class SetIntersectionController(private val setIntersectionService: SetIntersectionService) {

    @Operation(summary = "Computes intersection of two sets when they are so large that must be put into request body", description = "Returns matching elements of the two sets")
    @ApiResponses(
      value = [
          ApiResponse(responseCode = "200", description = "Successful computation", content = [Content(
                  mediaType = "application/json",
                  schema = Schema(implementation = Pair::class, example = "Pair([1, 2, 3, 4, 5], 135 ms)"),
                )]),
          ApiResponse(responseCode = "400", description = "Bad request, check for example encoding, escape of the two sets as parameters"),
      ]
    )
    @PostMapping("/complex", consumes = ["application/json"])
    //@ResponseBody
    fun setIntersection(
            @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Two sets in form of kotlin Pair",
                content = [Content(
                  mediaType = "application/json",
                  schema = Schema(implementation = Pair::class, example="Pair([1, 2, 3], [2, 3, 4])"),
                )]            
            )
            pairOfSets: Pair<List<Int>, List<Int> >): Pair<Set<Int>, String> {
        val (firstCollection, secondCollection) = pairOfSets
        return setIntersectionService.computeIntersection(firstCollection, secondCollection)
    }
    
    @Operation(summary = "Computes intersection of two sets when they are small in size where the URI does not exceeds 8kB (2048 characters)", description = "Returns matching elements of the two sets")    
    @ApiResponses(
      value = [
          ApiResponse(responseCode = "200", description = "Successful computation", content = [Content(
                  mediaType = "application/json",
                  schema = Schema(implementation = Pair::class, example = "Pair([1, 2, 3, 4, 5], 135 ms)"),
                )]),
          ApiResponse(responseCode = "400", description = "Bad request, check for example encoding, escape of the two sets as parameters"),
      ]
    )
    @GetMapping("/simple")
    fun setIntersectionDefault(
            @RequestParam firstCollection: List<Int>, 
            @RequestParam secondCollection: List<Int>): Pair<Set<Int>, String> {
        return setIntersectionService.computeIntersection(firstCollection, secondCollection)
    }
    
}

