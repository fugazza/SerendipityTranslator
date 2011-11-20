/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.repositories;

import serendipitytranslator.mainWindow.PluginList;

/**
 *
 * @author Vláďa
 */
public abstract class AbstractTranslatorRepository {

    public abstract boolean hasInternalPlugins();

    public abstract boolean hasFiles();
    
    public abstract boolean isUpdatable();
    
    public abstract void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern);

}
