package com.namoadigital.prj001

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test


class RegexTest {

    val default = "UM SERIAL NORMAL"
    val with_char_valid = "UM SERIAL NORMAL [] + /|\\ _ - () ."
    val char_invalid = " SPACE BEFORE, '', ' ' \"\", \"  \",     \t\n" +
            "\t & "
    val text_char_invalid = " CHAR  VALIDO"

    @Test
    fun `Verificar Regex sem char especial`() {
        assertFalse(default, checkIfHasCharInvalid(default))
    }

    @Test
    fun `Verificar Regex com char especial valido`() {
        assertFalse(with_char_valid, checkIfHasCharInvalid(with_char_valid))
    }

    @Test
    fun `Verificar Regex com char especial invalido`() {
        assertTrue(char_invalid, checkIfHasCharInvalid(char_invalid))
    }

    @Test
    fun `Verificar Regex com char invalido`() {
        assertTrue(text_char_invalid, checkIfHasCharInvalid(text_char_invalid))
    }


    fun checkIfHasCharInvalid(value: String): Boolean {
        val regexPattern = Regex(
            """^\s|\s$|\s{2}|[\t\n\r]|[^\s0-9a-zà-ü\-\_\(\)\[\]\{\}\.\|\/\+\\\ª\º]""",
            setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )
        return regexPattern.containsMatchIn(value)
    }


}