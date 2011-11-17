/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

/**
 *
 * @author Vláďa
 */
public class LangFileElement {

    public static final int STRING = 1;
    public static final int KEY = 2;
    public static final int HEADER = 3;
    public static final int STATEMENT = 4;

    private int type = STRING;
    private String line = "";
    private String prefix = "";
    private String between = ",";
    private boolean newElement = false;

    public LangFileElement(int type, String line) {
        this(type, line, "", ",\t", false);
    }

    public LangFileElement(int type, String line, String prefix, String between) {
        this(type, line, prefix, between, false);
    }

    public LangFileElement(int type, String line, String prefix, String between, boolean newElement) {
        this.type = type;
        this.line = line;
        this.prefix = prefix;
        this.between = between;
        this.newElement = newElement;
    }

    public String getLine() {
        return line;
    }

    public int getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getBetween() {
        return between;
    }

    public boolean isNewElement() {
        return newElement;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setBetween(String between) {
        this.between = between;
    }

    public void setNewElement(boolean newElement) {
        this.newElement = newElement;
    }

    /*
    @Override
    public LangFileElement clone() {
        return super.clone();
    }
     */


    
}
