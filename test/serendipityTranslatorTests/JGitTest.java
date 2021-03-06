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
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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
    
    /*
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
    */
    /*
    @Test
    public void jGitTest2() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File("c:/Cvs/php-blog/Serendipity/.git"))
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();
          ObjectId revId = repository.resolve(Constants.HEAD);
          //DirCache cache = new DirCache(directory, FS.DETECTED);
          TreeWalk treeWalk = new TreeWalk(repository);

          treeWalk.addTree(new RevWalk(repository).parseTree(revId));
          //treeWalk.addTree(new DirCacheIterator(cache));

          while (treeWalk.next())
          {
            System.out.println("---------------------------");
            System.out.append("name: ").println(treeWalk.getNameString());
            System.out.append("path: ").println(treeWalk.getPathString());

            ObjectLoader loader = repository.open(treeWalk.getObjectId(0));

            System.out.append("directory: ").println(loader.getType()
                              == Constants.OBJ_TREE);
            System.out.append("size: ").println(loader.getSize());
            // ???
            System.out.append("last modified: ").println("???");
            System.out.append("message: ").println("???");            
        
            }
        } catch (IOException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    /*
    @Test
    public void jGitTest3() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File("c:/Cvs/php-blog/Serendipity/.git"))
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();
            ObjectId headId = repository.resolve(Constants.HEAD);
            Git git = new Git(repository);
            LogCommand log = git.log();
            log.addPath("lang/serendipity_lang_cs.inc.php");
            //ObjectId fileId = repository.resolve("refs/heads/master/lang/serendipity_lang_cs.inc.php");
            System.out.println(headId);
            //log.addRange(headId, headId);
            RevWalk commits = (RevWalk) log.call();
            int i = 0;
            for (RevCommit c: commits) {
                SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date d = new Date(c.getCommitTime()*1000l);
                
                System.out.println(date.format(d) + ": " +c.getShortMessage());
                if (i++ >=10) {
                    break;
                }
            }
            
            
        } catch (NoHeadException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JGitInternalException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void jGitTest4() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File("c:/Cvs/php-blog/Serendipity/.git"))
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();
            ObjectId headId = repository.resolve(Constants.HEAD);
            RevWalk walk = new RevWalk(repository);
            walk.markStart(walk.parseCommit(headId));
            walk.setTreeFilter(AndTreeFilter.create(
                                PathFilter.create("lang/serendipity_lang_cs.inc.php"),
                                TreeFilter.ANY_DIFF));
            Iterator<RevCommit> it = walk.iterator();
            System.out.println("has next = " + it.hasNext());
            if (it.hasNext()) {
                RevCommit lastCommit = it.next();
                SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date d = new Date(lastCommit.getCommitTime()*1000l);
                System.out.println(date.format(d) + ": " +lastCommit.getShortMessage());                
            }
            
        } catch (JGitInternalException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void jGitTest5() {
        GitProtocolRepository gitRepo = new GitProtocolRepository();
        
        gitRepo.setRepositoryFolderName("C:/Cvs/php-blog/Serendipity");
        String localFolder = "plugins/serendipity_event_karma";
        gitRepo.updateFileList(localFolder);
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date d;
        
        ArrayList<SerendipityFileInfo> filelist = gitRepo.getFileList(localFolder);
        for (SerendipityFileInfo info: filelist) {
            d = new Date(info.getFileDate());
            System.out.println(info.getFilename() + ": " + date.format(d));
        }
    }
    */

    @Test
    public void jGitTest6() {
        try {
//            String repoFolderName = "C:/Cvs/php-blog/pluginsTryout";
            String repoFolderName = "pluginsTryout";
            String remoteString = "git://github.com/s9y/Serendipity.git";
            File workingFolder = new File(repoFolderName);
            File gitFolder = new File(repoFolderName + "/.git");
            deleteRecursive(workingFolder);
            if (!gitFolder.exists()) {
                if (!workingFolder.exists()) {
                    workingFolder.mkdirs();
                }
                InitCommand initCommand = Git.init();
                initCommand.setDirectory(workingFolder);
                Git git = initCommand.call();

                StoredConfig config = git.getRepository().getConfig();
                config.setString("remote", "origin", "url", remoteString);
                config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
                config.setString("branch", "master", "remote", "origin");
                config.setString("branch", "master", "merge", "refs/heads/master");
                config.save();
                
                for (String remoteName : config.getSubsections("remote")) {
                    for (String name: config.getNames("remote", remoteName)) {
                        String value = config.getString("remote", remoteName, name);
                        System.out.println(remoteName + ": " + name + " = " + value);
                    }
                }
                PullCommand pull = git.pull();
                TextProgressMonitor monitor = new TextProgressMonitor();
                monitor.start(100);
                pull.setProgressMonitor(monitor);
                PullResult pr = pull.call();
                System.out.println(pr.getFetchedFrom());
                System.out.println(pr.toString());
            }
            
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(gitFolder)
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();
            System.out.println(repository.getFullBranch());
            repository.close();
        } catch (NoHeadException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongRepositoryStateException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DetachedHeadException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CanceledException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RefNotFoundException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRemoteException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JGitInternalException ex) {
            System.out.println(ex.getCause().getMessage());
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JGitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deleteRecursive(File folder) {
        if (folder.exists()) {
            for(File f:folder.listFiles()) {
                if (f.isDirectory()) {
                    deleteRecursive(f);
                } else {
                    f.delete();
                }
            }
            folder.delete();
        }
    }
}
