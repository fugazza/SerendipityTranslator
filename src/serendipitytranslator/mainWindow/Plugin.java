/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import serendipitytranslator.repositories.AbstractUpdatableRepository;
import serendipitytranslator.repositories.SimpleFileRepository;
import serendipitytranslator.translationWindow.LangFile;

/**
 *
 * @author Vláďa
 */
public final class Plugin implements Comparator {
    private String name;
    private boolean intern = false;
    private PluginType type = PluginType.event;
    private PluginStatus CVSstatus = PluginStatus.no;
    private PluginStatus localStatus = PluginStatus.no;
    private DocumentationStatus documentationStatus = DocumentationStatus.problem;
    private String pluginVersion;
    private String langFileVersion;
    private HashMap<String,String> messageDatabase = null;
    private int enCount = 0;
    private int locCount = 0;
    private int translatedLocCount = 0;
    private SimpleFileRepository repository;
    private String folderInRepository;
    
    private String language;

    PropertyChangeSupport propertyChange;

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
//        for (String n: interns) {
//            if (n.equals(name)) {
//                intern = true;
//            }
//        }
        if (name.equals("homepage")) {
            System.out.println("contructor: hompage plugin type = " + getType());
        }
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
    
    public static boolean isDocReadme(String filename) {
        return (filename.toLowerCase().contains("readme")
                    || filename.toLowerCase().contains("changelog")
                    || filename.toLowerCase().contains("credit")
                    || filename.toLowerCase().contains("copy")
                    || filename.toLowerCase().contains("todo")
                    || filename.toLowerCase().contains("author")
                    && !filename.toLowerCase().endsWith(".tpl")
                    && !filename.toLowerCase().endsWith(".php")
               );
    }

    public ArrayList<SerendipityFileInfo> getFileList() {
        if (repository instanceof SimpleFileRepository) {
            return ((SimpleFileRepository) repository).getFileList(folderInRepository);
        } else {
            return null;
        }
    }
    
    public void downloadFilelist() {
        if (repository instanceof AbstractUpdatableRepository) {
            ((AbstractUpdatableRepository) repository).updateFileList(folderInRepository);
        }
    }
    
    public void downloadFiles() {
        if (repository instanceof AbstractUpdatableRepository) {
            ((AbstractUpdatableRepository) repository).updateFiles(folderInRepository, language);
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
        LangFile downloadedLocFile = new LangFile(this,language);
        //JOptionPane.showMessageDialog(null, "before translated file");
        LangFile translatedLocFile = new LangFile(LangFile.LOCATIONS_TRANSLATED,this,language);
        //JOptionPane.showMessageDialog(null, "before english file");
        LangFile enFile = new LangFile(this,"en");
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
        SerendipityFileInfo enDocFile = getFileInfo(folderInRepository,getDocFileNameEn());
        SerendipityFileInfo csDocFile = getFileInfo(folderInRepository,getDocFileNameLoc());
        File csDocFileLocal = new File(getTranslatedDirName()+ "/" + getDocFileNameLoc());
//        if (enDocFile != null) {
//            System.out.println(name + ": en " + enDocFile.getFilename() + "; cs "+csDocFile+"; folder = "+folderInRepository);
//        }
        
        if (getName().equals("homepage")) {
            System.out.println("english doc filename = "+getDocFileNameEn()+"; docFile = "+enDocFile);
        }

        if (enDocFile == null) { // english documentation file does not exist
            setDocumentationStatus(DocumentationStatus.problem);
        } else if (csDocFile != null) { // documentation in local language exists
            if (csDocFile.getFileDate() > enDocFile.getFileDate()) {
                setDocumentationStatus(DocumentationStatus.translated);
            } else if (csDocFileLocal.exists() && (csDocFileLocal.lastModified() > enDocFile.getFileDate())) {
                setDocumentationStatus(DocumentationStatus.local);
            } else {
                setDocumentationStatus(DocumentationStatus.partial);
            }
        } else if (csDocFileLocal.exists()) { // documentation in local languge exist only in translated direcotry
            if (csDocFileLocal.lastModified() > enDocFile.getFileDate()) {
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

    private SerendipityFileInfo getFileInfo(String folder, String filename) {
        SerendipityFileInfo info = null;
        for (SerendipityFileInfo i: repository.getFileList(folder)) {
            if (i.getFilename().equals(filename)) {
                return i;
            }
        }
        return info;
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

    public void setMessageDatabase(HashMap<String, String> messageDatabase) {
        this.messageDatabase = messageDatabase;
    }

    private String getDocFileNameEn() {
        
        String readmeFile = null;
        long newest = 0;
        String filename;
        for (SerendipityFileInfo file: repository.getFileList(folderInRepository)) {
            filename = file.getFilename();
            //System.out.println(name + ": " + filename);
            if (filename.equals("documentation_en.html")) {
                return "documentation_en.html";
            } else if (isDocReadme(filename)) {
                if (file.getFileDate() > newest) {
                    readmeFile = filename;
                    newest = file.getFileDate();
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

    public void createUtfDocumentation() {
        File locDocFile = new File(getTranslatedDirName()+"/"+getDocFileNameLoc());
        if (locDocFile.exists()) {
            File locDocUtfFile = new File(getTranslatedDirName()+"/UTF-8/"+getDocFileNameLoc());
            if (!locDocUtfFile.exists()) {
                _encodeDocFile(locDocFile, locDocUtfFile, LangFile.getCharsetName(language), "utf-8");
            }
            if (language.equals("cs")) {
                File czDocFile = new File(getTranslatedDirName()+"/documentation_cz.html");
                File czDocUtfFile = new File(getTranslatedDirName()+"/UTF-8/documentation_cz.html");
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

//    public void setIntern(boolean intern) {
//        this.intern = intern;
//    }

    public void setRepository(SimpleFileRepository repository) {
        this.repository = repository;
        intern = this.repository.hasInternalPlugins();
    }

    public void setFolderInRepository(String folderInRepository) {
        this.folderInRepository = folderInRepository;
    }

    public String getFolder() {
        return repository.getRepositoryFolderName()+"/"+folderInRepository;
    }
    
    public String getTranslatedDirName() {
        String dirname;
        String basedir = "plugins_translated/";
        if (getType().equals(PluginType.system)) {
            dirname = basedir + getName();
        } else if (getType().equals(PluginType.template) && isIntern()) {
            dirname = basedir + "core_templates/" + getName();
        } else if (getType().equals(PluginType.template) && !isIntern()) {
            dirname = basedir + "spartacus_templates/" + getName();
        } else if (isIntern()) {
            dirname = basedir + "core/" + getName();
        } else {
            dirname = basedir + "spartacus/" + getName();
        }
        //System.out.println("translated dir name for " + getName() + " = " + dirname);
        return dirname;
    }
    
}
