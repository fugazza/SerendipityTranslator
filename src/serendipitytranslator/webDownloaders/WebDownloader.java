/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.webDownloaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 *
 * @author Vláďa
 */
public abstract class WebDownloader {

    public abstract void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern);

    public abstract Vector<SerendipityFileInfo> loadFileList(String urlString);

    public abstract String modifyToCheckoutLink(String urlString);

    protected boolean followsInReader(InputStream is, String text) throws IOException {
        for (int pos = 0;  pos < text.length(); pos++) {
            if (is.read() != text.codePointAt(pos)) {
                return false;
            }
        }
        return true;
    }
    
}
