/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import ajgl.utils.ajglTools;
import serendipitytranslator.translationWindow.LangFile;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import serendipitytranslator.webDownloaders.CvsDownloader;
import serendipitytranslator.webDownloaders.GitDownloader;
import serendipitytranslator.webDownloaders.SvnDownloader;
import serendipitytranslator.webDownloaders.WebDownloader;

/**
 *
 * @author Vláďa
 */
public class Plugin implements Comparator {
    private String name;
    private boolean intern = false;
    private PluginType type = PluginType.event;
    private PluginStatus CVSstatus = PluginStatus.no;
    private PluginStatus localStatus = PluginStatus.no;
    private DocumentationStatus documentationStatus = DocumentationStatus.problem;
    private String pluginVersion;
    private String langFileVersion;
    private Hashtable<String,String> messageDatabase = null;
    private int enCount = 0;
    private int locCount = 0;
    private int translatedLocCount = 0;
    private Vector<SerendipityFileInfo> filelist = new Vector<SerendipityFileInfo>();
    private String repositoryType;
    private String repositoryFolderUrl;

    private String language;

    PropertyChangeSupport propertyChange;

    private static String[] interns = {
        "serendipity_event_bbcode",
        "serendipity_event_browsercompatibility",
        "serendipity_event_contentrewrite",
        "serendipity_event_creativecommons",
         "serendipity_event_emoticate",
         "serendipity_event_entryproperties",
         "serendipity_event_karma",
         "serendipity_event_livesearch",
         "serendipity_event_mailer",
         "serendipity_event_nl2br",
         "serendipity_event_s9ymarkup",
         "serendipity_event_searchhighlight",
         "serendipity_event_spamblock",
         "serendipity_event_spartacus",
         "serendipity_event_statistics",
         "serendipity_event_templatechooser",
         "serendipity_event_textile",
         "serendipity_event_textwiki",
         "serendipity_event_trackexits",
         "serendipity_event_weblogping",
         "serendipity_event_xhtmlcleanup",
         "serendipity_plugin_comments",
         "serendipity_plugin_creativecommons",
         "serendipity_plugin_entrylinks",
         "serendipity_plugin_eventwrapper",
         "serendipity_plugin_history",
         "serendipity_plugin_recententries",
         "serendipity_plugin_remoterss",
         "serendipity_plugin_shoutbox",
         "serendipity_plugin_templatedropdown",
         "blue",
         "bulletproof",
         "carl_contest",
         "competition",
         "contest",
         "default",
         "default-php",
         "default-rtl",
         "default-xml",
         "idea",
         "kubrick",
         "moz-modern",
         "mt-clean",
         "mt-georgiablue",
         "mt-gettysburg",
         "mt-plainjane",
         "mt-rusty",
         "mt-trendy",
         "mt3-chalkboard",
         "mt3-gettysburg",
         "mt3-independence",
         "mt3-squash",
         "newspaper",
         "wp"
    };

    public Plugin (String name) {
        this.name = name;
        this.propertyChange = new PropertyChangeSupport(this);
        if (name.startsWith("serendipity_plugin")) {
            setType(PluginType.sidebar);
        } else if (name.startsWith("serendipity_event")) {
            setType(PluginType.event);
        } else if (name.equals("system")) {
            //System.out.println("Have a system plugin");
            setType(PluginType.system);
        } else {
            setType(PluginType.template);
        }
        //System.out.println("plugin "+ name);

        intern = false;
        if (type.equals(PluginType.system)) {
            //System.out.println("System plugin IS intern.");
            intern = true;
        }
        for (String n: interns) {
            if (n.equals(name)) {
                intern = true;
            }
        }

        loadFileList();
    }

