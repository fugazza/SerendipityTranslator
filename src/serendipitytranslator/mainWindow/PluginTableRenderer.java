/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.mainWindow;

import java.awt.Component;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Vláďa
 */
public class PluginTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (c instanceof JComponent && value instanceof PluginStatus) {
            // -------------------------------------
            // displaying of icons for plugin status
            // -------------------------------------

            PluginStatus status = (PluginStatus) value;
            //System.out.println("Rendering plugin status: " + status);
            // ------------- set icon ---------------
            String iconName = "";
            if (status.equals(PluginStatus.no)) {
                if (column == 6) {
                    iconName = "";
                } else {
                    iconName = "/serendipitytranslator/icons/no.png";
                }
            } else if (status.equals(PluginStatus.translated)) {
                iconName = "/serendipitytranslator/icons/ok.png";
            } else if (status.equals(PluginStatus.partial)) {
                iconName = "/serendipitytranslator/icons/partial.png";
            } else if (status.equals(PluginStatus.problem)) {
                iconName = "/serendipitytranslator/icons/problem.png";
            }
            URL resource = getClass().getResource(iconName);

            // --- create label wtih icon (if any) and description ------------
            JLabel label;
            if (resource != null) {
                label = new JLabel(new ImageIcon(resource,status.getDescription()));
            } else {
                label = new JLabel(status.getDescription());
            }
            label.setOpaque(true);
            label.setToolTipText(status.getDescription());
            label.setBackground(c.getBackground());
            label.setBorder(((JComponent) c).getBorder());

            return label;

        } else if (c instanceof JComponent && value instanceof DocumentationStatus) {
            // --------------------------------------------
            // displaying of icons for documentation status
            // --------------------------------------------

            DocumentationStatus status = (DocumentationStatus) value;
            //System.out.println("Rendering documentation status: " + status);
            // ------------- set icon ---------------
            String iconName = "";
            if (status.equals(DocumentationStatus.no)) {
                iconName = "/serendipitytranslator/icons/no.png";
            } else if (status.equals(DocumentationStatus.translated)) {
                iconName = "/serendipitytranslator/icons/ok.png";
            } else if (status.equals(DocumentationStatus.partial)) {
                iconName = "/serendipitytranslator/icons/partial.png";
            } else if (status.equals(DocumentationStatus.local)) {
                iconName = "/serendipitytranslator/icons/local.png";
            } else if (status.equals(DocumentationStatus.problem)) {
                iconName = "";
            }
            URL resource = getClass().getResource(iconName);

            // --- create label wtih icon (if any) and description ------------
            JLabel label;
            if (resource != null) {
                label = new JLabel(new ImageIcon(resource,status.getDescription()));
            } else {
                label = new JLabel(status.getDescription());
            }
            label.setOpaque(true);
            label.setToolTipText(status.getDescription());
            label.setBackground(c.getBackground());
            label.setBorder(((JComponent) c).getBorder());

            return label;

        } else {
            // ---------------------------------------
            // displaying information in other columns
            // ---------------------------------------
            return c;
        }
    }


}
