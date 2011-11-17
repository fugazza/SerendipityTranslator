/*
 * file DocumentationStatus.java
 * part of package serendipitytranslator.mainWindow
 * defines possible statuses for documentation of serendipity plugin
 */

package serendipitytranslator.mainWindow;

/**
 * This class defines possible statuses for documentation of serendipity plugin
 * @author Vláďa
 */
public final class DocumentationStatus {
    private static final int NO = 1;
    private static final int PARTIAL = 2;
    private static final int TRANSLATED = 3;
    private static final int PROBLEM = 4;
    private static final int LOCAL = 5;

    /**
     * documentation for plugin exists but is not translated
     */
    public static final DocumentationStatus no = new DocumentationStatus(NO);
    /**
     * documentation for plugin exists and is translated, but possibly outdated
     * (new version of english documentation may exist and is not yet translated
     * to local language)
     */
    public static final DocumentationStatus partial = new DocumentationStatus(PARTIAL);
    /**
     * documentation for plugin exists and is translated and uploaded on cvs server
     */
    public static final DocumentationStatus translated = new DocumentationStatus(TRANSLATED);
    /**
     * english documentation for plugin does not exist
     */
    public static final DocumentationStatus problem = new DocumentationStatus(PROBLEM);
    /**
     * Documentation for plugin exists and is translated, but only on local computer.
     * It should be uploaded to cvs server.
     */
    public static final DocumentationStatus local = new DocumentationStatus(LOCAL);

    /**
     * Integer value representing the status of plugin documentation.
     */
    private int type;

    public DocumentationStatus(int num) {
        type = num;
    }

    public DocumentationStatus(String name) {
        if (name.equals("OK")) {
            type  = TRANSLATED;
        } else if (name.equals("partial")) {
            type  = PARTIAL;
        } else if (name.equals("problem")) {
            type  = PROBLEM;
        } else if (name.equals("local")) {
            type  = LOCAL;
        } else {
            type  = NO;
        }
    }

    public String getDescription() {
        switch (type) {
            case PARTIAL:
                return "Documentation file exist, but it is older than english one. Update it!";
            case TRANSLATED:
                return "Documentation is translated.";
            case PROBLEM:
                return "English documentation was not found. Nothing to translate here.";
            case LOCAL:
                return "Documentation was translated, but is stored only on local computer. Upload it!";
            case NO:
            default:
                return "Documentation is not translated. It's time to translate it:-)";
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case PARTIAL:
                return "partial";
            case TRANSLATED:
                return "OK";
            case PROBLEM:
                return "problem";
            case LOCAL:
                return "local";
            case NO:
            default:
                return "no";
        }
    }

    @Override
    public boolean equals(Object documentationStatus) {
        if (!(documentationStatus instanceof DocumentationStatus)) {
            throw new ClassCastException("Object to compare must be of type DocumentationStatus");
        }

        return type == ((DocumentationStatus) documentationStatus).type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.type;
        return hash;
    }


}
