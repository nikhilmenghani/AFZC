/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.DiskOperations.Write;
import static flashablezipcreator.Operations.AdbOperations.checkDeviceConnectivity;
import static flashablezipcreator.Operations.AdbOperations.getAppList;
import static flashablezipcreator.Operations.AdbOperations.getDeviceName;
import static flashablezipcreator.Operations.AdbOperations.getFileList;
import static flashablezipcreator.Operations.AdbOperations.runProcess;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.UserInterface.FilterList;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Adb extends JFrame {

    public JDialog dialog;
    public static String deviceName = "My Device";
    public static ArrayList<String> filteredPartitionPath = new ArrayList<>();
    public static ArrayList<String> filteredFilePath = new ArrayList<>();
    public static boolean filteredFileInclude = true;
    public static int index = 0;

    private void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Adb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void continueAdbProcessCheck(String message) {
        int dialogResult = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                new MyTree().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.exit(0);
        }
    }

    public Adb() {
        setLookAndFeel();
        int connectivityFlag = checkDeviceConnectivity();
        switch (connectivityFlag) {
            case 0:
                continueAdbProcessCheck("The device is not identified,"
                        + " Cannot proceed with ADB process"
                        + " Do you still want to proceed ahead with manual project creation?");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "The device is unauthorized!\nGive Adb permissions in your mobile phone and try again.");
                System.exit(0);
                break;
        }
        if (connectivityFlag == 1) {
            deviceName = getDeviceName();
            if (!deviceName.equals("")) {
                initComponents();
            } else {
                continueAdbProcessCheck("The device name is not identified,"
                        + " Cannot proceed with ADB process"
                        + " Do you still want to proceed ahead with manual project creation?");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        dialog = new JDialog(this, "ADB", true);
        dialog.setResizable(false);
        panelMain = new javax.swing.JPanel();
        lblDeviceName = new javax.swing.JLabel();
        lblChoose = new javax.swing.JLabel();
        lblDeviceIdentified = new javax.swing.JLabel();
        cbSystemApp = new javax.swing.JCheckBox();
        cbSystemPrivApp = new javax.swing.JCheckBox();
        cbDataApp = new javax.swing.JCheckBox();
        cbSystemFonts = new javax.swing.JCheckBox();
        cbSystemMedia = new javax.swing.JCheckBox();
        cbSystemRingtones = new javax.swing.JCheckBox();
        cbSystemNotifications = new javax.swing.JCheckBox();
        cbSystemAlarms = new javax.swing.JCheckBox();
        cbSystemUI = new javax.swing.JCheckBox();
        btnContinue = new javax.swing.JButton();
        btnIgnoreSelected = new javax.swing.JButton();
        btnImportSelected = new javax.swing.JButton();
        cbUseFilteredList = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMain.setBackground(new java.awt.Color(255, 255, 255));

        lblDeviceName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDeviceName.setText(deviceName);

        lblChoose.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblChoose.setText("Choose the partitions you want to fetch files from");

        lblDeviceIdentified.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDeviceIdentified.setText("Device Identified:-");

        cbSystemApp.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemApp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemApp.setSelected(true);
        cbSystemApp.setText("/system/app");
        cbSystemApp.setContentAreaFilled(false);
        cbSystemApp.setFocusPainted(false);

        cbSystemPrivApp.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemPrivApp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemPrivApp.setText("/system/priv-app");
        cbSystemPrivApp.setFocusPainted(false);

        cbDataApp.setBackground(new java.awt.Color(255, 255, 255));
        cbDataApp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbDataApp.setSelected(true);
        cbDataApp.setText("/data/app");
        cbDataApp.setFocusPainted(false);

        cbSystemFonts.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemFonts.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemFonts.setSelected(true);
        cbSystemFonts.setText("/system/fonts");
        cbSystemFonts.setFocusPainted(false);

        cbSystemMedia.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemMedia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemMedia.setSelected(true);
        cbSystemMedia.setText("/system/media");
        cbSystemMedia.setFocusPainted(false);

        cbSystemRingtones.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemRingtones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemRingtones.setText("/system/media/audio/ringtones");
        cbSystemRingtones.setFocusPainted(false);

        cbSystemNotifications.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemNotifications.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemNotifications.setText("/system/media/audio/notifications");
        cbSystemNotifications.setFocusPainted(false);

        cbSystemAlarms.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemAlarms.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemAlarms.setText("/system/media/audio/alarms");
        cbSystemAlarms.setFocusPainted(false);

        cbSystemUI.setBackground(new java.awt.Color(255, 255, 255));
        cbSystemUI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSystemUI.setText("/system/media/audio/ui");
        cbSystemUI.setFocusPainted(false);

        btnContinue.setBackground(new java.awt.Color(255, 255, 255));
        btnContinue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnContinue.setText("Continue");
        btnContinue.setContentAreaFilled(false);
        btnContinue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueActionPerformed(evt);
            }
        });

        btnIgnoreSelected.setBackground(new java.awt.Color(255, 255, 255));
        btnIgnoreSelected.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnIgnoreSelected.setText("Ignore Selected");
        btnIgnoreSelected.setContentAreaFilled(false);
        btnIgnoreSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIgnoreSelectedActionPerformed(evt);
            }
        });

        btnImportSelected.setBackground(new java.awt.Color(255, 255, 255));
        btnImportSelected.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnImportSelected.setText("Import Selected");
        btnImportSelected.setContentAreaFilled(false);
        btnImportSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportSelectedActionPerformed(evt);
            }
        });

        cbUseFilteredList.setBackground(new java.awt.Color(255, 255, 255));
        cbUseFilteredList.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbUseFilteredList.setSelected(true);
        cbUseFilteredList.setText("Use saved filtered list");
        cbUseFilteredList.setContentAreaFilled(false);
        cbUseFilteredList.setFocusPainted(false);
        cbUseFilteredList.setVisible(false);//a flag to be added here

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIgnoreSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImportSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnContinue, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMainLayout.createSequentialGroup()
                        .addComponent(lblDeviceIdentified)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(lblDeviceName, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbSystemUI, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemAlarms, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemNotifications, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemRingtones, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemMedia, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemPrivApp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemApp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChoose, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbDataApp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSystemFonts, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeviceName, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDeviceIdentified, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblChoose, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(cbSystemApp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemPrivApp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbDataApp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemFonts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemMedia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemRingtones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemNotifications)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemAlarms)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSystemUI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIgnoreSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnContinue, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnImportSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dialog.getContentPane().add(panelMain);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dialog.dispose();
                try {
                    new MyTree().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }// </editor-fold>                        

    private void btnIgnoreSelectedActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        new FilterList("Ignore");
    }
    
    private void btnImportSelectedActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        new FilterList("Import");
    }

    private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {
        filteredPartitionPath = new ArrayList<>();
        if (cbSystemApp.isSelected()) {
            filteredPartitionPath.add("/system/app");
        }
        if (cbSystemPrivApp.isSelected()) {
            filteredPartitionPath.add("/system/priv-app");
        }
        if (cbSystemFonts.isSelected()) {
            filteredPartitionPath.add("/system/fonts");
        }
        if (cbSystemMedia.isSelected()) {
            filteredPartitionPath.add("/system/media");
        }
        if (cbSystemRingtones.isSelected()) {
            filteredPartitionPath.add("/system/media/audio/ringtones");
        }
        if (cbSystemNotifications.isSelected()) {
            filteredPartitionPath.add("/system/media/audio/notifications");
        }
        if (cbSystemAlarms.isSelected()) {
            filteredPartitionPath.add("/system/media/audio/alarms");
        }
        if (cbSystemUI.isSelected()) {
            filteredPartitionPath.add("/system/media/audio/ui");
        }
        if (cbDataApp.isSelected()) {
            filteredPartitionPath.add("/data/app");
        }
        try {
            dialog.dispose();
            new MyTree(2).setVisible(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        letsBegin(filteredPartitionPath);
                    } catch (IOException ex) {
                        Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();

        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void letsBegin(ArrayList<String> pList) throws IOException {
        index = 0;
        int totalSize = 0;
        ArrayList<App> appList = new ArrayList<>();
        updateProgress("Scanning Files from your device", 0, false);
        if (pList.contains("/system/app")
                || pList.contains("/system/priv-app")
                || pList.contains("/data/app")) {
            appList = getAppList(pList);
        }
        pList.remove("/system/app");
        pList.remove("/system/priv-app");
        pList.remove("/data/app");
        ArrayList<String> fileList = new ArrayList<>();
        for (String partition : pList) {
            for (String list : getFileList(partition)) {
                fileList.add(list);
            }
        }
        totalSize = appList.size() + fileList.size();
        if (appList.size() > 0) {
            for (App app : appList) {
                int fileIndex = (index * 100 / totalSize);
                updateProgress("Pulling " + app.packageName, fileIndex, false);
                if (app.associatedFileList.size() > 0) {
                    for (String mPath : app.associatedFileList) {
                        String sPath = app.getImportFilePath(mPath);
                        System.out.println(sPath);
                        java.io.File sFile = new java.io.File(sPath);
                        Write w = new Write();
                        String absolutePath = sFile.getAbsolutePath();
                        sFile = new java.io.File(absolutePath);
                        w.createFolders(sFile.getParent());
                        updateProgress("" + mPath, fileIndex, false);
                        ArrayList<String> pullList = runProcess(true, false, Commands.getAdbPull(mPath, sPath));
                        pullList.forEach((str) -> {
                            System.out.println(str);
                        });
                        app.importFilePath(mPath);

                    }
                }
                updateProgress("Pulled " + app.packageName, fileIndex, true);
            }
        }
        if (fileList.size() > 0) {
            for (String mPath : fileList) {
                int fileIndex = (index * 100 / totalSize);
                File f = new File();
                f.installedPath = mPath;
                String sPath = f.getImportFilePath(mPath);
                System.out.println(sPath);
                if (!sPath.equals("")) {
                    java.io.File sFile = new java.io.File(sPath);
                    Write w = new Write();
                    String absolutePath = sFile.getAbsolutePath();
                    sFile = new java.io.File(absolutePath);
                    w.createFolders(sFile.getParent());
                    updateProgress("Pulling " + mPath, fileIndex, false);
                    ArrayList<String> pullList = runProcess(true, false, Commands.getAdbPull(mPath, sPath));
                    for (String str : pullList) {
                        System.out.println(str);
                    }
                    f.importFilePath(mPath);
                    updateProgress("Pulled " + sFile.getName(), fileIndex, true);
                } else {
                    index += 2;
                }
            }
        }
        if (totalSize > 0) {
            updateProgress("Files Successfully Imported", 100, false);
            JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
            updateProgress("", 0, false);
        }
        setCardLayout(1);
    }

    public static void updateProgress(String progressText, int progressValue, boolean increase) {
        String str = progressText;
        if (progressText.length() > 60) {
            str = str.substring(0, progressText.length() / 3) + "..." + str.substring(progressText.length() - 10, progressText.length());
        } else if (progressText.length() > 40) {
            str = str.substring(0, progressText.length() / 2) + "..." + str.substring(progressText.length() - 10, progressText.length());
        }
        MyTree.txtProgress.setText(str);
        MyTree.circularProgressBar.updateProgress(progressValue);
        if (increase) {
            index++;
        }
    }

    private javax.swing.JButton btnIgnoreSelected;
    private javax.swing.JButton btnImportSelected;
    private javax.swing.JButton btnContinue;
    private javax.swing.JCheckBox cbDataApp;
    private javax.swing.JCheckBox cbSystemAlarms;
    private javax.swing.JCheckBox cbSystemApp;
    private javax.swing.JCheckBox cbSystemFonts;
    private javax.swing.JCheckBox cbSystemMedia;
    private javax.swing.JCheckBox cbSystemNotifications;
    private javax.swing.JCheckBox cbSystemPrivApp;
    private javax.swing.JCheckBox cbSystemRingtones;
    private javax.swing.JCheckBox cbSystemUI;
    private javax.swing.JCheckBox cbUseFilteredList;
    private javax.swing.JLabel lblChoose;
    private javax.swing.JLabel lblDeviceIdentified;
    private javax.swing.JLabel lblDeviceName;
    private javax.swing.JPanel panelMain;
    // End of variables declaration                   
}
