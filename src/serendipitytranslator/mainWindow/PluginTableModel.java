/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Vláďa
 */
public class PluginTableModel extends AbstractTableModel {

    private PluginList pluginList = null;
    private boolean displayProblems = false;

    public PluginTableModel() {
        this(null);
    }


    public PluginTableModel(PluginList pluginList) {
       super();
       this.pluginList = pluginList;
       fireTableDataChanged();
    }

    public int getColumnCount() {
        return 8;
    }

    public int getRowCount() {
        if (pluginList == null) {
            return 0;
        } else {
            return pluginList.size();
        }
    }

    public Object getValueAt(int row, int column) {
        if (pluginList == null || row >= pluginList.size()) {
            return null;
        } else {
            switch (column) {
                case 0:
                    return pluginList.get(row).getName();
                case 1:
                    return pluginList.get(row).getType();
                case 2:
                    return pluginList.get(row).isIntern();
                case 3:
                    return pluginList.get(row).getEnCount();
                case 4:
                    return pluginList.get(row).getLocCount();
                case 5:
                    return pluginList.get(row).getStatus();
                case 6:
                    return pluginList.get(row).getLocalStatus();
                case 7:
                    return pluginList.get(row).getDocumentationStatus();
                default:
                    return null;
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Type";
            case 2:
                return "Intern";
            case 3:
                return "En";
            case 4:
                return ((pluginList == null) ? "Loc" : pluginList.getLanguage());
            case 5:
                return "Status";
            case 6:
                return "Translated Status";
            case 7:
                return "Documentation";
            default:
                return null;
        }
    }


    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return PluginType.class;
            case 2:
                return Boolean.class;
            case 3:
            case 4:
                return Integer.class;
            case 5:
            case 6:
                return PluginStatus.class;
            case 7:
                return DocumentationStatus.class;
            default:
                return Object.class;
        }
    }


    public void setPluginList(PluginList pluginList) {
        this.pluginList = pluginList;
        this.pluginList.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("list_downloaded")) {
                    fireTableDataChanged();
                } else if(evt.getPropertyName().equals("plugin_status_changed")) {
                    int row = ((Integer) evt.getNewValue()).intValue();
                    //System.out.println(row);
                    fireTableRowsUpdated(row, row);
                }
            }

        });
        fireTableStructureChanged();
        //fireTableDataChanged();
    }

    public void setDisplayProblems(boolean displayProblems) {
        if (this.displayProblems != displayProblems) {
            this.displayProblems = displayProblems;
        }
    }




}
