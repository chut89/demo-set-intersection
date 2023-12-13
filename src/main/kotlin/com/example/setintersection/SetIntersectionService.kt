package com.example.setintersection

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeSource
import kotlin.random.Random

import org.springframework.stereotype.Service
import org.apache.logging.log4j.kotlin.Logging

import java.util.stream.IntStream

@Service
class SetIntersectionService {
    companion object : Logging
    
    val MIN = 0
    val MAX = 1000
    
    // TODO: consider returning Flow<Object> in which the last element is computation time in String
    fun computeIntersection(firstList: List<Int>, secondList: List<Int>): Pair<Set<Int>, String> {
        assert(secondList.size <= firstList.size)
        logger.info{"Computation starts!"}
        if (secondList.isEmpty()) {
            return Pair(setOf(), "0 us")
        }

        val timeSource = TimeSource.Monotonic
        val markBefore = timeSource.markNow()
        
        val secondSet = secondList.toHashSet()
        
        var result: Set<Int> = emptySet()
        for (candidate in firstList.toSet()) {
            if (secondSet.contains(candidate)) {
                result += candidate
            }

        }
        
        val markAfter = timeSource.markNow()
        //result.forEach{ element -> logger.debug{"$element, "}}
        
        logger.info{"Computation ends!"}
        logger.debug{"duration=${markAfter - markBefore}, there are ${result.size} elements in common"}
        return Pair(result, (markAfter - markBefore).toString())
    }
    
    fun getRandomIntegerList(size: Long): List<Int> {
        return IntStream.generate{ Math.abs(Random.nextInt(MIN, MAX)) }.limit(size).boxed().toList()
    }
}
