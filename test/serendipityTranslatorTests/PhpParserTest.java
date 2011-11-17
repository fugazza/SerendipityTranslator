/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipityTranslatorTests;

import java.io.StringReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import serendipitytranslator.translationWindow.PhpParser;


/**
 *
 * @author Vláďa
 */
public class PhpParserTest {

    public PhpParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void phpParserTest() {
        String testString = "<?php //my comment\n" +
                "    @define('SOME_KEY',\t\t'value with\\' character');\n" +
                "    $var = fct(a,b);\n" +
                " /** and some multiline\n" +
                "   * commented\n" +
                "   * text\n" +
                "   ***/";

        PhpParser pars = new PhpParser(new StringReader(testString));

        assertTrue(pars.hasMoreTokens());
        assertEquals("//my comment",pars.nextToken());
        assertEquals("@define('SOME_KEY',\t\t'value with\\' character');",pars.nextToken());
        assertEquals("$var = fct(a,b);",pars.nextToken());
        assertEquals("/** and some multiline\n" +
                "   * commented\n" +
                "   * text\n" +
                "   ***/",pars.nextToken());
        assertFalse(pars.hasMoreTokens());

    }

    @Test
    public void phpParserTest2() {
        String testString = "<?php //my comment\n" +
                "    @define('SOME_KEY',\t\t'value with\\' character' ." +
                "\n SOME_CONSTANT . \"\\n and multiline statement\");\n";

        PhpParser pars = new PhpParser(new StringReader(testString));

        assertTrue(pars.hasMoreTokens());
        assertEquals("//my comment",pars.nextToken());
        assertEquals("@define('SOME_KEY',\t\t'value with\\' character' ." +
                "\n SOME_CONSTANT . \"\\n and multiline statement\");",pars.nextToken());
        assertFalse(pars.hasMoreTokens());
    }

    @Test
    public void phpParserTest3() {
        String testString = "<?php \n" +
                "    @define('SOME_KEY',\t\t'value with\\' character'); //commented in the same line\n" +
                "    $var = fct(a,b);\n";

        PhpParser pars = new PhpParser(new StringReader(testString));

        assertTrue(pars.hasMoreTokens());
        assertEquals("@define('SOME_KEY',\t\t'value with\\' character');",pars.nextToken());
        assertEquals("//commented in the same line",pars.nextToken());
        assertEquals("$var = fct(a,b);",pars.nextToken());
        assertFalse(pars.hasMoreTokens());

    }

    @Test
    public void phpParserTest4() {
        String testString = "<?php \n" +
                "    @define('SOME_KEY',\t\t'value with\\' character'); //commented in the same line\n" +
                "    /* next line will be blank */\n\n$var = fct(a,b);\n";

        PhpParser pars = new PhpParser(new StringReader(testString));

        assertTrue(pars.hasMoreTokens());
        assertEquals("@define('SOME_KEY',\t\t'value with\\' character');",pars.nextToken());
        assertEquals("//commented in the same line",pars.nextToken());
        assertEquals("/* next line will be blank */",pars.nextToken());
        assertEquals("",pars.nextToken());
        assertEquals("$var = fct(a,b);",pars.nextToken());
        assertFalse(pars.hasMoreTokens());

    }

    @Test
    public void phpParserTest5() {
        String testString = "<?php # $Id: lang_en.inc.php,v 1.1 2007/06/11 10:26:26 garvinhicking Exp $\n" +
                "@define('NAV_LINK_TEXT', 'Navbar link');\n" +
                "@define('NAV_LINK_DESC', 'Enter the navbar link text');";

        PhpParser pars = new PhpParser(new StringReader(testString));

        assertTrue(pars.hasMoreTokens());
        assertEquals("// $Id: lang_en.inc.php,v 1.1 2007/06/11 10:26:26 garvinhicking Exp $",pars.nextToken());
        assertEquals("@define('NAV_LINK_TEXT', 'Navbar link');",pars.nextToken());
        assertEquals("@define('NAV_LINK_DESC', 'Enter the navbar link text');",pars.nextToken());
        assertFalse(pars.hasMoreTokens());

    }

}