/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import ajgl.utils.ajglTools;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JOptionPane;

/**
 *
 * @author Vláďa
 */
public class PluginDownloader implements Runnable {

    PluginList pluginList;
    PropertyChangeSupport propertyChange;
    boolean stop = false;

    public PluginDownloader(PluginList pluginList) {
        this.pluginList = pluginList;
        this.propertyChange = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void run() {
        int count = 0;
        
        propertyChange.firePropertyChange("workStarted", null, "Download of plugins started.");
        if (ajglTools.checkInternetConnection()) {
                for (Plugin plugin : pluginList) {
                    if (stop || Thread.interrupted()) {
                        break;
                    }
                    propertyChange.firePropertyChange("pluginDowloadStarted", null, plugin.getName());
                    plugin.downloadFilelist();
                    plugin.downloadFiles();
                    propertyChange.firePropertyChange("pluginDowloaded", null, new Integer(++count));
                    //Thread.yield();
                }
        } else {
            JOptionPane.showMessageDialog(null, "You are not connected to internet, plugins can not be downloaded!","Internet connection failed",JOptionPane.WARNING_MESSAGE);
        }
        propertyChange.firePropertyChange("workFinished", null, pluginList);
    }

    public void stop() {
        stop = true;
    }
}
