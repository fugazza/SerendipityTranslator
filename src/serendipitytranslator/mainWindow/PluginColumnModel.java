/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Vláďa
 */
public class PluginColumnModel extends DefaultTableColumnModel {

    @Override
    public void addColumn(TableColumn column) {
        super.addColumn(column);

        switch (getColumnCount()) {
            case 1:
                column.setPreferredWidth(400);
                break;
            case 2:
                column.setPreferredWidth(80);
                break;
            case 3:
                column.setPreferredWidth(50);
                break;
            case 4:
                column.setPreferredWidth(40);
                break;
            case 5:
                column.setPreferredWidth(40);
                break;
            case 6:
                column.setPreferredWidth(80);
                break;
            case 7:
                column.setPreferredWidth(80);
                break;
            case 8:
                column.setPreferredWidth(80);
                break;

        }
    }


}
