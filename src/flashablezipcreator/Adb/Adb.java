/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Operations.AdbOperations;
import static flashablezipcreator.Operations.AdbOperations.checkDeviceConnectivity;
import static flashablezipcreator.Operations.AdbOperations.getAppList;
import static flashablezipcreator.Operations.AdbOperations.getFileList;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Adb {

    public JDialog dialog;
    public static String deviceName = "My Device";
    public static ArrayList<String> filteredPartitionPath = new ArrayList<>();
    public static ArrayList<String> filteredFilePath = new ArrayList<>();
    public static boolean filteredFileInclude = true;
    public static int index = 0;

    public static String logs = "";

    public Adb() {

    }

    public Adb(ProjectItemNode parent) {
        new Thread(() -> {
            importFiles(parent);
        }).start();
    }

    public Adb(ArrayList<Package> packages, ProjectItemNode parent) {
        new Thread(() -> {
            importPackages(packages, parent);
        }).start();
    }

    public void importPackages(ArrayList<Package> packages, ProjectItemNode parent) {
        MyTree.setCardLayout(2);
        Adb.logs = "";
        ArrayList<Package> pList = new ArrayList<>();
        int associatedFileListCount = 0;
        for (Package p : packages) {
            if (!p.packageName.equals("")) {
                String path = AdbOperations.getPackagePath(p.packageName);
                p.updatedInstalledPath = path.substring("package:".length(), path.length());
                updateProgress("Reading Package: " + p.packageName, 0, false);
                pList.add(p);
            } else {
                associatedFileListCount += p.associatedFileList.size();
            }
        }
        index = 0;
        int fileListSize = pList.size() + associatedFileListCount;
        for (Package p : packages) {
            int fileIndex = (index * 100 / fileListSize);
            String pullFrom = "";
            String zipPath = "";
            if (!p.packageName.equals("")) {
                pullFrom = p.updatedInstalledPath;
                zipPath = p.getZipPath(p.installedPath);
                updateProgress("Pulling " + pullFrom, fileIndex, true);
                String pullTo = p.getImportFilePath(p.installedPath, parent);
                AdbOperations.pull(pullFrom, pullTo, zipPath, parent);
            } else {
                for (String file : p.associatedFileList) {
                    pullFrom = file;
                    updateProgress("Pulling " + pullFrom, fileIndex, true);
                    AdbOperations.pull(pullFrom, parent);
                }
            }
        }
        if (!Adb.logs.equals("")) {
            JOptionPane.showMessageDialog(null, Adb.logs);
        }
        if (fileListSize > 0) {
            updateProgress("Files Successfully Imported", 100, false);
            JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
            updateProgress("", 0, false);
        }
        setCardLayout(1);
    }

    public void checkForUpdate(ProjectItemNode parent) {
        new Thread(() -> {
            ArrayList<FileNode> apkFiles = new ArrayList<>();
            apkFiles = AdbOperations.getApkFiles(parent, apkFiles);
            index = 0;
            int apkFilesSize = apkFiles.size();
            if (apkFilesSize > 0) {
                setCardLayout(2);
                for (FileNode file : apkFiles) {
                    int fileIndex = (index * 100 / apkFilesSize);
                    updateProgress("Updating " + file.prop.title, fileIndex, true);
                    try {
                        String packageName = AdbOperations.getPackageName(file.prop.fileSourcePath);
                        packageName = packageName.substring("package: name='".length(), packageName.length());
                        packageName = packageName.substring(0, packageName.indexOf("'"));
                        String packagePath = AdbOperations.getPackagePath(packageName);
                        packagePath = packagePath.substring("package:".length(), packagePath.length());
                        AdbOperations.pullFile(packagePath, file.prop.fileSourcePath);
                    } catch (Exception e) {
                        //need to handle exception in a better way! Device disconnecting doesn't break as the exception is handled in ADB functions.
                        JOptionPane.showMessageDialog(null, "Something went wrong!\nCouldn't update " + file.prop.title + "!");
                    }
                }
                updateProgress("Updating Process Completed", 100, false);
                JOptionPane.showMessageDialog(null, "Updating Process Completed");
                updateProgress("", 0, false);
                setCardLayout(1);
            } else {
                JOptionPane.showMessageDialog(null, "No Apk Files Found!");
            }
        }).start();
    }

    public void importFiles(ProjectItemNode parent) {
        Adb.logs = "";
        int connectivityFlag = checkDeviceConnectivity();
        switch (connectivityFlag) {
            case 0:
                JOptionPane.showMessageDialog(null, "The device is not identified,"
                        + " Cannot proceed with ADB process");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "The device is unauthorized!\nGive Adb permissions in your mobile phone and try again.");
                break;
        }
        if (connectivityFlag == 1) {
            MyTree.setCardLayout(2);
            index = 0;
            ArrayList<String> fileList = new ArrayList<>();
            switch (parent.prop.packageType) {
                case Types.PACKAGE_FILE:
                case Types.PACKAGE_SUBGROUP_FILE:
                case Types.PACKAGE_FOLDER_FILE:
                    fileList = getFileList(parent.prop.location);
                    break;
                case Types.PACKAGE_APP:
                    for (Package app : getAppList(new ArrayList<>(Arrays.asList(parent.prop.location)))) {
                        if (app.associatedFileList.size() > 0) {
                            for (String mPath : app.associatedFileList) {
                                fileList.add(mPath);
                            }
                        }
                    }
                    break;
            }
            int fileListSize = fileList.size();
            if (fileListSize > 0) {
                for (String pullFrom : fileList) {
                    int fileIndex = (index * 100 / fileListSize);
                    updateProgress("Pulling " + pullFrom, fileIndex, true);
                    AdbOperations.pull(pullFrom, parent);
                }
            }
            if (!Adb.logs.equals("")) {
                JOptionPane.showMessageDialog(null, Adb.logs);
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

    public void pushZipToDevice(String source, String destination) {
        if (checkDeviceConnectivity() == 1) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to push file to Device?", "", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                AdbOperations.push(source, destination);
            }
        }
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
}
