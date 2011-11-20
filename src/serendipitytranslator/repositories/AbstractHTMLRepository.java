/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import ajgl.utils.ajglTools;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 *
 * @author Vláďa
 */
public abstract class AbstractHTMLRepository extends AbstractUpdatableRepository {

    private String remoteUrl;
    protected HashMap<String, ArrayList<SerendipityFileInfo>> filelists = new HashMap<String, ArrayList<SerendipityFileInfo>>();
    
    private String getRemoteRepositoryFolder (String folderPath, boolean checkout) {
        String folder = getRemoteURL() + "/" + folderPath;
        if (checkout) {
            try {
                return modifyToCheckoutLink(folder);
            } catch (TranslatorRepositoryException ex) {
                Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
                return folder;
            }
        } else {
            return folder;
        }
    }

    private boolean isDocumentationFile(String filename, String language) {
        if (filename.equals("documentation_en.html")
                || filename.equals("documentation_"+language+".html")
                || Plugin.isDocReadme(filename)) {
            return true;
        } else {
            return false;
        }
    }

    private long getFileDate(String folderPath, String filename) {
        long date = (new Date()).getTime();
        for (SerendipityFileInfo file: getRemoteFileList(folderPath)) {
            if (file.getFilename().equals(filename)) {
                return file.getFileDate();
            }
        }
        return date;
    }

    private boolean isInFileList(ArrayList<SerendipityFileInfo> filelist, String filename) {
        for (SerendipityFileInfo file: filelist) {
            if (file.getFilename().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public void updateFiles(String folderPath, String language) {
        InputStream is = null;
        FileOutputStream fos = null;
        long remoteFileDate = 0;

        //System.out.println("************* " + name + ": download started ***************");

        try {
            String dirname = getRepositoryFolderName() + "/"+folderPath;
            //String UTFdirname = dirname+"/UTF-8";

            File directory = new File(dirname);
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            // download english file
            String enFileName = getEnFilename(folderPath);

            File enFile = new File(dirname+"/"+enFileName);
            boolean downloadEnFile = isInFileList(getRemoteFileList(folderPath),enFileName);
            // delete local english file to prevent remaining english files
            // that were removed from serendipity project
            if (enFile.exists()) {
                // check the age of downloaded file
                // if it is older than the one that should be downloaded, delete old file
                
                if (!downloadEnFile) {
                    //it means that remote file does not exist -> we will delete local
                    enFile.delete();
                    //System.out.println(name + " - english file deleted");
                } else {
                    // must check the lastModified date of local and remote file
                    long localFileDate = enFile.lastModified();
                    remoteFileDate = getFileDate(folderPath, enFileName);
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate >= remoteFileDate) {
                        // local file is the newest one, no need to download file
                        downloadEnFile = false;
                        enFile.setLastModified(remoteFileDate);
                    } else {
                        enFile.delete();
                        //System.out.println(name + " - english file deleted");
                    }                    
                }
            } else if (downloadEnFile) {
                remoteFileDate = getFileDate(folderPath, enFileName);
            }

            if (downloadEnFile) {
                ajglTools.download(new URL(getRemoteRepositoryFolder(folderPath,true)+"/"+enFileName), enFile, remoteFileDate);
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: english file downloaded successfully.", folderPath);
            } else {
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: english file will NOT be downloaded.", folderPath);
            }


            String csFileName = getCsFilename(folderPath, language);
            // download local (non-english) language file
            URL csUrl= new URL(getRemoteRepositoryFolder(folderPath,true)+"/"+csFileName);
            File csFile = new File(dirname+"/"+csFileName);
            boolean downloadCsFile = isInFileList(getRemoteFileList(folderPath),csFileName);

            // delete local non-english file to prevent remaining files
            // that were removed from serendipity project
            if (csFile.exists()) {
                // check the age of downloaded file
                // if it is older than the one that should be downloaded, delete old file

                if (!downloadCsFile) {
                    //it means that remote file does not exist -> we will delete local
                    csFile.delete();
                    //System.out.println(name + " - local file deleted");
                } else {
                    // must check the lastModified date of local and remote file
                    long localFileDate = csFile.lastModified();
                    remoteFileDate = getFileDate(folderPath, csFileName);
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate >= remoteFileDate) {
                        // local file is the newest, no need to download file
                        downloadCsFile = false;
                        csFile.setLastModified(remoteFileDate);
                    } else {
                        csFile.delete();
                        //System.out.println(name + " - local file deleted");
                    }
                }
            } else if (downloadCsFile) {
                remoteFileDate = getFileDate(folderPath, csFileName);
            }

            if (downloadCsFile) {
                ajglTools.download(csUrl, csFile, remoteFileDate);
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: local file downloaded successfully.", folderPath);
            } else {
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: local file will NOT be downloaded.", folderPath);
            }

            for (SerendipityFileInfo fileName: getRemoteFileList(folderPath)) {
                if (isDocumentationFile(fileName.getFilename(), language)) {
                    File docFile = new File(dirname+"/"+fileName.getFilename());
                    long localFileDate = docFile.lastModified();
                    remoteFileDate = getFileDate(folderPath, fileName.getFilename());
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate < remoteFileDate) {
                        // newer file on server, must be downloaded
                        URL docUrl= new URL(getRemoteRepositoryFolder(folderPath,true)+"/"+fileName.getFilename());
                        ajglTools.download(docUrl, docFile, remoteFileDate);
                        Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: documentation file ({1}) downloaded successfully.", new Object[]{folderPath, fileName.getFilename()});
                    } else {
                        //System.out.println(name + " - local file deleted");
                        docFile.setLastModified(remoteFileDate);
                        Logger.getLogger(PluginList.class.getName()).log(Level.INFO, "{0}: documentation file ({1}) is up to date, NO downloading.", new Object[]{folderPath, fileName.getFilename()});
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(PluginList.class.getName());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setRemoteURL(String remoteUrl){
        this.remoteUrl = remoteUrl;
    }

    @Override
    public String getRemoteURL() {
        return remoteUrl;
    }

    public String modifyToCheckoutLink(String urlString)  throws TranslatorRepositoryException{
        throw new TranslatorRepositoryException("Repository does not update filelist from web");
    }

    protected boolean followsInReader(InputStream is, String text) throws IOException {
        for (int pos = 0;  pos < text.length(); pos++) {
            if (is.read() != text.codePointAt(pos)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<SerendipityFileInfo> getRemoteFileList(String folderPath) {
        if (!filelists.containsKey(folderPath)) {
            updateFileList(folderPath);
        }
        return filelists.get(folderPath);
    }
    
}
