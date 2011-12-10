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
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 *
 * @author Vláďa
 */
public class GitProtocolRepository extends AbstractUpdatableRepository {

    private boolean repoUpdated = false;
    
    protected HashMap<String, ArrayList<SerendipityFileInfo>> filelists = new HashMap<String, ArrayList<SerendipityFileInfo>>();

    @Override
    public void updateFiles(String folderPath, String language) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRemoteURL(String remoteURL) {
        
    }

    @Override
    public String getRemoteURL() {
        return "";
    }

    @Override
    public void updateFileList(String folderPath) {
        try {
            ArrayList<SerendipityFileInfo> filelist = new ArrayList<SerendipityFileInfo>();
            
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(getRepositoryFolderName()+"/.git"))
                                            .readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build();

            // update repository if not updated yet
            if (!repoUpdated) {
                Git git = new Git(repository);
                PullCommand pull = git.pull();
                pull.setTimeout(10);
                PullResult pr = pull.call();
                System.out.println(pr.getFetchedFrom());
                System.out.println(pr.toString());
                repoUpdated = true;
            }
            
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
            //System.out.println("Number of trees (commits): "+tree.getTreeCount());

            // find the folder
            Pattern p = Pattern.compile("/");
            for(String folder: p.split(folderPath)) {
                while (tree.next() && !tree.getNameString().equals(folder)) {
                    //System.out.println("folder = "+folder+"; treeitem = "+tree.getPathString());                    
                }
                if (tree.getNameString().equals(folder)) {
                    tree.enterSubtree();
                }
            }
            
            // go through required tree and find dates of last commit for each file
            tree.setPostOrderTraversal(true);
            while (tree.next() && !tree.isPostChildren()) {
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
            
            filelists.put(folderPath, filelist);
            repository.close();
        } catch (WrongRepositoryStateException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DetachedHeadException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRemoteException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CanceledException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RefNotFoundException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JGitInternalException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GitProtocolRepository.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
}
