/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import serendipitytranslator.mainWindow.Plugin;
import serendipitytranslator.mainWindow.PluginList;

/**
 *
 * @author Vláďa
 */
public class StaticInternsRepository extends AbstractTranslatorRepository {

    private static String[] interns = {
        "serendipity_event_bbcode",
        "serendipity_event_browsercompatibility",
        "serendipity_event_contentrewrite",
        "serendipity_event_creativecommons",
         "serendipity_event_emoticate",
         "serendipity_event_entryproperties",
         "serendipity_event_karma",
         "serendipity_event_livesearch",
         "serendipity_event_mailer",
         "serendipity_event_nl2br",
         "serendipity_event_s9ymarkup",
         "serendipity_event_searchhighlight",
         "serendipity_event_spamblock",
         "serendipity_event_spartacus",
         "serendipity_event_statistics",
         "serendipity_event_templatechooser",
         "serendipity_event_textile",
         "serendipity_event_textwiki",
         "serendipity_event_trackexits",
         "serendipity_event_weblogping",
         "serendipity_event_xhtmlcleanup",
         "serendipity_plugin_comments",
         "serendipity_plugin_creativecommons",
         "serendipity_plugin_entrylinks",
         "serendipity_plugin_eventwrapper",
         "serendipity_plugin_history",
         "serendipity_plugin_recententries",
         "serendipity_plugin_remoterss",
         "serendipity_plugin_shoutbox",
         "serendipity_plugin_templatedropdown",
         "blue",
         "bulletproof",
         "carl_contest",
         "competition",
         "contest",
         "default",
         "default-php",
         "default-rtl",
         "default-xml",
         "idea",
         "kubrick",
         "moz-modern",
         "mt-clean",
         "mt-georgiablue",
         "mt-gettysburg",
         "mt-plainjane",
         "mt-rusty",
         "mt-trendy",
         "mt3-chalkboard",
         "mt3-gettysburg",
         "mt3-independence",
         "mt3-squash",
         "newspaper",
         "wp"
    };

    public static String[] getInterns() {
        return interns;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    @Override
    public boolean hasInternalPlugins() {
        return true;
    }

    @Override
    public boolean hasFiles() {
        return false;
    }

    @Override
    public void loadListOfPlugins(PluginList plugins, String urlString, String language, boolean isIntern) {
        for (String pluginName : getInterns()) {
            Plugin p = new Plugin(pluginName, language);
            p.setIntern(true);
        }
    }
    
}
