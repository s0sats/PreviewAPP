package com.namoadigital.prj001

import com.namoadigital.prj001.util.ToolBox_Inf.calculateDaysPassed
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DateTest {


    @Test
    fun `has passed 10 days`() {
        val inputDate = "2023-07-30 09:22:40"
        val hasPassed = calculateDaysPassed(inputDate)

        assertEquals(true, hasPassed >= 10)
    }

    @Test
    fun `has passed 15 days`() {
        val inputDate = "2023-07-25 12:00:00"
        val hasPassed = calculateDaysPassed(inputDate)

        assertEquals(true, hasPassed >= 15)
    }


}