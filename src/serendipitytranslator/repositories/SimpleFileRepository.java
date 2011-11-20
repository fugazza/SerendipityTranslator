/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import java.io.File;
import java.util.ArrayList;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.PluginType;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 *
 * @author Vláďa
 */
public class SimpleFileRepository extends AbstractTranslatorRepository {

    //private ArrayList<SerendipityFileInfo> filelist = new ArrayList<SerendipityFileInfo>();
    private String repositoryFolderName;
    private boolean hasInternalPlugins;
    
    public ArrayList<SerendipityFileInfo> getFileList(String folderPath) {
        ArrayList<SerendipityFileInfo> filelist = new ArrayList<SerendipityFileInfo> ();

        File downloadDir = new File(getRepositoryFolderName() + "/" + folderPath);

        if (downloadDir.exists()) {
            for (File f: downloadDir.listFiles()) {
                if (f.isFile()) {
                    filelist.add(new SerendipityFileInfo(f.getName(),f.lastModified()));
                }
            }
        }
        return filelist;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    public void setRepositoryFolderName(String repositoryFolderName) {
        this.repositoryFolderName = repositoryFolderName;
    }

    public String getRepositoryFolderName() {
        return repositoryFolderName;
    }

    @Override
    public boolean hasInternalPlugins() {
        return hasInternalPlugins;
    }

    public void setHasInternalPlugins(boolean hasInternalPlugins) {
        this.hasInternalPlugins = hasInternalPlugins;
    }

    @Override
    public boolean hasFiles() {
        return true;
    }

    @Override
    public void loadListOfPlugins(PluginList plugins, String folderPath, String language, boolean isIntern) {
        File downloadDir = new File(getRepositoryFolderName() + "/" + folderPath);

        if (downloadDir.exists()) {
            for (File f: downloadDir.listFiles()) {
                if (f.isDirectory()) {
                    Plugin p = new Plugin(f.getName(), language);
                    if (folderPath.contains("plugins")) {
                        if (p.getType().equals(PluginType.template)) {
                            p.setType(PluginType.event);
                        }
                    } else {
                        p.setType(PluginType.template);
                    }
                    p.setRepository(this);
                    p.setFolderInRepository(folderPath + "/" + f.getName());
                    plugins.add(p);
                }
            }
        }
    }
    
    public String getEnFilename(String pluginFolder) {
        if (pluginFolder.contains("lang")) {
            return "serendipity_lang_en.inc.php";
        } else {
            return "lang_en.inc.php";
        }
    }
    
    public String getCsFilename(String pluginFolder, String language) {
        if (pluginFolder.contains("lang")) {
            return "serendipity_lang_"+language+".inc.php";
        } else {
            return "lang_"+language+".inc.php";
        }
    }
}
