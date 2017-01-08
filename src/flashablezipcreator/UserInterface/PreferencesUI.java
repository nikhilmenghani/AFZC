/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.UserInterface;

/**
 *
 * @author Nikhil
 */
public class PreferencesUI extends javax.swing.JFrame {

    /**
     * Creates new form PreferencesUI
     */
    public PreferencesUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        panelPreferenceOptions = new javax.swing.JPanel();
        JSPPreferences = new javax.swing.JScrollPane();
        listPreferences = new javax.swing.JList<>();
        panelPreferencesDetails = new javax.swing.JPanel();
        paneGeneral = new javax.swing.JLayeredPane();
        panelGeneral = new javax.swing.JPanel();
        lblGeneralHeading = new javax.swing.JLabel();
        lblAndroidVersion = new javax.swing.JLabel();
        cbAndroidVersion = new javax.swing.JCheckBox();
        lblGeneralOptions = new javax.swing.JLabel();
        lblHoverInformation = new javax.swing.JLabel();
        lblAromaVersion = new javax.swing.JLabel();
        cbAromaVersion = new javax.swing.JComboBox<>();
        lblProjectSetup = new javax.swing.JLabel();
        cbProjectSetup = new javax.swing.JCheckBox();
        panelThemes = new javax.swing.JPanel();
        lblThemesOptions = new javax.swing.JLabel();
        lblThemesHeading = new javax.swing.JLabel();
        cbThemeDefault = new javax.swing.JCheckBox();
        cbThemeNikhil = new javax.swing.JCheckBox();
        cbThemeMiui = new javax.swing.JCheckBox();
        cbThemeMiui4 = new javax.swing.JCheckBox();
        cbThemeMiui6 = new javax.swing.JCheckBox();
        cbThemeFranzyroy = new javax.swing.JCheckBox();
        cbThemeIcs = new javax.swing.JCheckBox();
        cbThemeTouchwiz = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setOpaque(false);

        panelPreferenceOptions.setBackground(new java.awt.Color(255, 255, 255));
        panelPreferenceOptions.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        listPreferences.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        listPreferences.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "General", "Themes" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listPreferences.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listPreferences.setFixedCellHeight(50);
        listPreferences.setSelectedIndex(0);
        listPreferences.setValueIsAdjusting(true);
        JSPPreferences.setViewportView(listPreferences);

