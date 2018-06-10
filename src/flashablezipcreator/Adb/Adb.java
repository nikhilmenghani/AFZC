/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.DiskOperations.Write;
import static flashablezipcreator.Operations.AdbOperations.checkDeviceConnectivity;
import static flashablezipcreator.Operations.AdbOperations.getAppList;
import static flashablezipcreator.Operations.AdbOperations.getDeviceName;
import static flashablezipcreator.Operations.AdbOperations.getFileList;
import static flashablezipcreator.Operations.AdbOperations.runProcess;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Identify;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Mod;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.FilterList;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Adb(int type, ProjectItemNode parent) {
        new Thread(() -> {
            importFiles(type, parent);
        }).start();

    }

    public void importFiles(int type, ProjectItemNode parent) {
        int connectivityFlag = checkDeviceConnectivity();
        switch (connectivityFlag) {
            case 0:
                continueAdbProcessCheck("The device is not identified,"
                        + " Cannot proceed with ADB process"
                        + " Do you still want to proceed ahead with manual project creation?");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "The device is unauthorized!\nGive Adb permissions in your mobile phone and try again.");
                break;
        }
        if (connectivityFlag == 1) {
            MyTree.setCardLayout(2);
            index = 0;
            String partition = "";
            ArrayList<String> fileList = new ArrayList<>();
            switch (type) {
                case Types.GROUP_SYSTEM_FONTS:
                    partition = "/system/fonts";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_DATA_LOCAL:
                    partition = "/data/local";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_SYSTEM_MEDIA:
                    partition = "/system/media";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_SYSTEM_APK:
                    partition = "/system/app";
                    for (Package app : getAppList(new ArrayList<>(Arrays.asList(partition)))) {
                        if (app.associatedFileList.size() > 0) {
                            for (String mPath : app.associatedFileList) {
                                fileList.add(mPath);
                            }
                        }
                    }
                    break;
                case Types.GROUP_SYSTEM_PRIV_APK:
                    partition = "/system/priv-app";
                    for (Package app : getAppList(new ArrayList<>(Arrays.asList(partition)))) {
                        if (app.associatedFileList.size() > 0) {
                            for (String mPath : app.associatedFileList) {
                                fileList.add(mPath);
                            }
                        }
                    }
                    break;
                case Types.GROUP_DATA_APP:
                    partition = "/data/app";
                    for (Package app : getAppList(new ArrayList<>(Arrays.asList(partition)))) {
                        if (app.associatedFileList.size() > 0) {
                            for (String mPath : app.associatedFileList) {
                                fileList.add(mPath);
                            }
                        }
                    }
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                    partition = "/system/media/audio/alarms";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                    partition = "/system/media/audio/notifications";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                    partition = "/system/media/audio/ringtones";
                    fileList = getFileList(partition);
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                    partition = "/system/media/audio/ui";
                    fileList = getFileList(partition);
                    break;
            }
            int fileListSize = fileList.size();
            if (fileListSize > 0) {
                for (String mPath : fileList) {
                    int fileIndex = (index * 100 / fileListSize);
                    Package f = new Package();
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
                        FileNode fileNode = null;
                        TreeOperations to = new TreeOperations();
                        try {
                            fileNode = to.addFileNode(f.getZipPath(mPath), parent);
                        } catch (IOException ex) {
                            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        updateProgress("Pulled " + sFile.getName(), fileIndex, true);
                    } else {
                        index += 1;
                    }
                }
            }
            if (fileListSize > 0) {
                updateProgress("Files Successfully Imported", 100, false);
                JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
                updateProgress("", 0, false);
            }
            setCardLayout(1);
        } else {
            JOptionPane.showMessageDialog(null, "Device Not Compatible!");
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
        btnAdvancedFilter = new javax.swing.JButton();
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

        Adb.filteredFilePath = new ArrayList<>();
        java.io.File f = new java.io.File("FilterList");
        if (f.exists()) {
            Read r = new Read();
            try {
                String strToRead = r.getFileString(f.getAbsolutePath());
                for (String str : strToRead.split("\n")) {
                    Adb.filteredFilePath.add(str);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FilterList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        btnAdvancedFilter.setBackground(new java.awt.Color(255, 255, 255));
        btnAdvancedFilter.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAdvancedFilter.setText("Advanced Filter");
        btnAdvancedFilter.setContentAreaFilled(false);
        btnAdvancedFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvancedFilterActionPerformed(evt);
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
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelMainLayout.createSequentialGroup()
                                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(panelMainLayout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(btnAdvancedFilter)
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
                                                                        .addComponent(lblChoose, javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbDataApp, javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbSystemFonts, javax.swing.GroupLayout.Alignment.LEADING))
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addGap(29, 29, 29))
                                        .addGroup(panelMainLayout.createSequentialGroup()
                                                .addComponent(cbSystemApp)
                                                .addGap(76, 452, Short.MAX_VALUE))))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnContinue, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAdvancedFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnAdvancedFilterActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<String> filteredPartitionPath = new ArrayList<>();
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
        FilterList filterList;
        filterList = new FilterList(filteredPartitionPath);
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
                        letsBegin(filteredPartitionPath, filteredFilePath);
                    } catch (IOException ex) {
                        Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();

        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void letsBegin(ArrayList<String> pList, ArrayList<String> fList) throws IOException {
        index = 0;
        ArrayList<Package> appList = new ArrayList<>();
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
        int appListSize = appList.size();
        int fileListSize = fileList.size();
        if (appListSize > 0) {
            for (Package app : appList) {
                if (app.associatedFileList.size() > 0) {
                    for (String mPath : app.associatedFileList) {
                        fileList.add(mPath);
                    }
                }
            }
        }
        for (String path : filteredFilePath) {
            boolean removeFlag = true;
            if (path.startsWith("+")) {
                removeFlag = false;
            }
            path = path.substring(2, path.length());
            path = path.replaceAll("\"", "");
            if (removeFlag) {
                if (path.contains("*")) {
                    for (String removePath : filteredFilePath) {
                        if (removePath.startsWith(path.substring(0, path.length() - 1))) {
                            filteredFilePath.remove(removePath);
                        }
                    }
                } else {
                    fileList.remove(path);
                }
            } else {
                fileList.remove("all other from that partition");
            }
        }
        fileListSize = fileList.size();

        if (fileListSize > 0) {
            for (String mPath : fileList) {
                int fileIndex = (index * 100 / fileListSize);
                Package f = new Package();
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
                    String filePath = f.getZipPath(mPath);
                    if (!filePath.equals("")) {
                        String projectName = Identify.getProjectName(filePath);
                        String groupName = Identify.getGroupName(filePath);
                        int groupType = Identify.getGroupType(filePath);
                        String originalGroupType = "";
                        if (groupType == Types.GROUP_CUSTOM) {
                            try {
                                originalGroupType = Identify.getOriginalGroupType(filePath);
                            } catch (Exception e) {
                                originalGroupType = "";
                            }
                        }
                        ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_AROMA);
                        String subGroupName = Identify.getSubGroupName(groupName, filePath);
                        int subGroupType = groupType; //Groups that have subGroups have same type.
                        String fName = (new java.io.File(filePath)).getName();
                        TreeOperations to = new TreeOperations();
                        FileNode file = to.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_AROMA, Mod.MOD_LESS);
                        file.prop.fileSourcePath = file.prop.path;
                        Logs.write("Written File: " + fName);
                    }
                    updateProgress("Pulled " + sFile.getName(), fileIndex, true);
                } else {
                    index += 1;
                }
            }
        }
        if (fileListSize > 0) {
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

    private javax.swing.JButton btnAdvancedFilter;
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
