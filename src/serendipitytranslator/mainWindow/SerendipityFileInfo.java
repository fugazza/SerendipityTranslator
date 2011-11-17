/*
 * 
 */

package serendipitytranslator.mainWindow;

/**
 * Třída pro uchování informací o staženém souboru
 * @author Vláďa
 */
public class SerendipityFileInfo {
    private String filename;
    private long fileDate;

    public SerendipityFileInfo(String filename, long fileDate) {
        this.filename = filename;
        this.fileDate = fileDate;
    }

    public long getFileDate() {
        return fileDate;
    }

    public String getFilename() {
        return filename;
    }
}
