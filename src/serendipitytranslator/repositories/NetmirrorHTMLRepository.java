/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.PluginStatus;
import serendipitytranslator.mainWindow.PluginType;

/**
 *
 * @author Vláďa
 */
public class NetmirrorHTMLRepository extends AbstractHTMLRepository {

    private void loadFromFile(PluginList plugins, File file, String language) {
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

    @Override
    public void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern) {
        //plugins.clear();
        File file1 = new File("package_event.xml");
        File file2 = new File("package_sidebar.xml");
        File file3 = new File("package_template.xml");
        download(file1);
        download(file2);
        download(file3);

        plugins.add(new Plugin("system",language));
        //addInterns();
        loadFromFile(plugins, file1,language);
        loadFromFile(plugins, file2,language);
        loadFromFile(plugins, file3,language);

        file1.delete();
        file2.delete();
        file3.delete();

        System.out.println("Plugin list updated. Now there are " + plugins.size() + " plugins/templates in the list.");
        //propertyChange.firePropertyChange("list_downloaded", null, this);
    }

    @Override
    public void updateFileList(String folderPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
