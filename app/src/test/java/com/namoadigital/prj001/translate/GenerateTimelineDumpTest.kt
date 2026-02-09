package com.namoadigital.prj001.translate

import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import org.junit.Test
import java.io.File

class GenerateTimelineDumpTest {

    @Test
    fun generateFile() {
        val output = File("build/timeline_translates_dump.txt")

        TranslateDumpGenerator.generate(
            enumClass = TimelineBlockTranslate::class,
            output = output
        )

        println("Gerado em: ${output.absolutePath}")
    }
}
