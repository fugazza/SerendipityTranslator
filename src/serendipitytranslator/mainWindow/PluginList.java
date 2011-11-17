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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import serendipitytranslator.settings.SettingsDialog;
import serendipitytranslator.webDownloaders.CvsDownloader;
import serendipitytranslator.webDownloaders.GitDownloader;
import serendipitytranslator.webDownloaders.SvnDownloader;
import serendipitytranslator.webDownloaders.WebDownloader;

/**
 *
 * @author Vláďa
 */
public class PluginList extends Vector<Plugin> {

    private static SettingsDialog settings = null;

    private String language = "cs";
    private PropertyChangeSupport propertyChange;
    private static Hashtable<String,String> messageDatabase = null;

    public PluginList(String language) {
        super();
        this.language = language;
        this.propertyChange = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void loadFromFile(File file) {
        if (file.exists()) {
            try {
                // System.out.println("File " + file.getName());
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbFactory.newDocumentBuilder();
                Document doc = builder.parse(file);
                NodeList nodes = doc.getChildNodes();
                NodeList packages = nodes.item(1).getChildNodes();
                //System.out.println("There are " + packages.getLength() + " packages.");
                Plugin plugin = null;
                for (int i = 0; i < packages.getLength(); i++) {
                    String pluginName = "";
                    PluginStatus pluginStatus = PluginStatus.problem;
                    NodeList pack = packages.item(i).getChildNodes();
                    for (int j = 0; j < pack.getLength(); j++) {
                        Node data = pack.item(j);
                        if (data.getNodeName().equals("name")) {
                            //System.out.println(data.getNodeName() + " - " + data.getTextContent());
                            pluginName = data.getTextContent();
                        }
                        if (data.getNodeName().equals("release")) {
                            NodeList release = data.getChildNodes();
                            for (int k = 0; k < release.getLength(); k++) {
                                Node releaseItem = release.item(k);
                                if (releaseItem.getNodeName().equals("filelist")) {
                                    NodeList relFiles = releaseItem.getChildNodes();
                                    for (int m =0; m < relFiles.getLength(); m++) {
                                        Node dirNode = relFiles.item(m);
                                        if (dirNode.getNodeName().equals("dir")) {
                                            pluginName = dirNode.getAttributes().getNamedItem("name").getTextContent();
                                            //System.out.println("plugin name changed to " + pluginName);
                                            NodeList baseDir = dirNode.getChildNodes();
                                            //System.out.println(releaseItem.getFirstChild().getNodeName());
                                            for (int l = 0; l < baseDir.getLength(); l++) {
                                                Node fileNode = baseDir.item(l);
                                                if (fileNode.getNodeName().equals("file") && fileNode.getTextContent().equals("lang_" + language + ".inc.php")) {
                                                    pluginStatus = PluginStatus.partial;
                                                    break;
                                                }
                                                if (fileNode.getNodeName().equals("file") && fileNode.getTextContent().equals("lang_en.inc.php")) {
                                                    pluginStatus = PluginStatus.no;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (packages.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        //System.out.println(pluginName + " plugin added");
                        plugin = new Plugin(pluginName,language);
                        plugin.setStatus(pluginStatus);
                        if (file.getName().equals("package_sidebar.xml")) {
                            plugin.setType(PluginType.sidebar);
                        } else if (file.getName().equals("package_template.xml")) {
                            plugin.setType(PluginType.template);
                        }
                        add(plugin);
                    }
                }
            } catch (SAXException ex) {
                Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void download(File file){
        try {
            URL url = new URL("http://netmirror.org/mirror/serendipity/"+file.getName());


            InputStream is = url.openStream();
            byte buffer[] = new byte[1024];
            FileOutputStream fos = new FileOutputStream(file);
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            is.close();
            fos.close();
            
//            BufferedWriter fw = new BufferedWriter(new FileWriter(file));
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                fw.write(inputLine);
//                fw.newLine();
//            }
//            in.close();
//            fw.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadFromRepository() {
        clear();
        File file1 = new File("package_event.xml");
        File file2 = new File("package_sidebar.xml");
        File file3 = new File("package_template.xml");
        download(file1);
        download(file2);
        download(file3);

        add(new Plugin("system",language));
        addInterns();
        loadFromFile(file1);
        loadFromFile(file2);
        loadFromFile(file3);

        file1.delete();
        file2.delete();
        file3.delete();

        System.out.println("Plugin list updated. Now there are " + size() + " plugins/templates in the list.");
        propertyChange.firePropertyChange("list_downloaded", null, this);
    }

    public void loadFromWeb() {
        if (ajglTools.checkInternetConnection()) {
            WebDownloader webDownloader;
            String serverType;
            //System.out.println("Connection works, now the download begins.");
            clear();
            
            Plugin systemLangFile = new Plugin("system",language);
            systemLangFile.setRepositoryType(settings.getCoreType());
            systemLangFile.setRepositoryFolderUrl(settings.getCoreUrl()+ "/lang");
            add(systemLangFile);
            //System.out.println("Internals will be loaded.");
            if (ajglTools.checkInternetConnection(settings.getCoreUrl())) {
                serverType = settings.getCoreType();
                if (serverType.equals("svn")) {
                    webDownloader = new SvnDownloader();
                } else if (serverType.equals("git")) {
                    webDownloader = new GitDownloader();
                } else {
                    webDownloader = new CvsDownloader();
                }

                webDownloader.loadListOfPlugins(this, settings.getCoreUrl() + "/plugins", language, true);
                webDownloader.loadListOfPlugins(this, settings.getCoreUrl() + "/templates", language, true);
            } else {
                JOptionPane.showMessageDialog(null, "Core server ("+settings.getCoreUrl()+") is not accessible."+'\r'+'\n'+"Internal plugins cannot be updated.", "Core server error", JOptionPane.WARNING_MESSAGE);
            }
            
            //System.out.println("External plugins be loaded.");
            if (ajglTools.checkInternetConnection(settings.getExternPluginsUrl())) {
                serverType = settings.getExternPluginsType();
                if (serverType.equals("svn")) {
                    webDownloader = new SvnDownloader();
                } else if (serverType.equals("git")) {
                    webDownloader = new GitDownloader();
                } else {
                    webDownloader = new CvsDownloader();
                }

                webDownloader.loadListOfPlugins(this, settings.getExternPluginsUrl(), language, false);
            } else {
                JOptionPane.showMessageDialog(null, "Server with external plugins ("+settings.getExternPluginsUrl()+") is not accessible."+'\r'+'\n'+"External plugins cannot be updated.", "Plugins server error", JOptionPane.WARNING_MESSAGE);
            }
            //System.out.println("External themes will be loaded.");
            if (ajglTools.checkInternetConnection(settings.getExternThemesUrl())) {
                serverType = settings.getExternThemesType();
                if (serverType.equals("svn")) {
                    webDownloader = new SvnDownloader();
                } else if (serverType.equals("git")) {
                    webDownloader = new GitDownloader();
                } else {
                    webDownloader = new CvsDownloader();
                }

                webDownloader.loadListOfPlugins(this, settings.getExternThemesUrl(), language, false);
            } else {
                JOptionPane.showMessageDialog(null, "CVS server (php-blog.cvs.sourceforge.net) is not accessible."+'\r'+'\n'+"External plugins cannot be updated.", "CVS server error", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, "Server with external themes ("+settings.getExternThemesUrl()+") is not accessible."+'\r'+'\n'+"External themes cannot be updated.", "Themes server error", JOptionPane.WARNING_MESSAGE);
            }
            System.out.println("Plugin list updated. Now there are " + size() + " plugins/templates in the list.");
            propertyChange.firePropertyChange("list_downloaded", null, this);
            //JOptionPane.showMessageDialog(null, "Plugin list downloaded","Plugin list downloaded",JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You are not connected to internet, plugin list can not be downloaded!","Internet connection failed",JOptionPane.WARNING_MESSAGE);
        }

    }

    public void addInterns() {
        for (String pluginName: Plugin.getInterns()) {
            add(new Plugin(pluginName,language));
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
                        plugin.setRepositoryType(settings.getCoreType());
                        plugin.setRepositoryFolderUrl(settings.getCoreUrl()+ "/lang");
                    } else if (intern) {
                        String folder = "";
                        if (type.equals(PluginType.template)) {
                            folder = "templates";
                        } else {
                            folder = "plugins";
                        }
                        plugin.setRepositoryType(settings.getCoreType());
                        plugin.setRepositoryFolderUrl(settings.getCoreUrl()+ "/" + folder + "/" + name);
                    } else if (type.equals(PluginType.template)) {
                        plugin.setRepositoryType(settings.getExternThemesType());
                        plugin.setRepositoryFolderUrl(settings.getExternThemesUrl()+ "/" + name);
                    } else {
                        plugin.setRepositoryType(settings.getExternPluginsType());
                        plugin.setRepositoryFolderUrl(settings.getExternPluginsUrl()+ "/" + name);
                    }

                    //System.out.println("plugin " + name + ": intern = " + plugin.isIntern());
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

    public static void setMessageDatabase(Hashtable<String, String> messageDatabase) {
        PluginList.messageDatabase = messageDatabase;
    }

    public static void setSettings(SettingsDialog settings) {
        PluginList.settings = settings;
    }
}
