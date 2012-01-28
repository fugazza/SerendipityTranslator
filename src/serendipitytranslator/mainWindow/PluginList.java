/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import ajgl.utils.ajglTools;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import serendipitytranslator.repositories.AbstractHTMLRepository;
import serendipitytranslator.repositories.SimpleFileRepository;
import serendipitytranslator.settings.SettingsDialog;

/**
 *
 * @author Vláďa
 */
public class PluginList extends ArrayList<Plugin> {

    private static SettingsDialog settings = null;

    private String language = "cs";
    private PropertyChangeSupport propertyChange;
    private static HashMap<String,String> messageDatabase = null;

    public PluginList(String language) {
        super();
        this.language = language;
        this.propertyChange = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void loadFromWeb() {
        SimpleFileRepository coreRepository = settings.getCoreRepository();
        SimpleFileRepository pluginsRepository = settings.getPluginsRepository();
        SimpleFileRepository themesRepository = settings.getThemesRepository();
        
        boolean downloadsRequired = coreRepository.isUpdatable() || pluginsRepository.isUpdatable() || themesRepository.isUpdatable();
        boolean internetAvailable = ajglTools.checkInternetConnection();
        if (!internetAvailable) {
            JOptionPane.showMessageDialog(null, "You are not connected to internet, plugin list can not be downloaded!","Internet connection failed",JOptionPane.WARNING_MESSAGE);
        }
        
        if (!downloadsRequired || internetAvailable) {
            propertyChange.firePropertyChange("workStarted", null, "Download of list of plugins started.");
            //System.out.println("Connection works, now the download begins.");
            clear();

            //System.out.println("System file will be added.");
            Plugin systemLangFile = new Plugin("system",language);
            systemLangFile.setRepository(coreRepository);
            systemLangFile.setFolderInRepository("lang");
            add(systemLangFile);

            //System.out.println("Internals will be loaded.");
            boolean coreRepAvailable = true;
            if (coreRepository instanceof AbstractHTMLRepository
                    && !((AbstractHTMLRepository) coreRepository).isAvailable()) {
                coreRepAvailable = false;
            }
            if (coreRepAvailable) {
                coreRepository.setPropertyChange(propertyChange);
                coreRepository.loadListOfPlugins(this, "plugins", language, true);
                coreRepository.loadListOfPlugins(this, "templates", language, true);
            } else {
                JOptionPane.showMessageDialog(null, "Core server ("+settings.getCoreUrl()+") is not accessible."+'\r'+'\n'+"Internal plugins cannot be updated.", "Core server error", JOptionPane.WARNING_MESSAGE);
            }

            //System.out.println("External plugins be loaded.");
            boolean pluginRepAvailable = true;
            if (pluginsRepository instanceof AbstractHTMLRepository
                    && !((AbstractHTMLRepository) pluginsRepository).isAvailable()) {
                pluginRepAvailable = false;
            }
            if (pluginRepAvailable) {
                pluginsRepository.setPropertyChange(propertyChange);
                pluginsRepository.loadListOfPlugins(this, "", language, false);
            } else {
                JOptionPane.showMessageDialog(null, "Server with external plugins ("+settings.getExternPluginsUrl()+") is not accessible."+'\r'+'\n'+"External plugins cannot be updated.", "Plugins server error", JOptionPane.WARNING_MESSAGE);
            }

            //System.out.println("External themes will be loaded.");
            boolean themesRepAvailable = true;
            if (themesRepository instanceof AbstractHTMLRepository) {
                if (ajglTools.checkInternetConnection(settings.getExternThemesUrl())) {
                    ((AbstractHTMLRepository) themesRepository).setRemoteURL(settings.getExternThemesUrl());                    
                } else {
                    themesRepAvailable = false;
                }
            }
            if (themesRepAvailable) {
                themesRepository.setPropertyChange(propertyChange);
                themesRepository.loadListOfPlugins(this, "", language, false);
            } else {
                JOptionPane.showMessageDialog(null, "Server with external themes ("+settings.getExternThemesUrl()+") is not accessible."+'\r'+'\n'+"External themes cannot be updated.", "Themes server error", JOptionPane.WARNING_MESSAGE);
            }

            System.out.println("Plugin list updated. Now there are " + size() + " plugins/templates in the list.");
            propertyChange.firePropertyChange("workFinished", null, this);
            //JOptionPane.showMessageDialog(null, "Plugin list downloaded","Plugin list downloaded",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public synchronized boolean add(Plugin plugin) {
        Iterator<Plugin> iter = this.iterator();
        while (iter.hasNext()) {
            if (iter.next().getName().equals(plugin.getName())) {
                return false;
            }
        }
        plugin.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("plugin_files_compared")) {
                    int pos = PluginList.this.indexOf(evt.getNewValue());
                    propertyChange.firePropertyChange("plugin_status_changed", null, pos);
                }
            }

        });

        if (messageDatabase != null) {
            plugin.setMessageDatabase(messageDatabase);
        }

        return super.add(plugin);
    }

    public Plugin getPluginByName(String name) {
        //System.out.println("Searching for plugin "+ name);
        for (Plugin p: this) {
            //System.out.println("           ... "+ p.getName());
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void saveToLocalDb() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            Document doc = docFactory.newDocumentBuilder().newDocument();
            doc.setXmlVersion("1.0");

            Node pluginDatabase = doc.createElement("pluginDatabase");
            doc.appendChild(pluginDatabase);

            Node plugin;
            Node name;
            Node langVersion;
            Node pluginVersion;
            Node type;
            Node status;
            Node intern;
            Node localStatus;
            Node documentationStatus;
            Node enCount;
            Node locCount;
            for (Plugin p: this) {
                plugin          = doc.createElement("plugin");

                name            = doc.createElement("name");
                name.setTextContent(p.getName());

                langVersion     = doc.createElement("langVersion");
                langVersion.setTextContent(p.getLangFileVersion());

                pluginVersion   = doc.createElement("pluginVersion");
                pluginVersion.setTextContent(p.getPluginVersion());

                type            = doc.createElement("type");
                type.setTextContent(p.getType().toString());

                status          = doc.createElement("status");
                status.setTextContent(p.getStatus().toString());

                intern          = doc.createElement("intern");
                intern.setTextContent(p.isIntern() ? "yes" : "no");

                localStatus     = doc.createElement("localStatus");
                localStatus.setTextContent(p.getLocalStatus().toString());

                documentationStatus     = doc.createElement("documentationStatus");
                documentationStatus.setTextContent(p.getDocumentationStatus().toString());

                enCount          = doc.createElement("enCount");
                enCount.setTextContent(Integer.toString(p.getEnCount()));

                locCount          = doc.createElement("locCount");
                locCount.setTextContent(Integer.toString(p.getLocCount()));

                plugin.appendChild(name);
                plugin.appendChild(langVersion);
                plugin.appendChild(pluginVersion);
                plugin.appendChild(type);
                plugin.appendChild(intern);
                plugin.appendChild(enCount);
                plugin.appendChild(locCount);
                plugin.appendChild(status);
                plugin.appendChild(localStatus);
                plugin.appendChild(documentationStatus);

                pluginDatabase.appendChild(plugin);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            File db = new File("pluginDatabse_"+language+".xml");
            StreamResult result = new StreamResult(db);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static PluginList loadFromLocalDb(String language) {
        SimpleFileRepository coreRepository = settings.getCoreRepository();
        SimpleFileRepository pluginsRepository = settings.getPluginsRepository();
        SimpleFileRepository themesRepository = settings.getThemesRepository();
        
        PluginList plugins = new PluginList(language);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            File db = new File("pluginDatabse_"+language+".xml");
            if (db.exists()) {
                Document doc = docFactory.newDocumentBuilder().parse(db);

                NodeList pluginList = doc.getElementsByTagName("plugin");

                NodeList p;
                Node pluginData;
                Plugin plugin;
                System.out.println("Stored plugins count = "+pluginList.getLength());
                for (int i=0; i < pluginList.getLength(); i++) {
                    p = pluginList.item(i).getChildNodes();
                    String name = "some_plugin";
                    String langVersion = "1.0";
                    String pluginVersion = "1.0";
                    PluginType type = PluginType.event;
                    PluginStatus status = PluginStatus.no;
                    PluginStatus localStatus = PluginStatus.no;
                    DocumentationStatus documentationStatus = DocumentationStatus.no;
                    boolean intern = false;
                    int enCount = 0;
                    int locCount = 0;

                    for (int j=0; j < p.getLength(); j++) {
                        pluginData = p.item(j);
                        if (pluginData.getNodeType() == Node.ELEMENT_NODE) {
                            if (pluginData.getNodeName().equals("name")) {
                                name = pluginData.getTextContent();
                            } else if (pluginData.getNodeName().equals("langVersion")) {
                                langVersion = pluginData.getTextContent();
                            } else if (pluginData.getNodeName().equals("pluginVersion")) {
                                pluginVersion = pluginData.getTextContent();
                            } else if (pluginData.getNodeName().equals("type")) {
                                type = new PluginType(pluginData.getTextContent());
                            } else if (pluginData.getNodeName().equals("intern")) {
                                intern = pluginData.getTextContent().equals("yes");
                            } else if (pluginData.getNodeName().equals("enCount")) {
                                enCount = Integer.parseInt(pluginData.getTextContent());
                            } else if (pluginData.getNodeName().equals("locCount")) {
                                locCount = Integer.parseInt(pluginData.getTextContent());
                            } else if (pluginData.getNodeName().equals("status")) {
                                status = new PluginStatus(pluginData.getTextContent());
                            } else if (pluginData.getNodeName().equals("localStatus")) {
                                localStatus = new PluginStatus(pluginData.getTextContent());
                            } else if (pluginData.getNodeName().equals("documentationStatus")) {
                                documentationStatus = new DocumentationStatus(pluginData.getTextContent());
                            }
                        }
                    }

                    plugin = new Plugin(name,language);
                    plugin.setLangFileVersion(langVersion);
                    plugin.setPluginVersion(pluginVersion);
                    plugin.setStatus(status);
                    plugin.setIntern(intern);
                    plugin.setLocalStatus(localStatus);
                    plugin.setDocumentationStatus(documentationStatus);
                    plugin.setType(type);
                    plugin.setCounts(enCount, locCount);

                    if (type.equals(PluginType.system)) {
                        plugin.setRepository(coreRepository);
                        plugin.setFolderInRepository("lang");
                    } else if (intern) {
                        String folder = "";
                        if (type.equals(PluginType.template)) {
                            folder = "templates";
                        } else {
                            folder = "plugins";
                        }
                        plugin.setRepository(coreRepository);
                        plugin.setFolderInRepository(folder + "/" + name);
                    } else if (type.equals(PluginType.template)) {
                        plugin.setRepository(themesRepository);
                        plugin.setFolderInRepository(name);
                    } else {
                        plugin.setRepository(pluginsRepository);
                        plugin.setFolderInRepository(name);
                    }

                    //System.out.println("plugin " + name + ": intern = " + plugin.isIntern() + "; type = " + plugin.getType());
                    plugins.add(plugin);
                }
            }

        } catch (SAXException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return plugins;
    }

    public String getLanguage() {
        return language;
    }

    public static void setMessageDatabase(HashMap<String, String> messageDatabase) {
        PluginList.messageDatabase = messageDatabase;
    }

    public static void setSettings(SettingsDialog settings) {
        PluginList.settings = settings;
    }
}