    public Plugin(String name, String language) {
        this(name);
        this.language = language;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    
    public String getName() {
        return name;
    }

    public PluginStatus getStatus() {
        return CVSstatus;
    }

    public PluginStatus getLocalStatus() {
        return localStatus;
    }

    public DocumentationStatus getDocumentationStatus() {
        return documentationStatus;
    }

    public PluginType getType() {
        return type;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public String getLangFileVersion() {
        return langFileVersion;
    }

    public boolean isIntern() {
        return intern;
    }

    public void setIntern(boolean intern) {
        this.intern = intern;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(PluginStatus status) {
        this.CVSstatus = status;
    }

    public void setLocalStatus(PluginStatus status) {
        this.localStatus = status;
    }

    public void setDocumentationStatus(DocumentationStatus documentationStatus) {
        this.documentationStatus = documentationStatus;
    }

    public void setType(PluginType type) {
        this.type = type;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public void setLangFileVersion(String langFileVersion) {
        this.langFileVersion = langFileVersion;
    }

    public static String[] getInterns() {
        return interns;
    }

    private String getRepositoryFolder (boolean checkout) {
        if (checkout) {
            WebDownloader webDownloader = getDownloader();
            return webDownloader.modifyToCheckoutLink(repositoryFolderUrl);
        } else {
            return repositoryFolderUrl;
        }
    }

    private boolean isDocumentationFile(String filename) {
        if (filename.equals("documentation_en.html")
                || filename.equals("documentation_"+language+".html")
                || isDocReadme(filename)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isDocReadme(String filename) {
        return (filename.toLowerCase().contains("readme")
                    || filename.toLowerCase().contains("changelog")
                    || filename.toLowerCase().contains("credit")
                    || filename.toLowerCase().contains("copy")
                    || filename.toLowerCase().contains("todo")
                    || filename.toLowerCase().contains("author")
                    && !filename.toLowerCase().endsWith(".tpl")
               );
    }

    private long getFileDate(String filename) {
        long date = (new Date()).getTime();
        for (SerendipityFileInfo file: filelist) {
            if (file.getFilename().equals(filename)) {
                return file.getFileDate();
            }
        }
        return date;
    }

    private WebDownloader getDownloader() {
        if (repositoryType.equals("svn")) {
                return new SvnDownloader();
            } else if (repositoryType.equals("git")) {
                return new GitDownloader();
            } else {
                return new CvsDownloader();
            }
    }

    public void downloadFilelist() {
        WebDownloader webDownloader = getDownloader();
        filelist = webDownloader.loadFileList(getRepositoryFolder(false));
    }
    
    public void downloadFiles() {
        InputStream is = null;
        FileOutputStream fos = null;
        long remoteFileDate = 0;

        //System.out.println("************* " + name + ": download started ***************");

        try {
            String dirname = "plugins/"+name;
            //String UTFdirname = dirname+"/UTF-8";

            File directory = new File(dirname);
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            // download english file
            String enFileName;
            if (type.equals(PluginType.system)) {
                enFileName = "serendipity_lang_en.inc.php";
            } else {
                enFileName = "lang_en.inc.php";
            }

            File enFile = new File(dirname+"/"+enFileName);
            boolean downloadEnFile = isInFileList(filelist,enFileName);
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
                    remoteFileDate = getFileDate(enFileName);
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate > remoteFileDate) {
                        // local file is the newest, no need to download file
                        downloadEnFile = false;
                        enFile.setLastModified(remoteFileDate);
                    } else {
                        enFile.delete();
                        //System.out.println(name + " - english file deleted");
                    }                    
                }
            } else if (downloadEnFile) {
                remoteFileDate = getFileDate(enFileName);
            }

            if (downloadEnFile) {
                ajglTools.download(new URL(getRepositoryFolder(true)+"/"+enFileName), enFile, remoteFileDate);
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": english file downloaded successfully.");
            } else {
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": english file will NOT be downloaded.");
            }


            String csFileName;
            // download local (non-english) language file
            if (type.equals(PluginType.system)) {
                csFileName = "serendipity_lang_"+language+".inc.php";
            } else {
                csFileName = "lang_"+language+".inc.php";
            }
            URL csUrl= new URL(getRepositoryFolder(true)+"/"+csFileName);
            File csFile = new File(dirname+"/"+csFileName);
            boolean downloadCsFile = isInFileList(filelist,csFileName);

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
                    remoteFileDate = getFileDate(csFileName);
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate > remoteFileDate) {
                        // local file is the newest, no need to download file
                        downloadCsFile = false;
                        csFile.setLastModified(remoteFileDate);
                    } else {
                        csFile.delete();
                        //System.out.println(name + " - local file deleted");
                    }
                }
            } else if (downloadCsFile) {
                remoteFileDate = getFileDate(csFileName);
            }

