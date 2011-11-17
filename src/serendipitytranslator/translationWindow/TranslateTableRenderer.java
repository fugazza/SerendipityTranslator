/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Vláďa
 */
public class TranslateTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            TranslateRowStatus status = ((TranslateTableModel) table.getModel()).getRowStatus(row);
            //System.out.println("row " + row + ": " + status);
            if (status.equals(TranslateRowStatus.blank_english)) {
                //System.out.println("yep, blank english");
                c.setBackground(new Color(255,255,200));
            } else if (status.equals(TranslateRowStatus.blank_local)) {
                //System.out.println("ouh, blank local");
                c.setBackground(new Color(255,200,200));
            } else if (status.equals(TranslateRowStatus.equal)) {
                //System.out.println("wtf the same?");
                c.setBackground(new Color(200,255,200));
            } else {
                c.setBackground(new Color(255,255,255,255));
            }
            //System.out.println("BG color = " + c.getBackground());
        }

        return c;
    }

}