        javax.swing.GroupLayout panelPreferenceOptionsLayout = new javax.swing.GroupLayout(panelPreferenceOptions);
        panelPreferenceOptions.setLayout(panelPreferenceOptionsLayout);
        panelPreferenceOptionsLayout.setHorizontalGroup(
            panelPreferenceOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JSPPreferences, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
        );
        panelPreferenceOptionsLayout.setVerticalGroup(
            panelPreferenceOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JSPPreferences)
        );

        panelPreferencesDetails.setBackground(new java.awt.Color(255, 255, 255));
        panelPreferencesDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelGeneral.setBackground(new java.awt.Color(255, 255, 255));

        lblGeneralHeading.setBackground(new java.awt.Color(204, 204, 204));
        lblGeneralHeading.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGeneralHeading.setText("Important options for working with AFZC");

        lblAndroidVersion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAndroidVersion.setText("Android Version");

        cbAndroidVersion.setBackground(new java.awt.Color(255, 255, 255));
        cbAndroidVersion.setSelected(true);
        cbAndroidVersion.setText("Android 5.x+");
        cbAndroidVersion.setToolTipText("Enable Support for 5.x+ based Roms");

        lblGeneralOptions.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblGeneralOptions.setText("General Options");

        lblHoverInformation.setText("Hover your mouse over options to get additional info");

        lblAromaVersion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAromaVersion.setText("Aroma Version");

        cbAromaVersion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Version 3.00b1 - MELATI", "Version 2.70 RC2 - FLAMBOYAN", "Version 2.56 - EDELWEIS" }));
        cbAromaVersion.setToolTipText("Choose which Aroma Binary to use.");
        cbAromaVersion.setOpaque(false);

        lblProjectSetup.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProjectSetup.setText("Quick Project Setup");

        cbProjectSetup.setBackground(new java.awt.Color(255, 255, 255));
        cbProjectSetup.setSelected(true);
        cbProjectSetup.setText("Open dialog box automatically");
        cbProjectSetup.setToolTipText("Enabling this will Open Add Project Dialog Box on Start up for quick project creation.");

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGeneralHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHoverInformation)
                            .addComponent(lblAndroidVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbAndroidVersion)
                            .addComponent(lblAromaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbAromaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProjectSetup, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbProjectSetup)))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(lblGeneralOptions)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGeneralOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHoverInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblGeneralHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAndroidVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbAndroidVersion)
                .addGap(13, 13, 13)
                .addComponent(lblAromaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbAromaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProjectSetup, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbProjectSetup)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        panelThemes.setBackground(new java.awt.Color(255, 255, 255));

        lblThemesOptions.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblThemesOptions.setText("Themes Options");

        lblThemesHeading.setBackground(new java.awt.Color(204, 204, 204));
        lblThemesHeading.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblThemesHeading.setText("Check Themes to be added by default");

        cbThemeDefault.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeDefault.setText("Default");

        cbThemeNikhil.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeNikhil.setSelected(true);
        cbThemeNikhil.setText("Nikhil");

        cbThemeMiui.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeMiui.setText("Miui");

        cbThemeMiui4.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeMiui4.setText("Miui4");

        cbThemeMiui6.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeMiui6.setText("Miui6");

        cbThemeFranzyroy.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeFranzyroy.setText("Franzyroy");

        cbThemeIcs.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeIcs.setText("Ics");

        cbThemeTouchwiz.setBackground(new java.awt.Color(255, 255, 255));
        cbThemeTouchwiz.setText("Touchwiz");

        javax.swing.GroupLayout panelThemesLayout = new javax.swing.GroupLayout(panelThemes);
        panelThemes.setLayout(panelThemesLayout);
        panelThemesLayout.setHorizontalGroup(
            panelThemesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThemesLayout.createSequentialGroup()
                .addGroup(panelThemesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThemesLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(lblThemesOptions))
                    .addGroup(panelThemesLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(panelThemesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbThemeDefault)
                            .addComponent(lblThemesHeading)
                            .addComponent(cbThemeNikhil)
                            .addComponent(cbThemeMiui)
                            .addComponent(cbThemeMiui4)
                            .addComponent(cbThemeMiui6)
                            .addComponent(cbThemeFranzyroy)
                            .addComponent(cbThemeIcs)
                            .addComponent(cbThemeTouchwiz))))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        panelThemesLayout.setVerticalGroup(
            panelThemesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThemesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThemesOptions)
                .addGap(18, 18, 18)
                .addComponent(lblThemesHeading)
                .addGap(18, 18, 18)
                .addComponent(cbThemeDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeNikhil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeMiui)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeMiui4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeMiui6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeFranzyroy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeIcs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemeTouchwiz)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        paneGeneral.setLayer(panelGeneral, javax.swing.JLayeredPane.DEFAULT_LAYER);
        paneGeneral.setLayer(panelThemes, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout paneGeneralLayout = new javax.swing.GroupLayout(paneGeneral);
        paneGeneral.setLayout(paneGeneralLayout);
        paneGeneralLayout.setHorizontalGroup(
            paneGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(paneGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelThemes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        paneGeneralLayout.setVerticalGroup(
            paneGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(paneGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelThemes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPreferencesDetailsLayout = new javax.swing.GroupLayout(panelPreferencesDetails);
        panelPreferencesDetails.setLayout(panelPreferencesDetailsLayout);
        panelPreferencesDetailsLayout.setHorizontalGroup(
            panelPreferencesDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneGeneral)
        );
        panelPreferencesDetailsLayout.setVerticalGroup(
            panelPreferencesDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnCancel.setText("Cancel");
        btnCancel.setContentAreaFilled(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.setContentAreaFilled(false);
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(panelPreferenceOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelPreferencesDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPreferenceOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPreferencesDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnCancel.getAccessibleContext().setAccessibleName("btnPreferencesOk");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOKActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreferencesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreferencesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreferencesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreferencesUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreferencesUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JSPPreferences;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox cbAndroidVersion;
    private javax.swing.JComboBox<String> cbAromaVersion;
    private javax.swing.JCheckBox cbProjectSetup;
    private javax.swing.JCheckBox cbThemeDefault;
    private javax.swing.JCheckBox cbThemeFranzyroy;
    private javax.swing.JCheckBox cbThemeIcs;
    private javax.swing.JCheckBox cbThemeMiui;
    private javax.swing.JCheckBox cbThemeMiui4;
    private javax.swing.JCheckBox cbThemeMiui6;
    private javax.swing.JCheckBox cbThemeNikhil;
    private javax.swing.JCheckBox cbThemeTouchwiz;
    private javax.swing.JLabel lblAndroidVersion;
    private javax.swing.JLabel lblAromaVersion;
    private javax.swing.JLabel lblGeneralHeading;
    private javax.swing.JLabel lblGeneralOptions;
    private javax.swing.JLabel lblHoverInformation;
    private javax.swing.JLabel lblProjectSetup;
    private javax.swing.JLabel lblThemesHeading;
    private javax.swing.JLabel lblThemesOptions;
    private javax.swing.JList<String> listPreferences;
    private javax.swing.JLayeredPane paneGeneral;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelPreferenceOptions;
    private javax.swing.JPanel panelPreferencesDetails;
    private javax.swing.JPanel panelThemes;
    // End of variables declaration//GEN-END:variables
}
