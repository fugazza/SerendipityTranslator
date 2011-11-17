/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 10.2.2009, 11:54:47
 */

package serendipitytranslator.mainWindow;

import ajgl.utils.ajglTools;
import serendipitytranslator.translationWindow.TranslateFrame;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.table.TableRowSorter;
import serendipitytranslator.translationWindow.LangFile;
import serendipitytranslator.settings.SettingsDialog;

/**
 *
 * @author Vláďa
 */
public class MainFrame extends javax.swing.JFrame {

    PluginList plugins;
    TranslateFrame translateFrame = new TranslateFrame();
    SettingsDialog settingsDialog = new SettingsDialog(this, true);
    private Hashtable<String,String> messageDatabase = null;
    String language;
    private String version = "1.9";
    PluginDownloader pluginDownloader;

    /** Creates new form MainFrame */
    public MainFrame() {
        try {
            Logger.getLogger("serendipitytranslator").addHandler(new FileHandler("error.log"));
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();

        PluginList.setSettings(settingsDialog);
        language = settingsDialog.getLanguage();
        settingsDialog.setMainWindowSizeAndPosition(this);
        settingsDialog.setDownloadDialogSizeAndPosition(downloadDialog);
        translateFrame.setLocation(this.getX()+this.getWidth(), 0);

        settingsDialog.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("language")) {
                    language = (String) evt.getNewValue();
                    updateLanguage();
                } else if(evt.getPropertyName().equals("problemPlugins")) {
                    pluginTable.getRowSorter().modelStructureChanged();
                }
            }

        });

        pluginTable.setModel(new PluginTableModel(plugins));
        pluginTable.setColumnModel(new PluginColumnModel());

        pluginTable.setDefaultRenderer(PluginStatus.class, new PluginTableRenderer());
        pluginTable.setDefaultRenderer(DocumentationStatus.class, new PluginTableRenderer());

        TableRowSorter sorter = new TableRowSorter<PluginTableModel>((PluginTableModel) pluginTable.getModel());
        sorter.setRowFilter(new RowFilter<PluginTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends PluginTableModel, ? extends Integer> entry) {
                PluginTableModel tableModel = entry.getModel();
                PluginStatus status = (PluginStatus) tableModel.getValueAt(entry.getIdentifier(), 5);
                DocumentationStatus docStatus = (DocumentationStatus) tableModel.getValueAt(entry.getIdentifier(), 7);
                return !(settingsDialog.isShowEmptyPlugins() && status.equals(PluginStatus.problem) && docStatus.equals(DocumentationStatus.problem));
            }

        });
        pluginTable.setRowSorter(sorter);
        updateLanguage();

        translateFrame.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("file_saved")) {
                    String pluginName =(String) evt.getNewValue();
                    Plugin p = plugins.getPluginByName(pluginName);
                    //System.out.println("plugin saved");
                    if (p != null) {
                        p.compareFiles();
                        //System.out.println("plugin compared");
                    }
                }
            }

        });

        try {
            ajglTools.updater(new URL(settingsDialog.getUpdateURL() + "/version.txt"), version, new URL(settingsDialog.getUpdateURL() + "/SerendipityTranslator.jar"), new File("SerendipityTranslator.jar"), "SerendipityTranslator");
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        downloadDialog = new javax.swing.JDialog();
        downloadProgressBar = new javax.swing.JProgressBar();
        downloadstatusLabel = new javax.swing.JLabel();
        downloadCancelButton = new javax.swing.JButton();
        licenseDialog = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        licenseCloseButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        aboutDialog = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        aboutCloseButton = new javax.swing.JButton();
        helpFrame = new javax.swing.JFrame();
        helpCloseButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        statsDialog = new javax.swing.JDialog();
        closeStatsButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        statLangLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        statPlugTotalLabel = new javax.swing.JLabel();
        statPlugFullLabel = new javax.swing.JLabel();
        statPlugPartLabel = new javax.swing.JLabel();
        statPlugNoLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        statMessNoLabel = new javax.swing.JLabel();
        statMessFullLabel = new javax.swing.JLabel();
        statMessTotalLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        statDocNoLabel = new javax.swing.JLabel();
        statDocLocalLabel = new javax.swing.JLabel();
        statDocFullLabel = new javax.swing.JLabel();
        statDocTotalLabel = new javax.swing.JLabel();
        statDocPartLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pluginTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        listButton = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();
        compareButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        applicationMenu = new javax.swing.JMenu();
        updateListMenuItem = new javax.swing.JMenuItem();
        downloadMenuItem = new javax.swing.JMenuItem();
        compareMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        settingsMenuItem = new javax.swing.JMenuItem();
        statsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        translationsMenu = new javax.swing.JMenu();
        exportMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        manualMenuItem = new javax.swing.JMenuItem();
        licenseMenuItem = new javax.swing.JMenuItem();
        updateMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        downloadDialog.setTitle("Plugin files download");
        downloadDialog.setMinimumSize(new java.awt.Dimension(500, 100));

        downloadstatusLabel.setText("Plugin download started");

        downloadCancelButton.setText("Cancel download");
        downloadCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout downloadDialogLayout = new javax.swing.GroupLayout(downloadDialog.getContentPane());
        downloadDialog.getContentPane().setLayout(downloadDialogLayout);
        downloadDialogLayout.setHorizontalGroup(
            downloadDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downloadDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(downloadDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(downloadstatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                    .addGroup(downloadDialogLayout.createSequentialGroup()
                        .addComponent(downloadCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)))
                .addContainerGap())
        );
        downloadDialogLayout.setVerticalGroup(
            downloadDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downloadDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(downloadstatusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(downloadDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(downloadProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(downloadCancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );

        licenseDialog.setTitle("Serendipity Translator License Terms");
        licenseDialog.setMinimumSize(new java.awt.Dimension(530, 520));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel3.setText("Serendipity Translator License Terms");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Courier New", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Copyright (c) 2009, Vladimír Ajgl\nAll rights reserved.\n\nRedistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n  * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n * Neither the name of the author nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.\n\nTHIS SOFTWARE IS PROVIDED BY AUTHOR \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea1);

        licenseCloseButton.setText("Close License Dialog");
        licenseCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licenseCloseButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Serendipity Translator is distributed under the terms of BSD license, which follows:");

        javax.swing.GroupLayout licenseDialogLayout = new javax.swing.GroupLayout(licenseDialog.getContentPane());
        licenseDialog.getContentPane().setLayout(licenseDialogLayout);
        licenseDialogLayout.setHorizontalGroup(
            licenseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(licenseDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(licenseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(licenseCloseButton)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        licenseDialogLayout.setVerticalGroup(
            licenseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(licenseDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(licenseCloseButton)
                .addContainerGap())
        );

        aboutDialog.setTitle("About Serendipity Translator");
        aboutDialog.setMinimumSize(new java.awt.Dimension(320, 200));
        aboutDialog.setModal(true);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel5.setText("Serendipity Translator");

        jLabel6.setText("Author:");

        jLabel7.setText("Version:");

        jLabel8.setText("Vladimír Ajgl <vlada@ajgl.cz>");

        versionLabel.setText(version);

        jScrollPane3.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setBorder(null);
        jScrollPane3.setEnabled(false);

        jTextArea2.setBackground(java.awt.SystemColor.control);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(2);
        jTextArea2.setText("Utility intended to easify translations of language files of Serendipity blogging system.");
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setMinimumSize(new java.awt.Dimension(2, 15));
        jScrollPane3.setViewportView(jTextArea2);

        jLabel10.setText("Czech Republic");

        aboutCloseButton.setText("Close About Dialog");
        aboutCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(aboutCloseButton)
                    .addGroup(aboutDialogLayout.createSequentialGroup()
                        .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(32, 32, 32)
                        .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8)
                            .addComponent(versionLabel))
                        .addGap(80, 80, 80)))
                .addContainerGap())
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(versionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aboutCloseButton)
                .addContainerGap())
        );

        helpFrame.setTitle("Help - How to use Serendipity Translator");
        helpFrame.setMinimumSize(new java.awt.Dimension(600, 750));

        helpCloseButton.setText("Close help window");
        helpCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpCloseButtonActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(jEditorPane1);

        javax.swing.GroupLayout helpFrameLayout = new javax.swing.GroupLayout(helpFrame.getContentPane());
        helpFrame.getContentPane().setLayout(helpFrameLayout);
        helpFrameLayout.setHorizontalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpCloseButton)
                .addContainerGap(267, Short.MAX_VALUE))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        helpFrameLayout.setVerticalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, helpFrameLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpCloseButton)
                .addContainerGap())
        );

        statsDialog.setTitle("Statistics - Serendipity Translator");
        statsDialog.setMinimumSize(new java.awt.Dimension(240, 450));

        closeStatsButton.setText("Close stats");
        closeStatsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeStatsButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("Statistics for language:");

        statLangLabel.setText("xx");

        jLabel12.setText("Plugins:");

        jLabel13.setText("total:");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/ok.png"))); // NOI18N
        jLabel14.setText("fully translated:");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/partial.png"))); // NOI18N
        jLabel15.setText("partially translated:");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/no.png"))); // NOI18N
        jLabel16.setText("not translated");

        statPlugTotalLabel.setText("111");

        statPlugFullLabel.setText("111");

        statPlugPartLabel.setText("111");

        statPlugNoLabel.setText("111");

        jLabel21.setText("Messages:");

        jLabel22.setText("total:");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/ok.png"))); // NOI18N
        jLabel23.setText("fully translated:");

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/no.png"))); // NOI18N
        jLabel25.setText("not translated");

        statMessNoLabel.setText("111");

        statMessFullLabel.setText("111");

        statMessTotalLabel.setText("111");

        jLabel17.setText("total:");

        jLabel18.setText("Documentation:");

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/ok.png"))); // NOI18N
        jLabel19.setText("fully translated:");

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/partial.png"))); // NOI18N
        jLabel20.setText("partially translated:");

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/no.png"))); // NOI18N
        jLabel24.setText("not translated");

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/serendipitytranslator/icons/local.png"))); // NOI18N
        jLabel26.setText("locally translated:");

        statDocNoLabel.setText("111");

        statDocLocalLabel.setText("111");

        statDocFullLabel.setText("111");

        statDocTotalLabel.setText("111");

        statDocPartLabel.setText("111");

        javax.swing.GroupLayout statsDialogLayout = new javax.swing.GroupLayout(statsDialog.getContentPane());
        statsDialog.getContentPane().setLayout(statsDialogLayout);
        statsDialogLayout.setHorizontalGroup(
            statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsDialogLayout.createSequentialGroup()
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                    .addGroup(statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                    .addGroup(statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(closeStatsButton))
                    .addGroup(statsDialogLayout.createSequentialGroup()
                        .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel25))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(statMessNoLabel)
                            .addComponent(statMessFullLabel)
                            .addComponent(statMessTotalLabel)
                            .addComponent(statPlugNoLabel)
                            .addComponent(statPlugPartLabel)
                            .addComponent(statPlugFullLabel)
                            .addComponent(statPlugTotalLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, statsDialogLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel26)))
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(statDocNoLabel)
                            .addComponent(statDocLocalLabel)
                            .addComponent(statDocPartLabel)
                            .addComponent(statDocFullLabel)
                            .addComponent(statDocTotalLabel)
                            .addComponent(statLangLabel))))
                .addContainerGap())
        );
        statsDialogLayout.setVerticalGroup(
            statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(statLangLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(statPlugTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(statPlugFullLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(statPlugPartLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(statPlugNoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(statMessTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(statMessFullLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(statMessNoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(statDocTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(statDocFullLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(statDocLocalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(statDocPartLabel))
                .addGap(6, 6, 6)
                .addGroup(statsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(statDocNoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeStatsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Serendipity translator");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pluginTable.setAutoCreateRowSorter(true);
        pluginTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Plugin Name", "Type", "I/E", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pluginTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pluginTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pluginTableMouseClicked(evt);
            }
        });
        pluginTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pluginTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(pluginTable);
        pluginTable.getColumnModel().getColumn(0).setPreferredWidth(200);

        listButton.setText("Update Plugin List");
        listButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listButtonActionPerformed(evt);
            }
        });

        downloadButton.setText("Download Files");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });

        compareButton.setText("Compare Files By Content");
        compareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compareButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(listButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compareButton)
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listButton)
                    .addComponent(downloadButton)
                    .addComponent(compareButton))
                .addContainerGap())
        );

        applicationMenu.setText("Application");
        applicationMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listButtonActionPerformed(evt);
            }
        });

        updateListMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, 0));
        updateListMenuItem.setText("Update Plugin List");
        updateListMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listButtonActionPerformed(evt);
            }
        });
        applicationMenu.add(updateListMenuItem);

        downloadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, 0));
        downloadMenuItem.setText("Download Files");
        downloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        applicationMenu.add(downloadMenuItem);

        compareMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, 0));
        compareMenuItem.setText("Compare Files By Content");
        compareMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compareButtonActionPerformed(evt);
            }
        });
        applicationMenu.add(compareMenuItem);
        applicationMenu.add(jSeparator1);

        settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        applicationMenu.add(settingsMenuItem);

        statsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, 0));
        statsMenuItem.setText("Statistics");
        statsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsMenuItemActionPerformed(evt);
            }
        });
        applicationMenu.add(statsMenuItem);
        applicationMenu.add(jSeparator2);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 0));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        applicationMenu.add(exitMenuItem);

        jMenuBar1.add(applicationMenu);

        translationsMenu.setText("Translations");

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        exportMenuItem.setText("Export translated files to zip file");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        translationsMenu.add(exportMenuItem);

        jMenuBar1.add(translationsMenu);

        helpMenu.setText("Help");

        manualMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, 0));
        manualMenuItem.setText("How to Use The Application");
        manualMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(manualMenuItem);

        licenseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, 0));
        licenseMenuItem.setText("License");
        licenseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licenseMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(licenseMenuItem);

        updateMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, 0));
        updateMenuItem.setText("Update SerendipityTranslator to latest version");
        updateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(updateMenuItem);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, 0));
        aboutMenuItem.setText("About SerendipityTranslator");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateLanguage() {
        messageDatabase = new Hashtable<String,String>();
        PluginList.setMessageDatabase(messageDatabase);
        plugins = PluginList.loadFromLocalDb(language);
        ((PluginTableModel) pluginTable.getModel()).setPluginList(plugins);
        pluginTable.repaint();
    }

    private void listButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listButtonActionPerformed
        PluginList.setSettings(settingsDialog);
        if (plugins.isEmpty()) {
            //plugins.loadFromRepository();
            plugins.loadFromWeb();
            JOptionPane.showMessageDialog(this, "Now only list of available plugins was downloaded.\n" +
                    "To know which plugins are really translated, you must download all language files and compare them by content.\n" +
                    "Continue by click on 'Download Files' button.");
        } else {
            //plugins.loadFromRepository();
            plugins.loadFromWeb();
            //JOptionPane.showMessageDialog(null, "Before comparison.");
            int i = 0;
            for (Plugin p: plugins) {
                File enFile = LangFile.getFile(LangFile.LOCATION_PLUGINS,p.getName(),"en");
                if (enFile.exists()) {
                    p.compareFiles();
                }
                i++;
                //JOptionPane.showMessageDialog(null, "plugin " + p.getName());
            }
            //JOptionPane.showMessageDialog(null, "After comparison. " + i);
        }
}//GEN-LAST:event_listButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        if (plugins.isEmpty()) {
            plugins.loadFromRepository();
        }

        pluginDownloader = new PluginDownloader(plugins);
        pluginDownloader.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("pluginDowloaded")) {
                    int progress = ((Integer) evt.getNewValue()).intValue();
                    downloadProgressBar.setValue(progress);
                    downloadstatusLabel.setText(MessageFormat.format("{0} plugins from {1} downloaded", progress, plugins.size()));
                    //pluginTable.getRowSorter().modelStructureChanged();
                } else if (evt.getPropertyName().equals("dowloadFinished")) {
                    downloadDialog.setVisible(false);
                    pluginTable.getRowSorter().modelStructureChanged();
                } else if (evt.getPropertyName().equals("pluginDowloadStarted")) {
                    downloadstatusLabel.setText(downloadstatusLabel.getText()+" (now downloading "+evt.getNewValue()+")");
                }
            }

        });
        new Thread(pluginDownloader).start();
        downloadDialog.setVisible(true);
        downloadProgressBar.setMinimum(0);
        downloadProgressBar.setMaximum(plugins.size());

}//GEN-LAST:event_downloadButtonActionPerformed

    private void compareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compareButtonActionPerformed
        if (plugins.isEmpty()) {
            downloadButtonActionPerformed(evt);
        } else {
            for (Plugin plugin: plugins) {
                plugin.compareFiles();
            }
            pluginTable.getRowSorter().modelStructureChanged();
        }
}//GEN-LAST:event_compareButtonActionPerformed

    private void pluginTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pluginTableMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            openPluginInTranslateFrame();
        }
    }//GEN-LAST:event_pluginTableMouseClicked

    private void openPluginInTranslateFrame() {
        String selectedPlugin = plugins.get(
                pluginTable.convertRowIndexToModel(pluginTable.getSelectedRow())
                ).getName();
        //System.out.println("Selected plugin: " + selectedPlugin);

        try {
            if (messageDatabase != null) {
                translateFrame.setMessageDatabase(messageDatabase);
            }
            translateFrame.setPluginAndLanguage(selectedPlugin, language);
            translateFrame.setTranslatorName(settingsDialog.getTranslator());
            translateFrame.setVisible(true);
        } catch (FileNotFoundException e) {
            translateFrame.setVisible(false);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Nothing to translate", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        plugins.saveToLocalDb();
        settingsDialog.storeMainWindowSizeAndPosition(this);
        settingsDialog.storeDownloadDialogSizeAndPosition(downloadDialog);
        settingsDialog.saveSettingsToFile();
    }//GEN-LAST:event_formWindowClosing

    private void pluginTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pluginTableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //pluginTable.changeSelection(pluginTable.getSelectedRow()-1, 0, false, false);
            openPluginInTranslateFrame();
            pluginTable.requestFocus();
        }
    }//GEN-LAST:event_pluginTableKeyPressed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuItemActionPerformed
        settingsDialog.setVisible(true);
    }//GEN-LAST:event_settingsMenuItemActionPerformed

    private void licenseCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licenseCloseButtonActionPerformed
        licenseDialog.setVisible(false);
    }//GEN-LAST:event_licenseCloseButtonActionPerformed

    private void licenseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licenseMenuItemActionPerformed
        licenseDialog.setVisible(true);
    }//GEN-LAST:event_licenseMenuItemActionPerformed

    private void aboutCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutCloseButtonActionPerformed
        aboutDialog.setVisible(false);
    }//GEN-LAST:event_aboutCloseButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void manualMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualMenuItemActionPerformed
        URL page = getClass().getResource("/serendipitytranslator/manual/help.html");
        try {
            //jEditorPane1.setPage(new URL("file:manual/help.html"));
            jEditorPane1.setPage(page);
            helpFrame.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Sorry, manual directory was not found:-( \n "+page, "Error, manual not found", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_manualMenuItemActionPerformed

    private void helpCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpCloseButtonActionPerformed
        helpFrame.setVisible(false);
    }//GEN-LAST:event_helpCloseButtonActionPerformed

    private void closeStatsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeStatsButtonActionPerformed
        statsDialog.setVisible(false);
}//GEN-LAST:event_closeStatsButtonActionPerformed

    private void statsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsMenuItemActionPerformed
        generateStats();
        statsDialog.setVisible(true);
    }//GEN-LAST:event_statsMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        try {
            ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(new File(language + "_s9y_translations.zip")));

            String pluginName;
            File dir;
            File dirUTF;

            FileFilter ff = new FileFilter() {
                public boolean accept(File file) {
                    String fileName = file.getName();
                    boolean accepted = fileName.endsWith("en.inc.php")
                            || fileName.endsWith(language+".inc.php")
                            || (language.equals("cs") && fileName.endsWith("cz.inc.php"));
                    //System.out.println(file.getName() + "; accepted = " + accepted);
                    return accepted;
                }
            };

            FileFilter docuFilter = new FileFilter() {
                public boolean accept(File file) {
                    String fileName = file.getName();
                    boolean accepted = fileName.equals("documentation_"+language+".html")
                            || (language.equals("cs") && fileName.equals("documentation_cz.html"));
                    return accepted;
                }
            };

            for (Plugin p: plugins) {
                pluginName = p.getName();
                dir = new File(LangFile.getTranslatedDirName(pluginName));
                dirUTF = new File(LangFile.getTranslatedDirName(pluginName)+"/UTF-8/");
                //System.out.println("dir " + dir.getName() + "; exists = " + dir.exists());

                // put into zip archive all language files
                if (p.getLocalStatus().equals(PluginStatus.translated)
                        && !(new LangFile(LangFile.LOCATIONS_TRANSLATED, p.getName(), language)).isIdenticTo(new LangFile(LangFile.LOCATION_PLUGINS, p.getName(), language))) {

                    for (File f: dir.listFiles(ff)) {
                        //System.out.println("File " + f.getName());
                        ajglTools.zipFile(zipFile,f,f.getPath().substring(19));
                    }
                    for (File f: dirUTF.listFiles(ff)) {
                        ajglTools.zipFile(zipFile,f,f.getPath().substring(19));
                    }
                    
                }

                // add into zip archive all documentation files
                if (p.getDocumentationStatus().equals(DocumentationStatus.local)) {
                    p.createUtfDocumentation();
                    for (File f: dir.listFiles(docuFilter)) {
                        ajglTools.zipFile(zipFile,f,f.getPath().substring(19));
                    }
                    if (dirUTF.exists()) {
                        for (File f: dirUTF.listFiles(docuFilter)) {
                            ajglTools.zipFile(zipFile,f,f.getPath().substring(19));
                        }
                    }
                }
            }

            zipFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void updateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateMenuItemActionPerformed
        // update of SerendipityTranslator to latest version

        // download file with latest version number from internet
        try {
            boolean result;
            result = ajglTools.updater(new URL(settingsDialog.getUpdateURL()+"/version.txt"),
                              version,
                              new URL(settingsDialog.getUpdateURL()+"/SerendipityTranslator.jar"),
                              new File("SerendipityTranslator.jar"),
                              "SerendipityTranslator");
            if (!result) {
                JOptionPane.showMessageDialog(null, "You have newest version of SerendipityTranslator.","Update to new version",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "You are not connected to internet, new version cannot be checked!","Update to new version",JOptionPane.WARNING_MESSAGE);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PluginList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateMenuItemActionPerformed

    private void downloadCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadCancelButtonActionPerformed
        pluginDownloader.stop();
    }//GEN-LAST:event_downloadCancelButtonActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        //System.out.println("Window activated!");
        if (downloadDialog.isVisible()) {
            downloadDialog.setVisible(true);
        }
    }//GEN-LAST:event_formWindowActivated

    private void generateStats() {
        statLangLabel.setText(language);

        int plugins_total = 0;
        int plugins_full = 0;
        int plugins_part = 0;
        int plugins_no = 0;

        int messages_total = 0;
        int messages_full = 0;
        int messages_no = 0;

        int doc_total = 0;
        int doc_full = 0;
        int doc_local = 0;
        int doc_part = 0;
        int doc_no = 0;

        int locCount;
        int enCount;
        PluginStatus status;
        PluginStatus locStatus;
        DocumentationStatus docStatus;

        for (Plugin p: plugins) {
            enCount = p.getEnCount();
            locCount = p.getLocCount();
            plugins_total++;
            messages_total += enCount;

            status = p.getStatus();
            locStatus = p.getLocalStatus();
            docStatus = p.getDocumentationStatus();

            if (status.equals(PluginStatus.translated) || locStatus.equals(PluginStatus.translated)) {
                plugins_full++;
                messages_full += enCount;
            } else if (status.equals(PluginStatus.partial) || locStatus.equals(PluginStatus.partial)) {
                plugins_part++;
                messages_full += (p.getTranslatedLocCount() > locCount) ? p.getTranslatedLocCount() : locCount ;
            } else if (status.equals(PluginStatus.no) || locStatus.equals(PluginStatus.no)) {
                plugins_no++;
                //messages_no += enCount;
            } else {
                plugins_total--;
                messages_total -= enCount;
            }

            if (docStatus.equals(DocumentationStatus.translated)) {
                doc_full++;
                doc_total++;
            } else if (docStatus.equals(DocumentationStatus.local)) {
                doc_local++;
                doc_total++;
            } else if (docStatus.equals(DocumentationStatus.partial)) {
                doc_part++;
                doc_total++;
            } else if (docStatus.equals(DocumentationStatus.no)) {
                doc_no++;
                doc_total++;
            }
        }

        messages_no = messages_total - messages_full;

        statPlugTotalLabel.setText(Integer.toString(plugins_total));
        statPlugFullLabel.setText(Integer.toString(plugins_full));
        statPlugPartLabel.setText(Integer.toString(plugins_part));
        statPlugNoLabel.setText(Integer.toString(plugins_no));

        statMessTotalLabel.setText(Integer.toString(messages_total));
        statMessFullLabel.setText(Integer.toString(messages_full));
        statMessNoLabel.setText(Integer.toString(messages_no));

        statDocTotalLabel.setText(Integer.toString(doc_total));
        statDocFullLabel.setText(Integer.toString(doc_full));
        statDocLocalLabel.setText(Integer.toString(doc_local));
        statDocPartLabel.setText(Integer.toString(doc_part));
        statDocNoLabel.setText(Integer.toString(doc_no));
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutCloseButton;
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu applicationMenu;
    private javax.swing.JButton closeStatsButton;
    private javax.swing.JButton compareButton;
    private javax.swing.JMenuItem compareMenuItem;
    private javax.swing.JButton downloadButton;
    private javax.swing.JButton downloadCancelButton;
    private javax.swing.JDialog downloadDialog;
    private javax.swing.JMenuItem downloadMenuItem;
    private javax.swing.JProgressBar downloadProgressBar;
    private javax.swing.JLabel downloadstatusLabel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JButton helpCloseButton;
    private javax.swing.JFrame helpFrame;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JButton licenseCloseButton;
    private javax.swing.JDialog licenseDialog;
    private javax.swing.JMenuItem licenseMenuItem;
    private javax.swing.JButton listButton;
    private javax.swing.JMenuItem manualMenuItem;
    private javax.swing.JTable pluginTable;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JLabel statDocFullLabel;
    private javax.swing.JLabel statDocLocalLabel;
    private javax.swing.JLabel statDocNoLabel;
    private javax.swing.JLabel statDocPartLabel;
    private javax.swing.JLabel statDocTotalLabel;
    private javax.swing.JLabel statLangLabel;
    private javax.swing.JLabel statMessFullLabel;
    private javax.swing.JLabel statMessNoLabel;
    private javax.swing.JLabel statMessTotalLabel;
    private javax.swing.JLabel statPlugFullLabel;
    private javax.swing.JLabel statPlugNoLabel;
    private javax.swing.JLabel statPlugPartLabel;
    private javax.swing.JLabel statPlugTotalLabel;
    private javax.swing.JDialog statsDialog;
    private javax.swing.JMenuItem statsMenuItem;
    private javax.swing.JMenu translationsMenu;
    private javax.swing.JMenuItem updateListMenuItem;
    private javax.swing.JMenuItem updateMenuItem;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables

}