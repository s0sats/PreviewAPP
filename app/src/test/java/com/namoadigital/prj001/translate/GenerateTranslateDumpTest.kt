package com.namoadigital.prj001.translate

import com.namoadigital.prj001.core.translate.TranslateKey
import com.namoadigital.prj001.core.translate.TranslateWildCard
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

/**
 * Gera um TXT contendo:
 * - Todas as keys do enum separadas por PIPE (sem espaço)
 * - Mapa dos placeholders
 * - Cabeçalho com resource e timestamp
 *
 * @param enumClass Classe do enum que implementa TranslateWildCard
 * @param output Arquivo onde salvar o dump
 */
object TranslateDumpGenerator {

    fun <T> generate(
        enumClass: KClass<T>,
        output: File
    ) where T : Enum<T>, T : TranslateKey {

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val values = enumClass.java.enumConstants

        val pipeString = values
            .joinToString("|") { it.key }

        // Placeholder map (somente quando existir)
        val placeholderLines = values
            .filterIsInstance<TranslateWildCard>()
            .filter { it.placeholders.isNotEmpty() }
            .joinToString("\n") { entry ->
                "${entry.key} -> ${entry.placeholders.joinToString(",")}"
            }

        val content = buildString {
            appendLine("Resource: ${enumClass.simpleName}")
            appendLine("Generated at: $timestamp")
            appendLine("----------------------------------------")
            appendLine()
            appendLine(pipeString)
            appendLine()
            if (placeholderLines.isNotEmpty()) {
                appendLine(placeholderLines)
            }
        }

        output.parentFile?.mkdirs()
        output.writeText(content)
    }
}
