/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.webDownloaders;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.PluginType;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 * https://github.com/s9y/Serendipity
 * @author Vláďa
 */
public class GitDownloader extends WebDownloader {

    @Override
    public void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern) {
        InputStream is = null;
        try {
            String server;
            String projectRoot;
            String dirname;

            //System.out.println(urlString);
            Pattern pattern = Pattern.compile("(https?://[^/]*)/(.*)/([^/]*)");
            Matcher m = pattern.matcher(urlString);
            m.matches();
            server = m.group(1);
            projectRoot = m.group(2);
            dirname = m.group(3);

            URL url = new URL(server+"/" + projectRoot + "/"+dirname);
            //System.out.println(server+" --- " + projectRoot + " --- "+dirname);
            is = url.openStream();
            int c = is.read();
            Plugin p;

            while (c != -1) {
                if (c == '<' && followsInReader(is,"a ")) {
                    //System.out.println("mám <a ");
                    if (followsInReader(is,"href=\"/" + projectRoot+"/"+dirname+"/")) {
                        //System.out.println("mám plugin");
                        String pluginName = "";
                        while ((c=is.read()) != '"') {
                            pluginName += new String(Character.toChars(c));
                        }

                        //System.out.println("plugin: " + pluginName);
                        if (!pluginName.contains("..") && !pluginName.contains("http://")) {
                            //System.out.println("Plugin found = " + pluginName);
                            p = new Plugin(pluginName,language);
                            if (dirname.contains("plugins")) {
                                if (p.getType().equals(PluginType.template)) {
                                    p.setType(PluginType.event);
                                }
                            } else {
                                p.setType(PluginType.template);
                            }
                            p.setIntern(isIntern);
                            p.setRepositoryType("git");
                            p.setRepositoryFolderUrl(urlString + "/" + pluginName);
                            plugins.add(p);
                        }
                    }
                } else {
                    c=is.read();
                }
            }

            is.close();
        } catch (SocketException ex) {
            //System.out.println(ex.getMessage());
            if (ex.getMessage().equals("Connection reset")) {
                //JOptionPane.showMessageDialog(null, "CVS server not found. "+dirname+" cannot be read.", "CVS server error", JOptionPane.WARNING_MESSAGE);
            }
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Vector<SerendipityFileInfo> loadFileList(String urlString) {
        Vector<SerendipityFileInfo> filelist = new Vector<SerendipityFileInfo> ();
        String server;
        String origFolder;
        String folder;

        Pattern pattern = Pattern.compile("(https?://[^/]*)/(.*)");
        Matcher m = pattern.matcher(urlString);
        m.matches();
        server = m.group(1);
        origFolder = m.group(2);
        folder = modifyFolderForFiles(origFolder);
        //System.out.println("Plugin filelist download started. url = " + urlString);

        InputStream is = null;
        try {

            URL url = new URL(server+"/"+folder);
            is = url.openStream();
            int c = is.read();

            while (c != -1) {
                if (c == '<' && followsInReader(is,"a ")) {
                    //System.out.println("have <a ");
                    if (followsInReader(is,"href=\"/"+folder+"/")) {
                        String fileName = "";
                        String age = "";

                        //System.out.println("have folder");
                        while ((c=is.read()) != '"') {
                            fileName += new String(Character.toChars(c));
                        }
                        //System.out.println("filename = "+fileName);

                        while (c !=-1) {
                            if ((c=is.read()) == '<' && followsInReader(is,"td")) {
                                if (followsInReader(is," class=\"age\"")) {
                                    while (!((c=is.read()) == ' ' && followsInReader(is,"title=\"")) && c!= '/') {
                                    }

                                    while (!(c== '/' || (c=is.read()) == '"')) {
                                        age += new String(Character.toChars(c));
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                        //System.out.println("age = " +age);
                        filelist.add(new SerendipityFileInfo(fileName,
                                                            parseFileDate(age))
                                                            );
                        //System.out.println("added file '"+fileName+"'; age "+age);
                    }

                } else {
                    c=is.read();
                }
            }

            is.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filelist;
    }

    @Override
    public String modifyToCheckoutLink(String urlString) {
        return urlString.replaceFirst("https://", "https://raw.").replaceFirst("tree/", "");
    }

    private long parseFileDate(String age) {
        long date = (new Date()).getTime();
        if (!age.isEmpty()) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = df.parse(age).getTime();
            } catch (ParseException ex) {
                Logger.getLogger(GitDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return date;
    }

    private String modifyFolderForFiles(String origFolder) {
        return origFolder.replaceFirst("tree", "blob");
    }
}
