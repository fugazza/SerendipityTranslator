/*
 * library of tools by
 * @author Vláďa Ajgl
 */

package ajgl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import serendipitytranslator.mainWindow.PluginDownloader;

/**
 * library of simple tools by
 * @author Vláďa Ajgl
 */
public class ajglTools {

    /**
     * Adds given file into zip archive under given path
     * @param zipFile zip archive where to add a file
     * @param f file to add in zip archive
     * @param filepath path/name of added file that will appear in zip archive
     * @throws IOException If anything goes bad.
     */
    public static void zipFile (ZipOutputStream zipFile, File f, String filepath) throws IOException {
        ZipEntry entry = new ZipEntry(filepath);
        FileInputStream fis = new FileInputStream(f);
        int bytesRead = 0;
        byte b[] = new byte[1024];

        zipFile.putNextEntry(entry);
        while ((bytesRead = fis.read(b)) != -1) {
            zipFile.write(b, 0, bytesRead);
        }
        fis.close();
        zipFile.closeEntry();
    }

    /**
     * Download a file from given URL and save it into file
     * @param url URL from which the file will be downloaded
     * @param f file where to save the file after download
     * @param date long the date of creation of file. If 0, than the downloaded file will have the timestampt of the moment of download.
     * @throws IOException If anything goes bad, such as FileNotFoundException or MalformedURLException.
     */
    public static void download (URL url, File f, Long date) throws IOException {
        InputStream is = url.openStream();
        FileOutputStream fos = new FileOutputStream(f);
        int bytesRead = 0;
        byte buffer[] = new byte[1024];

        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        is.close();
        fos.close();
        if (date != 0) {
            f.setLastModified(date);
        }
    }

    /**
     * Connects to internet repository, checkes whether the application has
     * newer version on internet and if has, downloads the newest version
     * and restarts application. The application must be one single jar file.
     * @param versionFile URL to internet text file holding newest version string
     * @param version actual Version string to compare with internet version. If these two differs, it is supposed that newer version exist on internet.
     * @param applicationFile URL internet address of newest application jar archive.
     * @param targetFile File where to save downloaded application.
     * @param appName The name of application for purposes of showing messages.
     * @return false if no new version is available, true if update finished successfully
     * @throws IOException If anything goes bad.
     */
    public static boolean updater (URL versionFile, String version, URL applicationFile, File targetFile, String appName) throws IOException {
        String versionString = "";
        int bytesRead = 0;
        byte buffer[] = new byte[1024];

        InputStream is = versionFile.openStream();

        while ((bytesRead = is.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                versionString = versionString + String.valueOf(Character.toChars(buffer[i]));
            }
        }
        is.close();

        if (versionString.equals(version)) {
            return false;
        } else {
            int result = JOptionPane.showConfirmDialog(null, "Newest version of "+appName+" is available. Your version: " + version + ". Newest version: "+versionString+"\r\nUpgrade to new version?","Update to new version",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                ajglTools.download(applicationFile, targetFile, 0l);

                /* is it a jar file? */
                if ( targetFile.getName().endsWith(".jar") ) {
                    String javaBin = System.getProperty("java.home") + "/bin/java";
                    String  toExec[] = new String[] { javaBin, "-jar", targetFile.getPath() };
                    try{
                        Process p = Runtime.getRuntime().exec( toExec );
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            }
            return true;
        }
    }

    /**
     * Checks whether internet connection works. It tries to connect to
     * "http://www.google.com" and tracks exceptions.
     * @return true if connection to google was successfull, false if not
     */
    public static boolean checkInternetConnection() {
        return ajglTools.checkInternetConnection("http://www.google.com");
    }

    /**
     * Checks whether internet connection works. It tries to connect to
     * url given in string and tracks exceptions.
     * @param url string containing url address of server to connect to (e.g. http://www.google.com)
     * @return true if connection to google was successfull, false if not
     */
    public static boolean checkInternetConnection(String url) {
        InputStream is = null;
        try {
            URL testUrl = new URL(url);
            is = testUrl.openStream();
            is.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PluginDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PluginDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
