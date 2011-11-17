/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

/**
 *
 * @author Vláďa
 */
public final class PluginStatus {
    private static final int NO = 1;
    private static final int PARTIAL = 2;
    private static final int TRANSLATED = 3;
    private static final int PROBLEM = 4;

    public static final PluginStatus no = new PluginStatus(NO);
    public static final PluginStatus partial = new PluginStatus(PARTIAL);
    public static final PluginStatus translated = new PluginStatus(TRANSLATED);
    public static final PluginStatus problem = new PluginStatus(PROBLEM);

    private int type;

    public PluginStatus(int num) {
        type = num;
    }

    public PluginStatus(String name) {
        if (name.equals("OK")) {
            type  = TRANSLATED;
        } else if (name.equals("partial")) {
            type  = PARTIAL;
        } else if (name.equals("problem")) {
            type  = PROBLEM;
        } else {
            type  = NO;
        }
    }

    public String getDescription() {
        switch (type) {
            case PARTIAL:
                return "English and local version of language file doesn't containt same keys. It means either local file is only partially translated, either some keys were removed from english version and they are still in local version.";
            case TRANSLATED:
                return "All keys in english file are translated.";
            case PROBLEM:
                return "English file was not found. The most probable is that the plugin/template does not use language constants. Nothing to translate here.";
            case NO:
            default:
                return "Language file for current language does not exist. It's time to translate it:-)";
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
            case NO:
            default:
                return "no";
        }
    }

    @Override
    public boolean equals(Object pluginStatus) {
        if (!(pluginStatus instanceof PluginStatus)) {
            throw new ClassCastException("Object to compare must be of type PluginStatus");
        }

        return type == ((PluginStatus) pluginStatus).type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.type;
        return hash;
    }


}
