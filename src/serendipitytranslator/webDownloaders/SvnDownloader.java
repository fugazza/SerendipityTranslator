/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.webDownloaders;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
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
 * http://svn.berlios.de/viewvc/serendipity/trunk/
 * @author Vláďa
 */
public class SvnDownloader extends WebDownloader {

    @Override
    public void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern) {
        InputStream is = null;
        Plugin p;

        try {
            String server_root;
            String dirname;

            Pattern pattern = Pattern.compile("(.*)/([^/]*)");
            Matcher m = pattern.matcher(urlString);
            m.matches();
            server_root = m.group(1);
            dirname = m.group(2);

            URL url = new URL(server_root + "/" + dirname);
            is = url.openStream();
            int c = is.read();

            while (c != -1) {
                if (c == '<' && followsInReader(is,"a href=\"")) {
                    String pluginName = "";
                    char ch[];
                    while ((c=is.read()) != '"') {
                        pluginName += new String(Character.toChars(c));
                    }

                    if (!pluginName.contains("..") && !pluginName.contains("http://") && pluginName.endsWith("/")) {
                        //System.out.println("Plugin found = " + pluginName.substring(0,pluginName.length()-1));
                        p = new Plugin(pluginName.substring(0,pluginName.length()-1),language);
                        if (dirname.equals("plugins")) {
                            if (p.getType().equals(PluginType.template)) {
                                p.setType(PluginType.event);
                            }
                        } else {
                            p.setType(PluginType.template);
                        }
                        p.setIntern(isIntern);
                        p.setRepositoryType("svn");
                        p.setRepositoryFolderUrl(urlString + "/" + pluginName);
                        plugins.add(p);
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
    }

    @Override
    public Vector<SerendipityFileInfo> loadFileList(String urlString) {
        Vector<SerendipityFileInfo> filelist = new Vector<SerendipityFileInfo> ();
        String server;
        String folder;

        Pattern pattern = Pattern.compile("(https?://[^/]*)/(.*)");
        Matcher m = pattern.matcher(urlString);
        m.matches();
        server = m.group(1);
        folder = m.group(2);
        //System.out.println("Plugin filelist download started.");

        InputStream is = null;
        try {

            URL url = new URL(server+"/"+folder);
            is = url.openStream();
            int c = is.read();

            while (c != -1) {
                if (c == '<' && followsInReader(is,"a name=\"")) {
                    while ((c = is.read()) != ' ' && c != -1) { }
                    if (c==-1) {
                        break;
                    } else if (followsInReader(is,"href=\""+folder)) {
                        String fileName = "";

                        while ((c=is.read()) != '"' && c != '?') {
                            fileName += new String(Character.toChars(c));
                        }

                        if (c== '?') {
                            String age = "";
                            boolean haveAge = false;
                            while (!haveAge) {
                                while ((c=is.read()) != '<') {
                                }
                                if (followsInReader(is,"td>&nbsp;")) {
                                    while ((c=is.read()) != '<') {
                                        age += new String(Character.toChars(c));
                                    }
                                    haveAge = true;
                                }
                            }
                        filelist.add(new SerendipityFileInfo(fileName,
                                                            parseFileDate(age))
                                                            );
                            //System.out.println("added file '"+fileName+"'; age "+age);
                        }
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
        return urlString;
    }

    private long parseFileDate(String age) {
        long date = (new Date()).getTime();
        try {
            DateFormat df = DateFormat.getDateInstance();
            date = df.parse(age).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(GitDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

}
