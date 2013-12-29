/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.PluginType;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 *
 * @author Vláďa
 */
public class GitProtocolRepository extends AbstractUpdatableRepository {

    private boolean repoUpdated = false;
    private String remoteUrl;
    
    protected HashMap<String, ArrayList<SerendipityFileInfo>> filelists = new HashMap<>();

    @Override
    public void updateFiles(String folderPath, String language) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRemoteURL(String remoteURL) {
        this.remoteUrl = remoteURL;
    }

    @Override
    public String getRemoteURL() {
        return remoteUrl;
    }

    @Override
    public void updateFileList(String folderPath) {
        try {
            //System.out.println("updating filelist for "+folderPath);
            ArrayList<SerendipityFileInfo> filelist = new ArrayList<>();
            
            File gitFolder = new File(getRepositoryFolderName() + "/.git");

            updateRepository();
            
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(gitFolder)
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();

            ObjectId headId = repository.resolve(Constants.HEAD);
            RevWalk walk = new RevWalk(repository);
            walk.markStart(walk.parseCommit(headId));
            TreeWalk tree = new TreeWalk(repository);

            // go through all commits, collect their dates and add all trees to walker
            Iterator<RevCommit> it = walk.iterator();
            int i = 0;
            boolean missing;
            int firstMissing;
            int j = 0;
            int size = 10000;
            long[] commitDates = new long[size]; 
            while (it.hasNext() && (i++ < size)) {
                RevCommit lastCommit = it.next();
                commitDates[i-1] = lastCommit.getCommitTime()*1000l;
                tree.addTree(lastCommit.getTree());
            }
            if (i<size) {
                size = i;
            }
            tree.setPostOrderTraversal(true);
            //System.out.println("Number of trees (commits): "+tree.getTreeCount());

            // find the folder
            Pattern p = Pattern.compile("/");
            String[] splittedPath = p.split(folderPath);
            if (splittedPath.length >0 && splittedPath[0].length()>0) {
                for(String folder: p.split(folderPath)) {
                    while (tree.next() && !tree.getNameString().equals(folder)) {
                        //System.out.println("folder = "+folder+"; treeitem = "+tree.getPathString());                    
                    }
                    if (tree.getNameString().equals(folder)) {
                        tree.enterSubtree();
                    }
                }
            }
            
            // go through required tree and find dates of last commit for each file
            while (tree.next() && !tree.isPostChildren()) {
                if (tree.getFileMode(0) != FileMode.MISSING) {
                    //System.out.println("tree "+tree.getPathString()+"; postchildren = "+tree.isPostChildren());
                    firstMissing = 0;
                    missing = false;
                    for (i = 1; i < size ; i++) {
                        j = i-1;
                        missing = (tree.getFileMode(i) == FileMode.MISSING);
                        if (missing && firstMissing == 0) {
                            firstMissing = i;
                        }
                        if (firstMissing != 0 && !missing) {
                            firstMissing = 0;
                        }
                        if (!tree.idEqual(0, i) && !missing) {
                            break;
                        }
                    }
                    //System.out.println("i = " + i+ "; tree = " + tree.getPathString() + "; " +tree.isPostChildren()+ "; " +tree.isPostOrderTraversal()+ "; " +tree.isSubtree());
                    if (i==size && missing) {
                        j = firstMissing-1;
                        //System.out.println(tree.getNameString() + " only initial commit");
                    }
                    if (i==size && !missing) {
                        j = size-1;
                        //System.out.println(tree.getNameString() + " set on first commit and then never changed");
                    }

                    //System.out.println(tree.getNameString() + " - "+ j +" - " + tree.idEqual(0, j) + "; "+tree.getObjectId(0)+ "; "+tree.getObjectId(j) + "; "+tree.getFileMode(j));
                    filelist.add(new SerendipityFileInfo(tree.getNameString(),commitDates[j]));
                    //lastCommit.getCommitTime()*1000l
                }
            }
            
            filelists.put(folderPath, filelist);
            repository.close();
        } catch (WrongRepositoryStateException | InvalidConfigurationException | DetachedHeadException | InvalidRemoteException | CanceledException | RefNotFoundException | JGitInternalException | IOException | NoHeadException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<SerendipityFileInfo> getFileList(String folderPath) {
        ArrayList<SerendipityFileInfo> filelist = filelists.get(folderPath);
        if (filelist == null) {
            updateFileList(folderPath);
            filelist = filelists.get(folderPath);
        }
        return (filelist != null) ? filelist : new ArrayList<SerendipityFileInfo>();
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

    private void updateRepository() throws IOException, WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException {
            File workingFolder = new File(getRepositoryFolderName());
            File gitFolder = new File(getRepositoryFolderName() + "/.git");
            if (!gitFolder.exists()) {
                if (!workingFolder.exists()) {
                    workingFolder.mkdirs();
                }
                int filesCount = workingFolder.list().length;
                System.out.println("dir "+workingFolder.getName()+" has "+filesCount+" files");
                if (!workingFolder.exists()) {
                    workingFolder.mkdirs();
                } else if (filesCount > 0) {
                    for (File f: workingFolder.listFiles()) {
                        if (f.isDirectory()) {
                            deleteRecursive(f);
                        } else {
                            f.delete();
                        }
                    }
                }
                InitCommand initCommand = Git.init();
                initCommand.setDirectory(workingFolder);
                Git git = initCommand.call();

                StoredConfig config = git.getRepository().getConfig();
                config.setString("remote", "origin", "url", getRemoteURL());
                config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
                config.setString("branch", "master", "remote", "origin");
                config.setString("branch", "master", "merge", "refs/heads/master");
                config.save();
            }
            
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(gitFolder)
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();

            // update repository if not updated yet
            if (!repoUpdated) {
                Git git = new Git(repository);
                final PullCommand pull = git.pull();
                //pull.setTimeout(10);
                JGitProgressMonitor monitor = new JGitProgressMonitor(getRepositoryFolderName().substring(getRepositoryFolderName().lastIndexOf("/")+1));
                monitor.setPropertyChange(propertyChange);
                pull.setProgressMonitor(monitor);
                System.out.println("Pull will be started for repo " + workingFolder);
                PullResult pr = pull.call();                        
                System.out.println("Pull ended. was successfull? - " +pr.isSuccessful());
                System.out.println("pull result string = " + pr.toString());
                System.out.println("fetched from = " + pr.getFetchedFrom());
                System.out.println("fetch result = " + pr.getFetchResult());
                System.out.println("merge result = " + pr.getMergeResult());
                System.out.println("rebase result = " + pr.getRebaseResult());
                
                repoUpdated = true;
                System.out.println("End of git updateRepository()");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "update of repository {0} finished", getRepositoryFolderName());                
            }
    }

    @Override
    public void loadListOfPlugins(PluginList plugins, String folderPath, String language, boolean isIntern) {
        try {
            System.out.println("loading list of plugins: " + getRepositoryFolderName()+" - "+folderPath);
            updateRepository();
            System.out.println("repository " + getRepositoryFolderName()+" was updated");
            String repoName = getRepositoryFolderName().substring(getRepositoryFolderName().lastIndexOf("/")+1);
            propertyChange.firePropertyChange("progressMax", -1, 0);
            propertyChange.firePropertyChange("progressValue", -1, 0);
            propertyChange.firePropertyChange("progressText", "", repoName + " - reading list of plugins");
            File gitFolder = new File(getRepositoryFolderName() + "/.git");
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(gitFolder)
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();

            ObjectId headId = repository.resolve(Constants.HEAD);
            RevWalk walk = new RevWalk(repository);
            //walk.markStart(walk.parseCommit(headId));
            TreeWalk tree = new TreeWalk(repository);
//            Iterator<RevCommit> it = walk.iterator();
//            while (it.hasNext()) {
//                RevCommit rc = it.next();
//                //System.out.println(rc.getShortMessage());
//                tree.addTree(rc.getTree());                
//            }
            tree.addTree(walk.parseTree(headId));
            tree.setPostOrderTraversal(true);
            tree.next();
            //System.out.println("tree "+tree.getPathString()+";name="+tree.getNameString()+"; depth="+ tree.getDepth() + "; subtree="+tree.isSubtree());
            
            Pattern pat = Pattern.compile("/");
            String[] splittedPath = pat.split(folderPath);
            //System.out.println("splittedPath - length="+splittedPath.length+";contents="+Arrays.toString(splittedPath));
            if (splittedPath.length >0 && splittedPath[0].length()>0) {
                for(String folder: splittedPath) {
                    while (tree.next() && !tree.getNameString().equals(folder)) {
                        //System.out.println("folder = "+folder+"; treeitem = "+tree.getPathString());                    
                    }
                    if (tree.getNameString().equals(folder)) {
                        tree.enterSubtree();
                    }
                }
            }
            //System.out.println("tree "+tree.getPathString()+";name="+tree.getNameString()+"; depth="+ tree.getDepth() + "; subtree="+tree.isSubtree());
            int i = 0;
            while (tree.next() && !tree.isPostChildren()) {
                //System.out.println(tree.getNameString() + "; subtree = "+tree.isSubtree());
                if (tree.isSubtree()) {
                    Plugin p = new Plugin(tree.getNameString(), language);
                    if (p.getName().equals("homepage")) {
                        System.out.println("Homepage plugin: type = " + p.getType() + "; folderPath = "+folderPath);
                    }
                    if (getRemoteURL().contains("plugins") || folderPath.contains("plugins")) {
                        if (p.getType().equals(PluginType.template)) {
                            p.setType(PluginType.event);
                        }
                    }
                    p.setRepository(this);
                    if (folderPath.length()>0) {
                        p.setFolderInRepository(folderPath + "/" + tree.getNameString());
                    } else {
                        p.setFolderInRepository(tree.getNameString());
                    }
                    
                    plugins.add(p);
                    i++;
                    propertyChange.firePropertyChange("progressText", "", repoName + " - " + i + " plugins found.");
                }
            }
        } catch (IOException | WrongRepositoryStateException | InvalidConfigurationException | DetachedHeadException | InvalidRemoteException | CanceledException | RefNotFoundException | NoHeadException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
