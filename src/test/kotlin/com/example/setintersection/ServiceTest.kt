package com.example.setintersection

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SetIntersectionService::class])
@EnableAutoConfiguration
class ServiceTest {

    @Autowired
    lateinit var testee: SetIntersectionService
	
	@Test
	fun `Test computation of set intersection`() {
	    val (intersection, processingTime) = testee.computeIntersection(listOf(1, 2, 3, 4), listOf(3, 4))
	    Assertions.assertEquals(setOf(3, 4), intersection)
	    Assertions.assertTrue(java.lang.Double.parseDouble(processingTime.substring(0, processingTime.lastIndexOf("s") - 2)) > 0.0, // processingTime can be xxxms or xxxus
	        "Processing time should be greater than 0!!!")
	}
	
	@Test
	fun `Test computation of set intersection when input list is empty`() {
	    var expected: Pair<Set<Int>, String> = Pair(setOf(), "0 us")
	    Assertions.assertEquals(expected, testee.computeIntersection(listOf(5, 6), listOf()))
	}
	
	@Test
	fun `Assert that AssertionError is thrown`() {
	    Assertions.assertThrows(java.lang.AssertionError::class.java, { 
	        testee.computeIntersection(listOf(1, 2), listOf(1, 2, 3)) 
	    })
	}
	
	@Test
	fun `Test random list generator`() {
	    val list = testee.getRandomIntegerList(3)
	    Assertions.assertEquals(3, list.size)
	    IntRange(0, 2).forEach{ Assertions.assertTrue(list.get(it) > 0) }
	}

}