            if (downloadCsFile) {
                ajglTools.download(csUrl, csFile, remoteFileDate);
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": local file downloaded successfully.");
            } else {
                Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": local file will NOT be downloaded.");
            }

            for (SerendipityFileInfo fileName: filelist) {
                if (isDocumentationFile(fileName.getFilename())) {
                    File docFile = new File(dirname+"/"+fileName.getFilename());
                    long localFileDate = docFile.lastModified();
                    remoteFileDate = getFileDate(fileName.getFilename());
                    //System.out.println("local: " + localFileDate + "; remote: "+ remoteFileDate + "; date: "+ (new Date()).getTime());
                    if (localFileDate < remoteFileDate) {
                        // newer file on server, must be downloaded
                        URL docUrl= new URL(getRepositoryFolder(true)+"/"+fileName.getFilename());
                        ajglTools.download(docUrl, docFile, remoteFileDate);
                        Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": documentation file ("+fileName.getFilename()+") downloaded successfully.");
                    } else {
                        //System.out.println(name + " - local file deleted");
                        docFile.setLastModified(remoteFileDate);
                        Logger.getLogger(PluginList.class.getName()).log(Level.INFO,name + ": documentation file ("+fileName.getFilename()+") is up to date, NO downloading.");
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
        compareFiles();
    }


    /**
     * Function compares the contents of files in plugin and updates their
     * statuses. Files with language constants are compared.
     * Also Documentation files are compared.
     */
    public void compareFiles() {
        //JOptionPane.showMessageDialog(null, "before downloaded file");
        LangFile downloadedLocFile = new LangFile(name,language);
        //JOptionPane.showMessageDialog(null, "before translated file");
        LangFile translatedLocFile = new LangFile(LangFile.LOCATIONS_TRANSLATED,name,language);
        //JOptionPane.showMessageDialog(null, "before english file");
        LangFile enFile = new LangFile(name,"en");
        //JOptionPane.showMessageDialog(null, "after english file");

        enCount = enFile.getKeysCount();
        locCount = downloadedLocFile.getKeysCount();
        translatedLocCount = translatedLocFile.getKeysCount();

        // ------ set status for downloaded files (the situation on server) ----
        if (!enFile.exists()) {
            langFileVersion = "1.-1";
            setStatus(PluginStatus.problem);
            System.out.println(name + ": Problem, english file not found, maybe no language constants.");
        } else if (!downloadedLocFile.exists()) {
            langFileVersion = "1.-1";
            setStatus(PluginStatus.no);
            System.out.println(name + ": "+language+" language file not found.");
        } else if (downloadedLocFile.hasIdenticKeys(enFile)) {
            langFileVersion = downloadedLocFile.getVersion();
            setStatus(PluginStatus.translated);
            System.out.println(name + ": Files are identic.");
        } else {
            langFileVersion = downloadedLocFile.getVersion();
            setStatus(PluginStatus.partial);
            System.out.println(name + ": Keys not identic. Maybe partially translated.");
        }

        //JOptionPane.showMessageDialog(null, "before local status");
        // --- set status for local files (the situation on local computer) ----
        if (!enFile.exists()) {
            setLocalStatus(PluginStatus.problem);
            //System.out.println(name + ": Problem, english file not found, maybe no language constants.");
        } else if (!translatedLocFile.exists()) {
            setLocalStatus(PluginStatus.no);
            //System.out.println(name + ": translated "+language+" language file not found.");
        } else if (translatedLocFile.hasIdenticKeys(enFile)) {
            setLocalStatus(PluginStatus.translated);
            //System.out.println(name + ": Files are identic.");
        } else {
            setLocalStatus(PluginStatus.partial);
            //System.out.println(name + ": Keys not identic. Maybe partially translated.");
        }

        //JOptionPane.showMessageDialog(null, "before doc status");
        // --------------- set status for documentation files ------------------
        File enDocFile = new File(LangFile.getDownloadDirName(name)+ "/" + getDocFileNameEn());
        File csDocFile = new File(LangFile.getDownloadDirName(name)+ "/" + getDocFileNameLoc());
        File csDocFileLocal = new File(LangFile.getTranslatedDirName(name)+ "/" + getDocFileNameLoc());
        
        if (!enDocFile.exists()) {
            setDocumentationStatus(DocumentationStatus.problem);
        } else if (csDocFile.exists()) {
            if (csDocFile.lastModified() > enDocFile.lastModified()) {
                setDocumentationStatus(DocumentationStatus.translated);
            } else if (csDocFileLocal.exists() && (csDocFileLocal.lastModified() > enDocFile.lastModified())) {
                setDocumentationStatus(DocumentationStatus.local);
            } else {
                setDocumentationStatus(DocumentationStatus.partial);
            }
        } else if (csDocFileLocal.exists()) {
            if (csDocFileLocal.lastModified() > enDocFile.lastModified()) {
                setDocumentationStatus(DocumentationStatus.local);
            } else {
                setDocumentationStatus(DocumentationStatus.partial);
            }
        } else {
            setDocumentationStatus(DocumentationStatus.no);
        }

        // put/update translated messages in database of this messages
        // (This database is used to pre-fill untranslated messages with
        // same message key. Especially templates have often the same
        // keys = messages.)
        if (messageDatabase != null) {
            //System.out.println(name + " - fill message database.");
            if (downloadedLocFile.exists()) {
                for (String key: downloadedLocFile.getKeys()) {
                    messageDatabase.put(key, downloadedLocFile.get(key));
                }
            }
            if (translatedLocFile.exists()) {
                for (String key: translatedLocFile.getKeys()) {
                    messageDatabase.put(key, translatedLocFile.get(key));
                }
            }
        }
        
        // announce the end of files comparaison to listeners
        propertyChange.firePropertyChange("plugin_files_compared", null, this);
   }

    private boolean isInFileList(Vector<SerendipityFileInfo> filelist, String filename) {
        for (SerendipityFileInfo file: filelist) {
            if (file.getFilename().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public int compare(Object plugin1, Object plugin2) {
        if ((plugin1 instanceof Plugin) && (plugin2 instanceof Plugin)) {
            return ((Plugin) plugin1).getName().compareToIgnoreCase(((Plugin) plugin2).getName());
        } else {
            throw new ClassCastException("Both parametres must be of Plugin class.");
        }
    }

    public int getEnCount() {
        return enCount;
    }

    public int getLocCount() {
        return locCount;
    }

    public int getTranslatedLocCount() {
        return translatedLocCount;
    }

    public void setCounts(int enCount, int locCount) {
        this.enCount = enCount;
        this.locCount = locCount;
    }

    public void setMessageDatabase(Hashtable<String, String> messageDatabase) {
        this.messageDatabase = messageDatabase;
    }

    private String getDocFileNameEn() {
        
        String readmeFile = null;
        long newest = 0;
        String filename;
        for (SerendipityFileInfo fileName: filelist) {
            filename = fileName.getFilename();
            if (filename.equals("documentation_en.html")) {
                return "documentation_en.html";
            } else if (isDocReadme(filename)) {
                File enDocFile = new File(LangFile.getDownloadDirName(name)+ "/" + filename);
                if (enDocFile.lastModified() > newest) {
                    readmeFile = filename;
                    newest = enDocFile.lastModified();
                }
            }
        }

        if (readmeFile != null) {
            return readmeFile;
        } else {
            return "doc_not_found.html";
        }
    }

    private String getDocFileNameLoc() {
        return "documentation_"+language+".html";
    }

    private void loadFileList() {
        filelist = new Vector<SerendipityFileInfo> ();

        File downloadDir = new File(LangFile.getDownloadDirName(name));

        if (downloadDir.exists()) {
            for (File f: downloadDir.listFiles()) {
                if (f.isFile()) {
                    filelist.add(new SerendipityFileInfo(f.getName(),f.lastModified()));
                }
            }
        }
    }

    public void createUtfDocumentation() {
        File locDocFile = new File(LangFile.getTranslatedDirName(name)+"/"+getDocFileNameLoc());
        if (locDocFile.exists()) {
            File locDocUtfFile = new File(LangFile.getTranslatedDirName(name)+"/UTF-8/"+getDocFileNameLoc());
            if (!locDocUtfFile.exists()) {
                _encodeDocFile(locDocFile, locDocUtfFile, LangFile.getCharsetName(language), "utf-8");
            }
            if (language.equals("cs")) {
                File czDocFile = new File(LangFile.getTranslatedDirName(name)+"/documentation_cz.html");
                File czDocUtfFile = new File(LangFile.getTranslatedDirName(name)+"/UTF-8/documentation_cz.html");
                if (!czDocFile.exists()) {
                    _encodeDocFile(locDocFile, czDocFile, LangFile.getCharsetName(language), "iso-8859-2");
                    _encodeDocFile(locDocFile, czDocUtfFile, LangFile.getCharsetName(language), "utf-8");
                }
            }
        }
    }

    private void _encodeDocFile(File original, File newFile, String originalCharset, String newCharset) {
        try {
            // reading of original file
            String originalContent = "";
            InputStreamReader isr = new InputStreamReader(new FileInputStream(original),originalCharset);
            int c;
            while ((c = isr.read()) != -1) {
                originalContent += String.valueOf(Character.toChars(c));
            }
            isr.close();
            
            //  modification of content of original file
            originalContent = originalContent.replaceAll("charset="+originalCharset, "charset="+newCharset);
            if (newCharset.equals("utf-8")) {
                originalContent = originalContent.replaceAll("a href=\"", "a href=\"../");
                originalContent = originalContent.replaceAll("src=\"", "src=\"../");
                originalContent = originalContent.replaceAll("../http://", "http://");
                originalContent = originalContent.replaceAll("../mailto:", "mailto:");
            }

            // create UTF-8 folder if it does not exist
            File utfDir = newFile.getParentFile();
            if (!utfDir.exists()) {
                utfDir.mkdirs();
            }
            
            // writing file in new encoding
            ByteBuffer bb;
            CharsetEncoder UTFencoder = Charset.forName(newCharset).newEncoder();
            FileOutputStream fwNewEnc = new FileOutputStream(newFile);
            StringReader srNewEnc = new StringReader(originalContent);
            CharBuffer cbNewEnc = CharBuffer.allocate(1024);
            while (srNewEnc.read(cbNewEnc) != -1) {
                cbNewEnc.flip();
                bb = UTFencoder.encode(cbNewEnc);
                fwNewEnc.write(bb.array(), 0, bb.limit());
                cbNewEnc.clear();
            }
            fwNewEnc.close();
        } catch (IOException ex) {
            Logger.getLogger(LangFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRepositoryType(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public void setRepositoryFolderUrl(String repositoryFolderUrl) {
        this.repositoryFolderUrl = repositoryFolderUrl;
    }

//    public void setIntern(boolean intern) {
//        this.intern = intern;
//    }

    
}
