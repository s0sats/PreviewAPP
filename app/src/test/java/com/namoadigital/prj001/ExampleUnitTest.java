package com.namoadigital.prj001;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkRegex() throws Exception {
        assertFalse(checkIfHasCharInvalid("aaaaaaaaa123 -_()[]{}.,|/+"));
        assertTrue(checkIfHasCharInvalid(" SPACE BEFORE,  DOUBLE SPACE,  S P A C E  L E T T E R , 'S I M P L E', 'SIMPLE', ' S I M P L E ', '  S  I  M  P  L  E  ', \"S I M P L E\", \"SIMPLE\", \" S I M P L E \", \"  S  I  M  P  L  E  \", TAB -> \t<- TAB, ENTER ->\n" +
                "\t<- ENTER + TAB, & COMERCIAL,      A A   L    G  U N     S                      E   S     P      A      Ç     O   S                <- TEM ALGUNS ESPAÇOS AQUI"));
    }

    //Verifica se há char inválidos na string
    private boolean checkIfHasCharInvalid(String value) {
        return Pattern.compile("/^\\s|\\s$|\\s{2}|[\\t\\n\\r]|[^\\s0-9a-zà-ü\\-\\_\\(\\)\\[\\]\\{\\}\\.\\|\\/\\+\\\\]/gmi").matcher(value).find();
    }
}