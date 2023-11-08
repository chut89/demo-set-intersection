package com.example.setintersection

import kotlin.time.Duration

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api/setintersection")
class SetIntersectionController(private val setIntersectionService: SetIntersectionService) {

    @PostMapping("/complex")
    //@ResponseBody
    fun setIntersection(
            @RequestBody pairOfSets: Pair<List<Int>, List<Int> >): Pair<Set<Int>, String> {
        val (firstCollection, secondCollection) = pairOfSets
        return setIntersectionService.computeIntersection(firstCollection, secondCollection)
    }
    
    @GetMapping("/simple")
    fun setIntersectionDefault(
            @RequestParam firstCollection: List<Int>, 
            @RequestParam secondCollection: List<Int>): Pair<Set<Int>, String> {
        return setIntersectionService.computeIntersection(firstCollection, secondCollection)
    }
    
}

