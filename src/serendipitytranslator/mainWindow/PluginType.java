/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

/**
 *
 * @author Vláďa
 */
public final class PluginType {
    private static final int SIDEBAR = 1;
    private static final int EVENT = 2;
    private static final int TEMPLATE = 3;
    private static final int SYSTEM = 4;

    public static final PluginType sidebar = new PluginType(SIDEBAR);
    public static final PluginType event = new PluginType(EVENT);
    public static final PluginType template = new PluginType(TEMPLATE);
    public static final PluginType system = new PluginType(SYSTEM);

    private int type;

    public PluginType(int num) {
        type = num;
    }

    public PluginType(String name) {
        if (name.equals("sidebar")) {
            type  = SIDEBAR;
        } else if (name.equals("template")) {
            type  = TEMPLATE;
        } else if (name.equals("system")) {
            type  = SYSTEM;
        } else {
            type  = EVENT;
        }

    }


    @Override
    public String toString() {
        switch (type) {
            case SIDEBAR:
                return "sidebar";
            case TEMPLATE:
                return "template";
            case SYSTEM:
                return "system";
            case EVENT:
            default:
                return "event";
        }
    }

    @Override
    public boolean equals(Object pluginType) {
        if (!(pluginType instanceof PluginType)) {
            throw new ClassCastException("Object to compare must be of type PluginType");
        }

        return type == ((PluginType) pluginType).type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.type;
        return hash;
    }


}
