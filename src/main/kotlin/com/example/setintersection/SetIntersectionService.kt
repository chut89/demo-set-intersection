package com.example.setintersection

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

import org.springframework.stereotype.Service
import io.github.oshai.kotlinlogging.KotlinLogging

@Service
class SetIntersectionService {
    private val logger = KotlinLogging.logger {}
    
    // TODO: consider returning Flow<Object> in which the last element is computation time in String
    fun computeIntersection(firstList: List<Int>, secondList: List<Int>): Pair<Set<Int>, String> {
        logger.info{"Computation starts!"}
        //if (first.isNotEmpty() && first is java.util.Set<*> && first is kotlin.collections.Set<*>) 
        //    return setOf(5,4,3,2,1)
        //val innerSet: Set<Int> = if (first.size <= second.size) first else second
        //val outerSet: Set<Int> = if (first.size <= second.size) second else first
        val secondSet = secondList.toHashSet()
        
        val timeSource = TimeSource.Monotonic
        val markBefore = timeSource.markNow()
        
        var result: Set<Int> = emptySet()
        for (candidate in firstList.toSet()) {
            if (secondSet.contains(candidate)) {
                result += candidate
            }

        }
        
        val markAfter = timeSource.markNow()
        result.forEach{ element -> logger.info{"$element, "}}
        
        logger.info{"Computation ends!"}
        logger.debug{"duration=${markAfter - markBefore}"}
        return Pair(result, (markAfter - markBefore).toString())
    }
}
