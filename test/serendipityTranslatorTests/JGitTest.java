/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipityTranslatorTests;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Vláďa
 */
public class JGitTest {
    
    public JGitTest() {
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
    public void jGitTest1() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File("c:/Cvs/php-blog/Serendipity/.git"))
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();
            Git git = new Git(repository);
            PullCommand pull = git.pull();
            TextProgressMonitor monitor = new TextProgressMonitor();
            monitor.start(1);
            monitor.beginTask("Pull git task", 1);
            pull.setProgressMonitor(monitor);
            pull.setTimeout(10);
            PullResult pr = pull.call();
            System.out.println(pr.getFetchedFrom());
            System.out.println(pr.toString());
            assertTrue(pr.isSuccessful());
            
        } catch (WrongRepositoryStateException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DetachedHeadException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRemoteException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CanceledException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RefNotFoundException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
