/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.repositories;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;
import serendipitytranslator.mainWindow.PluginType;
import serendipitytranslator.mainWindow.SerendipityFileInfo;

/**
 * http://php-blog.cvs.sourceforge.net/viewvc/php-blog/additional_plugins
 * @author Vláďa
 */
public class CvsHTMLRepository extends AbstractHTMLRepository {

    @Override
    public void loadListOfPlugins(PluginList plugins, String folderPath, String language, boolean isIntern) {
        String urlString = getRemoteURL();
        if (folderPath.length() > 0) {
            urlString += "/" + folderPath;
        }
        InputStream is = null;
        try {
            String server;
            String projectRoot;
            String dirname;

            System.out.println(urlString);
            Pattern pattern = Pattern.compile("(https?://[^/]*)/(.*)/([^/]*)");
            Matcher m = pattern.matcher(urlString);
            m.matches();
            server = m.group(1);
            projectRoot = m.group(2);
            dirname = m.group(3);

            URL url = new URL(server+"/" + projectRoot + "/"+dirname);
            is = url.openStream();
            int c = is.read();
            Plugin p;

            while (c != -1) {
                if (c == '<' && followsInReader(is,"a name=\"")) {
                    while ((c = is.read()) != ' ' && c != -1) { }
                    if (c==-1) {
                        break;
                    } else if (followsInReader(is,"href=\"/"+projectRoot+"/"+dirname+"/")) {
                        String pluginName = "";
                        while ((c=is.read()) != '"') {
                            pluginName += new String(Character.toChars(c));
                        }

                        if (!pluginName.contains("..") && !pluginName.contains("http://") && pluginName.endsWith("/")) {
                            //System.out.println("Plugin found = " + pluginName.substring(0,pluginName.length()-1));
                            pluginName = pluginName.substring(0,pluginName.length()-1);
                            p = new Plugin(pluginName,language);
                            if (dirname.contains("plugins")) {
                                if (p.getType().equals(PluginType.template)) {
                                    p.setType(PluginType.event);
                                }
                            }
                            p.setIntern(isIntern);
                            p.setRepository(this);
                            String pluginFolderPath = pluginName;
                            if (folderPath.length() > 0) {
                                pluginFolderPath = folderPath + "/" + pluginName;
                            }
                            p.setFolderInRepository(pluginFolderPath);
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
    public void updateFileList(String folderPath) {
        String urlString = getRemoteURL() + "/" + folderPath;
        ArrayList<SerendipityFileInfo> filelist = new ArrayList<SerendipityFileInfo> ();
        String server;
        String folder;

        Pattern pattern = Pattern.compile("(https?://[^/]*)/(.*)");
        Matcher m = pattern.matcher(urlString);
        m.matches();
        server = m.group(1);
        folder = m.group(2);
        //System.out.println("Plugin filelist download started. - " + urlString + "; folderPath = " + folderPath);

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
                    } else if (followsInReader(is,"href=\"/"+folder+"/")) {
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
                                                                parseFileDate(age, fileName, folderPath))
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

        filelists.put(folderPath,filelist);
    }

    @Override
    public String modifyToCheckoutLink(String urlString) {
        return urlString.replaceFirst("/viewvc/", "/viewvc/*checkout*/");
    }

    private long parseFileDate(String age, String fileName, String folderPath) {
        Pattern p = Pattern.compile(" ");
        String [] splitted = p.split(age);
        long count = Long.parseLong(splitted[0]);
        long date = (new Date()).getTime();
        long fileDate = 0;
        long step = 0;
        if (splitted[1].startsWith("second")) {
            step = 1000l;
        } else if (splitted[1].startsWith("minute")) {
            step = 1000l*60;
        } else if (splitted[1].startsWith("hour")) {
            step = 1000l*60*60;
        } else if (splitted[1].startsWith("day")) {
            step = 1000l*60*60*24;
        } else if (splitted[1].startsWith("week")) {
            step = 1000l*60*60*24*7;
        } else if (splitted[1].startsWith("month")) {
            step = 1000l*60*60*24*31;
        } else if (splitted[1].startsWith("year")) {
            step = 1000l*60*60*24*365;
        }
        fileDate = date - (count+1)*step;

        File localFile = new File(getRepositoryFolderName()+"/"+folderPath+"/"+fileName);
        if (localFile.exists() &&
                (Math.abs(fileDate - localFile.lastModified()) < step)) {
            fileDate = localFile.lastModified();
        }
        //System.out.println("datestring: " + splitted[0] + "; "+ splitted[1] + "; count: " +count+ "; date: "+date+"; filedate: "+fileDate);
        return fileDate;
    }
}
