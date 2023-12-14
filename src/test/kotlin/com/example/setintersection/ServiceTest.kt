package com.example.setintersection

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(
    classes = [ SetIntersectionService::class ],
)
@EnableAutoConfiguration
class ServiceTest {
    @Autowired
    lateinit var testee: SetIntersectionService

    @Test
    fun `Test computation of set intersection`() {
        val (intersection, processingTime) = testee.computeIntersection(listOf(1, 2, 3, 4), listOf(3, 4))
        Assertions.assertEquals(setOf(3, 4), intersection)
        // processingTime can be xxxms or xxxus
        Assertions.assertTrue(
            java.lang.Double.parseDouble(
                processingTime.substring(
                    0,
                    processingTime.lastIndexOf("s") - 2,
                ),
            ) > 0.0,
            "Processing time should be greater than 0!!!",
        )

        val (otherIntersection, otherProcessingTime) = testee.computeIntersection(listOf(1, 2, 3), listOf(2, 3, 4))
        Assertions.assertEquals(setOf(2, 3), otherIntersection)
        // processingTime can be xxxms or xxxus
        Assertions.assertTrue(
            java.lang.Double.parseDouble(
                otherProcessingTime.substring(
                    0,
                    otherProcessingTime.lastIndexOf("s") - 2,
                ),
            ) > 0.0,
            "Processing time should be greater than 0!!!",
        )
    }

    @Test
    fun `Test computation of set intersection when input list is empty`() {
        val expected: Pair<Set<Int>, String> = Pair(setOf(), "0 us")
        Assertions.assertEquals(expected, testee.computeIntersection(listOf(5, 6), listOf()))
    }

    @Test
    fun `Assert that AssertionError is thrown if parameters are not passed in right order`() {
        Assertions.assertThrows(java.lang.AssertionError::class.java, {
            testee.computeIntersection(
                listOf(1, 2),
                listOf(1, 2, 3),
            )
        })
        val expected: Pair<Set<Int>, String> = Pair(setOf(), "0 us")
        Assertions.assertThrows(java.lang.AssertionError::class.java, {
            Assertions.assertEquals(
                expected,
                testee.computeIntersection(
                    listOf(),
                    listOf(5, 6),
                ),
            )
        })
    }

    @Test
    fun `Test random list generator`() {
        val list = testee.getRandomIntegerList(3)
        Assertions.assertEquals(3, list.size)
        IntRange(0, 2).forEach { Assertions.assertTrue(list.get(it) > 0) }
    }
}
