package com.namoadigital.prj001

import com.namoadigital.prj001.extensions.roundOffDecimal
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.service.location.util.calculateDistance
import com.namoadigital.prj001.service.location.util.calculateDistanceHaversine
import com.namoadigital.prj001.service.location.util.calculateSpeed
import junit.framework.TestCase
import org.junit.Test
import java.util.Locale




class DistanceTest {

    @Test
    fun `parse number`() {
        val number = 0.01
        Locale.setDefault(Locale.GERMAN)
        val roundOffDecimal = roundOffDecimal(number)
        TestCase.assertEquals(true, roundOffDecimal == 0.01)
    }
     @Test
    fun `calculate speed`() {
        val initial = Coordinates(-23.6985216,-46.5527647, "2024-03-15 18:26:39 -03:00")
        val current = Coordinates(-23.7009049, -46.5527253, "2024-03-15 18:27:40 -03:00")
        val calculateSpeed = calculateSpeed(current, initial)
        println("calculateSpeed: $calculateSpeed")
        TestCase.assertEquals(true, calculateSpeed >= 1.0)
    }
    @Test
    fun `calculate distance`() {
        val initial = Coordinates(-23.624815,-46.5804767)
        val current = Coordinates(-23.624765,-46.5795517)
        val calculateDistance = calculateDistance(current,initial)
        System.out.println("calculateDistance: $calculateDistance")
        TestCase.assertEquals(true, calculateDistance >= 0.09)
    }
    @Test
    fun `calculate Haversine distance`() {
        val initial = Coordinates(-23.624815,-46.5804767)
        val current = Coordinates(-23.624765,-46.5795517)
        val calculateDistanceHaversine = calculateDistanceHaversine(current,initial)
        System.out.println("calculateDistance: $calculateDistanceHaversine")

        TestCase.assertEquals(true, calculateDistanceHaversine >= 0.09)
    }

}