/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsDialog.java
 *
 * Created on 13.11.2011, 10:18:05
 */

package serendipitytranslator.settings;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import serendipitytranslator.mainWindow.MainFrame;
import serendipitytranslator.repositories.*;
import serendipitytranslator.translationWindow.LangFile;

/**
 *
 * @author Vláďa
 */
public class SettingsDialog extends javax.swing.JDialog {

    Point mainWindowPosition = new Point(0,0);
    Dimension mainWindowSize = new Dimension(473,326);
    PropertyChangeSupport propertyChange;


    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setDefaults();
        propertyChange= new PropertyChangeSupport(this);
        readSettingsFromFile(getSettingsFile());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        lineBreakButtonGroup = new javax.swing.ButtonGroup();
        langComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        translatorTextField = new javax.swing.JTextField();
        problemPluginsCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        closeSettingsButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        updateURLTextField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        externPluginsComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        externPluginsTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        pluginsFolderTextField = new javax.swing.JTextField();
        pluginsBrowseButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        coreTypeComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        coreUrlTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        coreFolderTextField = new javax.swing.JTextField();
        coreBrowseButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        externThemesComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        externThemesTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        themesFolderTextField = new javax.swing.JTextField();
        themesBrowseButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        unixRadioButton = new javax.swing.JRadioButton();
        windowsRadioButton = new javax.swing.JRadioButton();

        jFileChooser1.setDialogTitle("Choose directory");
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings for Serendipity Translator");

        langComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "de", "da", "es", "fr", "fi", "cs", "nl", "is", "tr", "se", "pt", "pt_PT", "bg", "hu", "no", "pl", "ro", "it", "ru", "fa", "tw", "tn", "zh", "cn", "ja", "ko", "sa", "ta" }));
        langComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                langComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Translator name and e-mail:");

        translatorTextField.setText("Translator Name <yourmail@example.com>");

        problemPluginsCheckBox.setSelected(true);
        problemPluginsCheckBox.setText("do not show plugins without language file");
        problemPluginsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                problemPluginsCheckBoxActionPerformed(evt);
            }
        });

        jLabel2.setText("Language of translations:");

        closeSettingsButton.setText("Close settings dialog");
        closeSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeSettingsButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("WWW address with application:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional plugins"));

        jLabel4.setText("Repository type:");

        externPluginsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "svn", "cvs", "git html", "git native", "folder" }));
        externPluginsComboBox.setSelectedIndex(3);

        jLabel5.setText("URL with plugins:");

        jLabel11.setText("Local folder:");

        pluginsFolderTextField.setText("plugins/additional_plugins");

        pluginsBrowseButton.setText("browse...");
        pluginsBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pluginsBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pluginsFolderTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pluginsBrowseButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(externPluginsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(externPluginsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(externPluginsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(externPluginsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(pluginsFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pluginsBrowseButton)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Serendipity core"));

        jLabel6.setText("Repository type:");

        coreTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "svn", "cvs", "git html", "git native", "folder" }));
        coreTypeComboBox.setSelectedIndex(3);

        jLabel7.setText("URL - S9y core root:");

        jLabel3.setText("Local folder:");

        coreFolderTextField.setText("plugins/Serendipity");

        coreBrowseButton.setText("browse...");
        coreBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coreBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(coreUrlTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(coreTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(coreFolderTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coreBrowseButton)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coreTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coreUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(coreFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coreBrowseButton)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional themes"));

        jLabel8.setText("Repository type:");

        externThemesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "svn", "cvs", "git html", "git native", "folder" }));
        externThemesComboBox.setSelectedIndex(3);

        jLabel10.setText("URL with themes:");

        jLabel12.setText("Local folder:");

        themesFolderTextField.setText("plugins/additional_themes");

        themesBrowseButton.setText("browse...");
        themesBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themesBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(themesFolderTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themesBrowseButton))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(externThemesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(externThemesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(externThemesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(externThemesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(themesFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themesBrowseButton)))
        );

        resetButton.setText("Reset to default settings");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jLabel13.setText("Style of line breaks:");

        jLabel14.setText("Plugins without language file:");

        lineBreakButtonGroup.add(unixRadioButton);
        unixRadioButton.setSelected(true);
        unixRadioButton.setText("Unix (\\n)");
        unixRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unixRadioButtonActionPerformed(evt);
            }
        });

        lineBreakButtonGroup.add(windowsRadioButton);
        windowsRadioButton.setText("Windows (\\r\\n)");
        windowsRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                windowsRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(closeSettingsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(translatorTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(langComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(unixRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(windowsRadioButton))
                            .addComponent(problemPluginsCheckBox))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateURLTextField)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(langComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(translatorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(problemPluginsCheckBox)
                    .addComponent(jLabel14))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(unixRadioButton)
                    .addComponent(windowsRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeSettingsButton)
                    .addComponent(resetButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    private void langComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_langComboBoxActionPerformed
        propertyChange.firePropertyChange("language", null, langComboBox.getSelectedItem());
}//GEN-LAST:event_langComboBoxActionPerformed

    private void problemPluginsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_problemPluginsCheckBoxActionPerformed
        propertyChange.firePropertyChange("problemPlugins", null, problemPluginsCheckBox.isSelected());
}//GEN-LAST:event_problemPluginsCheckBoxActionPerformed

    private void closeSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeSettingsButtonActionPerformed
        this.setVisible(false);
}//GEN-LAST:event_closeSettingsButtonActionPerformed

    private void coreBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coreBrowseButtonActionPerformed
        jFileChooser1.setSelectedFile(new File(coreFolderTextField.getText()));
        if (jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            coreFolderTextField.setText(jFileChooser1.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_coreBrowseButtonActionPerformed

    private void pluginsBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pluginsBrowseButtonActionPerformed
        jFileChooser1.setSelectedFile(new File(pluginsFolderTextField.getText()));
        if (jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            pluginsFolderTextField.setText(jFileChooser1.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_pluginsBrowseButtonActionPerformed

    private void themesBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themesBrowseButtonActionPerformed
        jFileChooser1.setSelectedFile(new File(themesFolderTextField.getText()));
        if (jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            themesFolderTextField.setText(jFileChooser1.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_themesBrowseButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        setDefaults();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void unixRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unixRadioButtonActionPerformed
        LangFile.setLineBreakStyle(LangFile.UNIX_BRAKE);
    }//GEN-LAST:event_unixRadioButtonActionPerformed

    private void windowsRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_windowsRadioButtonActionPerformed
        LangFile.setLineBreakStyle(LangFile.WINDOWS_BRAKE);
    }//GEN-LAST:event_windowsRadioButtonActionPerformed

    private void setDefaults() {
        updateURLTextField.setText("http://vlada.ajgl.cz/serendipity_translator");
        coreTypeComboBox.setSelectedIndex(3);
        coreUrlTextField.setText("git://github.com/s9y/Serendipity.git");
        externPluginsComboBox.setSelectedIndex(3);
        externPluginsTextField.setText("git://github.com/s9y/additional_plugins.git");
        externThemesComboBox.setSelectedIndex(3);
        externThemesTextField.setText("git://github.com/s9y/additional_themes.git");        
        unixRadioButton.setSelected(true);
    }
    
    public String getLanguage() {
        return (String) langComboBox.getSelectedItem();
    }

    public String getTranslator() {
        return translatorTextField.getText();
    }

    public boolean isShowEmptyPlugins() {
        return problemPluginsCheckBox.isSelected();
    }

    public String getUpdateURL() {
        return updateURLTextField.getText();
    }

    private File getSettingsFile() {
        return new File("translatorSettings.ini");
    }

    private void readSettingsFromFile(File f) {
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                StringTokenizer st;
                String key;
                int count;
                while ((line = br.readLine()) != null) {
                    st = new StringTokenizer(line,"=");
                    count = st.countTokens();
                    key = st.nextToken();
                    if (count==2 && key.equals("language")) {
                        langComboBox.setSelectedItem(st.nextToken());
                    }
                    if (count==2 && key.equals("translator")) {
                        translatorTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("applicationRepository")) {
                        updateURLTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("hideProblems")) {
                        problemPluginsCheckBox.setSelected(st.nextToken().equals("Y"));
                    }
                    if (count==2 && key.equals("pluginWindowLeft")) {
                        mainWindowPosition.x = Integer.parseInt(st.nextToken());
                        //this.setLocation(Integer.parseInt(st.nextToken()), this.getY());
                    }
                    if (count==2 && key.equals("pluginWindowTop")) {
                        mainWindowPosition.y = Integer.parseInt(st.nextToken());
                        //this.setLocation(this.getX(), Integer.parseInt(st.nextToken()));
                    }
                    if (count==2 && key.equals("pluginWindowWidth")) {
                        mainWindowSize.width = Integer.parseInt(st.nextToken());
                        //this.setSize(Integer.parseInt(st.nextToken()), this.getHeight());
                    }
                    if (count==2 && key.equals("pluginWindowHeight")) {
                        mainWindowSize.height = Integer.parseInt(st.nextToken());
                        //this.setSize(this.getWidth(), Integer.parseInt(st.nextToken()));
                    }
                    if (count==2 && key.equals("coreType")) {
                        coreTypeComboBox.setSelectedItem(st.nextToken());
                    }
                    if (count==2 && key.equals("coreUrl")) {
                        coreUrlTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("coreFolder")) {
                        coreFolderTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("externPluginsType")) {
                        externPluginsComboBox.setSelectedItem(st.nextToken());
                    }
                    if (count==2 && key.equals("externPluginsUrl")) {
                        externPluginsTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("externPluginsFolder")) {
                        pluginsFolderTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("externThemesType")) {
                        externThemesComboBox.setSelectedItem(st.nextToken());
                    }
                    if (count==2 && key.equals("externThemesUrl")) {
                        externThemesTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("externThemesFolder")) {
                        themesFolderTextField.setText(st.nextToken());
                    }
                    if (count==2 && key.equals("lineBreakStyle")) {
                        String lbr = st.nextToken();
                        if (lbr.equals("windows")) {
                            windowsRadioButton.setSelected(true);
                            LangFile.setLineBreakStyle(LangFile.WINDOWS_BRAKE);
                        } else {
                            unixRadioButton.setSelected(true);
                            LangFile.setLineBreakStyle(LangFile.UNIX_BRAKE);
                        }
                    }
                }
                br.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveSettingsToFile() {
        File f = getSettingsFile();
        FileWriter fw;
        try {
            fw = new FileWriter(f);
            fw.write("language="+getLanguage()+"\r\n");
            fw.write("translator="+getTranslator()+"\r\n");
            fw.write("hideProblems="+(problemPluginsCheckBox.isSelected()? "Y" : "N")+"\r\n");
            fw.write("lineBreakStyle="+(windowsRadioButton.isSelected()? "windows" : "unix")+"\r\n");
            fw.write("applicationRepository="+updateURLTextField.getText()+"\r\n");
            fw.write("pluginWindowLeft="+mainWindowPosition.x+"\r\n");
            fw.write("pluginWindowTop="+mainWindowPosition.y+"\r\n");
            fw.write("pluginWindowWidth="+mainWindowSize.width+"\r\n");
            fw.write("pluginWindowHeight="+mainWindowSize.height+"\r\n");
            fw.write("coreType="+getCoreType()+"\r\n");
            fw.write("coreUrl="+getCoreUrl()+"\r\n");
            fw.write("coreFolder="+getCoreLocalFolder()+"\r\n");
            fw.write("externPluginsType="+getExternPluginsType()+"\r\n");
            fw.write("externPluginsUrl="+getExternPluginsUrl()+"\r\n");
            fw.write("externPluginsFolder="+getExternPluginsLocalFolder()+"\r\n");
            fw.write("externThemesType="+getExternThemesType()+"\r\n");
            fw.write("externThemesUrl="+getExternThemesUrl()+"\r\n");
            fw.write("externThemesFolder="+getExternThemesLocalFolder()+"\r\n");
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMainWindowSizeAndPosition(MainFrame mainWindow) {
        mainWindow.setSize(mainWindowSize);
        mainWindow.setLocation(mainWindowPosition);
    }

    public void storeMainWindowSizeAndPosition(MainFrame mainWindow) {
        mainWindowSize = mainWindow.getSize();
        mainWindowPosition = mainWindow.getLocation();
    }

    public String getCoreType() {
        return coreTypeComboBox.getSelectedItem().toString();
    }

    public String getCoreUrl() {
        return coreUrlTextField.getText();
    }

    public String getExternPluginsType() {
        return externPluginsComboBox.getSelectedItem().toString();
    }

    public String getExternPluginsUrl() {
        return externPluginsTextField.getText();
    }

    public String getExternThemesType() {
        return externThemesComboBox.getSelectedItem().toString();
    }

    public String getExternThemesUrl() {
        return externThemesTextField.getText();
    }

    public String getCoreLocalFolder() {
        return coreFolderTextField.getText();
    }

    public String getExternPluginsLocalFolder() {
        return pluginsFolderTextField.getText();
    }

    public String getExternThemesLocalFolder() {
        return themesFolderTextField.getText();
    }
    
    private static SimpleFileRepository selectRepository(String repoType) {
        SimpleFileRepository workingRepository;
        if (repoType.equals("svn")) {
            workingRepository = new SvnHTMLRepository();
        } else if (repoType.equals("git html")) {
            workingRepository = new GitHTMLRepository();
        } else if (repoType.equals("git native")) {
            workingRepository = new GitProtocolRepository();
        } else if (repoType.equals("folder")) {
            workingRepository = new SimpleFileRepository();
        } else {
            workingRepository = new CvsHTMLRepository();
        }
        return workingRepository;
    }
    
    public SimpleFileRepository getCoreRepository() {
        SimpleFileRepository repository = selectRepository(coreTypeComboBox.getSelectedItem().toString());
        repository.setRepositoryFolderName(getCoreLocalFolder());
        repository.setHasInternalPlugins(true);
        if (repository instanceof AbstractUpdatableRepository) {
            ((AbstractUpdatableRepository) repository).setRemoteURL(getCoreUrl());                    
        }
        return repository;
    }
    
    public SimpleFileRepository getPluginsRepository() {
        SimpleFileRepository repository = selectRepository(externPluginsComboBox.getSelectedItem().toString());
        repository.setRepositoryFolderName(getExternPluginsLocalFolder());
        repository.setHasInternalPlugins(false);
        if (repository instanceof AbstractUpdatableRepository) {
            ((AbstractUpdatableRepository) repository).setRemoteURL(getExternPluginsUrl());                    
        }
        return repository;
    }

    public SimpleFileRepository getThemesRepository() {
        SimpleFileRepository repository = selectRepository(externThemesComboBox.getSelectedItem().toString());
        repository.setRepositoryFolderName(getExternThemesLocalFolder());
        repository.setHasInternalPlugins(false);
        if (repository instanceof AbstractUpdatableRepository) {
            ((AbstractUpdatableRepository) repository).setRemoteURL(getExternThemesUrl());                    
        }
        return repository;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeSettingsButton;
    private javax.swing.JButton coreBrowseButton;
    private javax.swing.JTextField coreFolderTextField;
    private javax.swing.JComboBox coreTypeComboBox;
    private javax.swing.JTextField coreUrlTextField;
    private javax.swing.JComboBox externPluginsComboBox;
    private javax.swing.JTextField externPluginsTextField;
    private javax.swing.JComboBox externThemesComboBox;
    private javax.swing.JTextField externThemesTextField;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox langComboBox;
    private javax.swing.ButtonGroup lineBreakButtonGroup;
    private javax.swing.JButton pluginsBrowseButton;
    private javax.swing.JTextField pluginsFolderTextField;
    private javax.swing.JCheckBox problemPluginsCheckBox;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton themesBrowseButton;
    private javax.swing.JTextField themesFolderTextField;
    private javax.swing.JTextField translatorTextField;
    private javax.swing.JRadioButton unixRadioButton;
    private javax.swing.JTextField updateURLTextField;
    private javax.swing.JRadioButton windowsRadioButton;
    // End of variables declaration//GEN-END:variables

}
