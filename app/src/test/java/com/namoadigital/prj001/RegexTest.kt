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

    @Test
    fun `Verificar se no final do texto contém TAB`() {
        assertTrue("TAB\t", `check if contain ENTER or TAB on end string`("TAB\t"))
    }

    @Test
    fun `Verificar se no final do texto contém ENTER`() {
        assertTrue("ENTER\n", `check if contain ENTER or TAB on end string`("ENTER\n"))
    }

    @Test
    fun `Verificar se no final do texto não contém TAB`() {
        assertFalse("T\tAB ", `check if contain ENTER or TAB on end string`("T\tAB "))
    }

    @Test
    fun `Verificar se no final do texto não contém ENTER`() {
        assertFalse("EN\nTER ", `check if contain ENTER or TAB on end string`("EN\nTER "))
    }

    fun checkIfHasCharInvalid(value: String): Boolean {
        val regexPattern = Regex(
            """^\s|\s$|\s{2}|[\t\n\r]|[^\s0-9a-zà-ü\-\_\(\)\[\]\{\}\.\|\/\+\\\ª\º]""",
            setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )
        return regexPattern.containsMatchIn(value)
    }


    fun `check if contain ENTER or TAB on end string`(value: String): Boolean {
        Regex(
            """[ \t\n\r]*([\t\n\r])${'$'}""",
            setOf(RegexOption.COMMENTS, RegexOption.DOT_MATCHES_ALL)
        ).let { regex ->
            return regex.containsMatchIn(value)
        }
    }


}