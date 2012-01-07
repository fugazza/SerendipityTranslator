/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.repositories;

import java.beans.PropertyChangeSupport;
import serendipitytranslator.mainWindow.PluginList;

/**
 *
 * @author Vláďa
 */
public abstract class AbstractTranslatorRepository {

    protected PropertyChangeSupport propertyChange = null;

    public abstract boolean hasInternalPlugins();

    public abstract boolean hasFiles();
    
    public abstract boolean isUpdatable();
    
    public abstract void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern);

    public void setPropertyChange(PropertyChangeSupport propertyChange) {
        this.propertyChange = propertyChange;
    }
    
}
