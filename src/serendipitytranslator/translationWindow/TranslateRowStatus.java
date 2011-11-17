/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

/**
 *
 * @author Vláďa
 */
public final class TranslateRowStatus {
    private static final int BLANK_ENGLISH = 1;
    private static final int BLANK_LOCAL = 2;
    private static final int EQUAL = 3;
    private static final int DIFFERENT = 4;

    public static final TranslateRowStatus blank_english = new TranslateRowStatus(BLANK_ENGLISH);
    public static final TranslateRowStatus blank_local = new TranslateRowStatus(BLANK_LOCAL);
    public static final TranslateRowStatus equal = new TranslateRowStatus(EQUAL);
    public static final TranslateRowStatus different = new TranslateRowStatus(DIFFERENT);

    private int type;

    public TranslateRowStatus(int num) {
        type = num;
    }

    public String getDescription() {
        switch (type) {
            case BLANK_ENGLISH:
                return "Constant not in english version. To delete it from local file, blank also local language field.";
            case BLANK_LOCAL:
                return "Needs translating.";
            case EQUAL:
                return "Value present in local language but the same text as english. Maybe needs translating.";
            case DIFFERENT:
            default:
                return "Translated.";
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case BLANK_ENGLISH:
                return "blank english";
            case BLANK_LOCAL:
                return "blank local";
            case EQUAL:
                return "equal";
            case DIFFERENT:
            default:
                return "different";
        }
    }

    @Override
    public boolean equals(Object status) {
        if (!(status instanceof TranslateRowStatus)) {
            throw new ClassCastException("object must be of type TranslateRowStatus");
        }
        return type == ((TranslateRowStatus) status).type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.type;
        return hash;
    }


}
