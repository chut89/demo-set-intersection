package com.example.setintersection

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Service
import java.util.stream.IntStream
import kotlin.random.Random
import kotlin.time.TimeSource

@Service
class SetIntersectionService {
    companion object {
        val logger: Any = logger()
        const val MIN = 0
        const val MAX = 1000
    }

    fun computeIntersection(
        firstList: List<Int>,
        secondList: List<Int>,
    ): Pair<Set<Int>, String> {
        assert(secondList.size <= firstList.size)
        logger.info { "Computation starts!" }
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
        // result.forEach{ element -> logger.debug{"$element, "}}

        logger.info { "Computation ends!" }
        logger.debug { "duration=${markAfter - markBefore}, there are ${result.size} elements in common" }
        return Pair(result, (markAfter - markBefore).toString())
    }

    fun getRandomIntegerList(size: Long): List<Int> {
        return IntStream.generate { Math.abs(Random.nextInt(MIN, MAX)) }.limit(size).boxed().toList()
    }
}
