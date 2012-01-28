/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import serendipitytranslator.mainWindow.Plugin;

/**
 *
 * @author Vláďa
 */
public class TranslateTableModel extends AbstractTableModel {

    private LangFile english = null;
    private LangFile originalLocal = null;
    private LangFile localLang = null;
    private ArrayList<String> keys = null;
    private String translatorName = "";
    private HashMap<String,String> messageDatabase = null;
    private HashMap<Integer,Boolean> initialFill = null;

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        if (keys == null) {
            return 0;
        } else {
            return keys.size();
        }
    }

    public Object getValueAt(int row, int column) {
        if (english == null) {
            return null;
        } else {
            switch (column) {
                case 0:
                    return keys.get(row);
                case 1:
                    return english.get(keys.get(row));
                case 2:
                    String locval = localLang.get(keys.get(row));
                    if ((!initialFill.containsKey(row) || !initialFill.get(row)) && (locval.equals("") || locval.equals("''")) && (messageDatabase != null)) {
                        String key = keys.get(row);
                        //System.out.println("Message database contains key '" + key + "': " + ( messageDatabase.containsKey(key)) + "; keys count = " + messageDatabase.size());
                        if (messageDatabase.containsKey(key)) {
                            setValueAt(messageDatabase.get(key), row, column);
                        }
                        initialFill.put(row, Boolean.TRUE);
                    }

                    return localLang.get(keys.get(row));
                default:
                    return null;
            }
        }
    }

    public TranslateRowStatus getRowStatus(int row) {
        String key = keys.get(row);
        String enValue = english.get(key);
        String locValue = localLang.get(key);

        if (!enValue.equals(locValue)) {
            if (locValue.isEmpty()) {
                return TranslateRowStatus.blank_local;
            }
            if (enValue.isEmpty()) {
                return TranslateRowStatus.blank_english;
            }
            return TranslateRowStatus.different;
        }
        return TranslateRowStatus.equal;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Key";
            case 1:
                return "English";
            case 2:
                return "Local";
            default:
                return null;
        }
    }


    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (column >= 2);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (!(value instanceof String)) {
            System.err.println("Error, value not String");
        } else {
            if (column == 1) {
                english.set(keys.get(row), (String) value);
            } else if (column == 2) {
                localLang.set(keys.get(row), (String) value);
                fireTableCellUpdated(row, column);
            } else {
                System.err.println("Not possible to modify the column number " + column);
            }
        }
    }



    public void setPluginAndLanguage(Plugin plugin, String language) throws FileNotFoundException {
        english = new LangFile(plugin, "en");
        if (!english.exists()) {
            throw new FileNotFoundException("English file not found, nothing to translate.");
        }

        keys = new ArrayList<String>();

        boolean newKeysAdded = false;
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        LangFile downloadedFile = new LangFile(plugin, language);
        LangFile translatedFile = new LangFile(LangFile.LOCATIONS_TRANSLATED,plugin, language);

        if (!downloadedFile.exists()) {
            //System.out.println("downloaded file does not exist.");
            localLang = new LangFile(plugin, language);
            localLang.setKeysStructure(english.getFileStructure());
            if (translatedFile.exists()) {
                for (String key: translatedFile.getKeys()) {
                    localLang.set(key, translatedFile.get(key) );
                }
            }
        } else {
            localLang = downloadedFile;
            if (translatedFile.exists()) {
                if (translatedFile.isIdenticTo(downloadedFile)) {
                    //System.out.println("both downloaded and translated files exist, they are the same.");
                    //localLang = downloadedFile;
                } else {
                    //System.out.println("both downloaded and translated files exist, but different.");
                    Object []objects = {"Downloaded file", "Local translated file"};
                    int result = JOptionPane.showOptionDialog(null, 
                            "Both downloaded and translated file were found, but they do not match. Which file to use?", 
                            "File type selector",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            objects,
                            objects[1]);
                    if (result == 1) {
                        //System.out.println("result = "+result+ "; translated file used");
                        //localLang = translatedFile;
                        for (String key: localLang.getKeys()) {
                            String keyValue = translatedFile.get(key);
                            localLang.set(key, (keyValue == null) ? "" : keyValue );
                        }

                        ArrayList<String> localKeys = localLang.getKeys();
                        for (String key: translatedFile.getKeys()) {
                            if (!localKeys.contains(key)) {
                                if (!newKeysAdded) {
                                    newKeysAdded = true;
                                    localLang.addBlankLineToStructure();
                                    localLang.addCommentToStructure("Next lines were translated on " + shortDateFormat.format(new Date()) );
                                }

                                localLang.set(key, translatedFile.get(key));
                            }
                        }
                    } else {
                        //System.out.println("result = "+result+ "; downloaded file used");
                        //localLang = downloadedFile;
                    }
                }
            } else {
                //System.out.println("downloaded file exists, but the translated one does not.");
                //localLang = downloadedFile;
            }


            ArrayList<String> enKeys = english.getKeys();
            ArrayList<String> locKeys = localLang.getKeys();

            for(String key: enKeys) {
                if (!locKeys.contains(key)) {
                    if (!newKeysAdded) {
                        newKeysAdded = true;
                        localLang.addBlankLineToStructure();
                        localLang.addCommentToStructure("Next lines were translated on " + shortDateFormat.format(new Date()) );
                    }

                    localLang.set(key, "");
                }
                //keys.add(key);
            }
        }

        ArrayList<String> enKeys = english.getKeys();
        ArrayList<String> locKeys = localLang.getKeys();

        for(String key: locKeys) {
            if (!enKeys.contains(key)) {
                english.set(key, "");
            }
        }

        keys = localLang.getKeys();
        originalLocal = localLang.clone();

        initialFill = new HashMap<Integer,Boolean> ();
        //System.out.println("english keys count = " + english.getKeysCount() + "; local keys count = " + localLang.getKeysCount() + "; original keys count = " + originalLocal.getKeysCount());

        fireTableDataChanged();
    }

    public void updateOriginal() {
        originalLocal = localLang.clone();
        fireTableDataChanged();
    }

    public boolean compareLocalToOriginal() {
        return originalLocal.isIdenticTo(localLang);
    }

    public void saveLocal() {
        localLang.setAuthorName(translatorName);
        //localLang.setKeysStructure(english.getFileStructure());
        localLang.saveToFile();
    }

    public void setTranslatorName(String translatorName) {
        this.translatorName = translatorName;
        if (localLang != null) {
            localLang.setAuthorName(translatorName);
        }
    }

    public void setMessageDatabase(HashMap<String, String> messageDatabase) {
        this.messageDatabase = messageDatabase;
    }


}
