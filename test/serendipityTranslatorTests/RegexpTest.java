/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipityTranslatorTests;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vláďa
 */
public class RegexpTest {

    public RegexpTest() {
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
    public void serverUrlSplitTest() {
        String testUrl = "http://php-blog.cvs.sourceforge.net/viewvc/php-blog/additional_plugins";

        Pattern p = Pattern.compile("(https?://[^/]*)/(.*)/([^/]*)");
        //Pattern p = Pattern.compile("[^/]*");
        //Pattern p = Pattern.compile(".*(sourceforge).*");
        Matcher m = p.matcher(testUrl);
        System.out.println("matches = " + m.matches());
        System.out.println("group count = " + m.groupCount());

        assertEquals("http://php-blog.cvs.sourceforge.net",m.group(1));
        assertEquals("viewvc/php-blog",m.group(2));
        assertEquals("additional_plugins",m.group(3));

    }

    @Test
    public void gitUrlSplitTest() {
        String testUrl = "https://github.com/s9y/Serendipity/tree/3d4e3e92c00f635853adfd196a6b67186e0523cc/plugins";

        Pattern p = Pattern.compile("(https?://[^/]*)/(.*)/([^/]*)");
        //Pattern p = Pattern.compile("[^/]*");
        //Pattern p = Pattern.compile(".*(sourceforge).*");
        Matcher m = p.matcher(testUrl);
        System.out.println("matches = " + m.matches());
        System.out.println("group count = " + m.groupCount());

        assertEquals("https://github.com",m.group(1));
        assertEquals("s9y/Serendipity/tree/3d4e3e92c00f635853adfd196a6b67186e0523cc",m.group(2));
        assertEquals("plugins",m.group(3));

    }

}