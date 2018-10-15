/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Gapps.Gapps;
import flashablezipcreator.Operations.AdbOperations;
import static flashablezipcreator.Operations.AdbOperations.checkDeviceConnectivity;
import static flashablezipcreator.Operations.AdbOperations.getAppList;
import static flashablezipcreator.Operations.AdbOperations.getFileList;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void importGapps(ProjectItemNode parent) {
        new Thread(() -> {
            switch (parent.prop.modType) {
                case Types.GAPPS_CORE:
                    importPackages(Gapps.getCoreList(), parent);
                    break;
                case Types.GAPPS_PICO:
                    importPackages(Gapps.getPicoList(), parent);
                    break;
                case Types.GAPPS_MY:
                    importPackages(Gapps.getMyList(), parent);
                    break;
                case Types.GAPPS_NANO:
                    importPackages(Gapps.getNanoList(), parent);
                    break;
            }
        }).start();
    }

    public void importPackages(ArrayList<Package> packages, ProjectItemNode parent) {
        MyTree.setCardLayout(2);
        Adb.logs = "";
        index = 0;
        int packageListSize = packages.size();
        float maxFileIndex = 0;
        float startFileIndex = 0;
        int windowIndex = 0;
        float window = 100 / packageListSize;
        for (Package p : packages) {
            windowIndex++;
            startFileIndex = (windowIndex - 1) * window;
            maxFileIndex = (windowIndex * window);
            float filesIndex = 0;
            index = 1;
            String zipPath = "";
            if (!p.packageName.equals("")) {
                String packagePath = AdbOperations.getPackagePath(p.packageName);
                if (packagePath.equals("")) {
                    continue;
                }
                String installPath = "";
                if (!packagePath.startsWith("/data/app")) {
                    installPath = packagePath;
                } else {
                    installPath = p.installedPath;
                }
                File f = new File(packagePath);
                ArrayList<String> fileList = new ArrayList<>();
                fileList = getFileList(f.getParent());//need to check if parent is not file node
                for (String pullFrom : fileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
                    updateProgress("Pulling", pullFrom, filesIndex, true);
                    if (!pullFrom.contains(packagePath)) {
                        if (pullFrom.startsWith("/data/app")) {
                            String tempPullFrom = pullFrom.substring("/data/app/".length(), pullFrom.length());
                            tempPullFrom = p.installedPath.substring(0, p.installedPath.lastIndexOf("/")) + tempPullFrom.substring(tempPullFrom.indexOf("/"), tempPullFrom.length());
                            AdbOperations.pull(pullFrom, tempPullFrom, parent);
                            System.out.println(pullFrom);
                            continue;
                        } else {
                            AdbOperations.pull(pullFrom, parent);
                            System.out.println(pullFrom);
                        }
                    } else {
                        String data[] = p.getImportFilePath(installPath, parent);
                        zipPath = data[1];
                        String pullTo = data[0];
                        AdbOperations.pull(pullFrom, pullTo, zipPath, parent);
                    }
                }
                for (String file : p.associatedFileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
                    updateProgress("Pulling", file, filesIndex, true);
                    AdbOperations.pull(file, parent);
                }
            } else {
                for (String file : p.associatedFileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / p.associatedFileList.size());
                    updateProgress("Pulling", file, filesIndex, true);
                    AdbOperations.pull(file, parent);
                }
            }
        }
        if (!Adb.logs.equals("")) {
            JOptionPane.showMessageDialog(null, Adb.logs);
        }
        if (packageListSize > 0) {
            updateProgress("", "Files Successfully Imported", 100, false);
            JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
            updateProgress("", "", 0, false);
        }
        setCardLayout(1);
    }

    public void checkForUpdate(ProjectItemNode parent) {
        new Thread(() -> {
            ArrayList<FileNode> files = new ArrayList<>();
            files = AdbOperations.getFilesToUpdate(parent, files);
            int filesSize = files.size();
            float maxFileIndex = 0;
            float startFileIndex = 0;
            int windowIndex = 0;
            float window = 100 / filesSize;
            if (filesSize > 0) {
                setCardLayout(2);
                for (FileNode file : files) {
                    windowIndex++;
                    startFileIndex = (windowIndex - 1) * window;
                    maxFileIndex = (windowIndex * window);
                    float filesIndex = 0;
                    index = 1;
                    try {
                        if (file.prop.title.endsWith(".apk")) {
                            String packageName = AdbOperations.getPackageName(file.prop.fileSourcePath);
                            importPackage(file, packageName, parent, startFileIndex, maxFileIndex);
                        } else {
                            String fileInstallPath = "";
                            switch (file.prop.parent.prop.type) {
                                case Types.NODE_GROUP:
                                    fileInstallPath = file.prop.fileInstallLocation + "/" + file.prop.title;
                                    break;
                                case Types.NODE_SUBGROUP:
                                    fileInstallPath = file.prop.fileInstallLocation + "/"
                                            + file.prop.parent.prop.title + "/" + file.prop.title;
                                    break;
                                case Types.NODE_FOLDER:
                                    fileInstallPath = file.prop.parent.prop.folderLocation + "/" + file.prop.title;
                                    break;
                            }
                            filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)));
                            updateProgress("Updating", file.prop.title, filesIndex, true);
                            AdbOperations.pullFile(fileInstallPath.replaceAll("\\\\", "/"), file.prop.fileSourcePath);
                        }
                    } catch (Exception e) {
                        //need to handle exception in a better way! Device disconnecting doesn't break as the exception is handled in ADB functions.
                        JOptionPane.showMessageDialog(null, "Something went wrong!\nCouldn't update " + file.prop.title + "!");
                    }
                }
                updateProgress("", "Updating Process Completed", 100, false);
                JOptionPane.showMessageDialog(null, "Updating Process Completed");
                updateProgress("", "", 0, false);
                setCardLayout(1);
            } else {
                JOptionPane.showMessageDialog(null, "No Files Found!");
            }
        }).start();
    }

    public void importPackage(FileNode file, String packageName, ProjectItemNode parent, float startFileIndex, float maxFileIndex) {
        float filesIndex = 0;
        String packagePath = AdbOperations.getPackagePath(packageName);
        File f = new File(packagePath);
        ArrayList<String> fileList = new ArrayList<>();
        if (parent.prop.type != Types.NODE_FILE) {
            fileList = getFileList(f.getParent());
        }
        //check here if the device is connected. if filelist size = 1
        //no devices/error: no devices/emulators found
        for (String pullFrom : fileList) {
            filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / fileList.size());
            if (!pullFrom.contains(packagePath)) {
                updateProgress("Pulling", pullFrom, filesIndex, true);
                switch (parent.prop.groupType) {
                    case Types.GROUP_SYSTEM_APK:
                    case Types.GROUP_SYSTEM_PRIV_APK:
                    case Types.GROUP_VENDOR_APP:
                        if (pullFrom.startsWith("/data/app")) {
                            String tempPullFrom = pullFrom.substring("/data/app/".length(), pullFrom.length());
                            tempPullFrom = file.prop.fileInstallLocation + tempPullFrom.substring(tempPullFrom.indexOf("/"), tempPullFrom.length());
                            AdbOperations.pull(pullFrom, tempPullFrom, parent);
                            System.out.println(pullFrom);
                            continue;
                        }
                        break;
                }
                AdbOperations.pull(pullFrom, parent);
                System.out.println(pullFrom);
            } else {
                updateProgress("Updating", file.prop.title, filesIndex, true);
                AdbOperations.pullFile(packagePath, file.prop.fileSourcePath);
            }
        }
        if (fileList.isEmpty()) {
            filesIndex = startFileIndex + (index * (maxFileIndex - startFileIndex));
            updateProgress("Updating", file.prop.title, filesIndex, true);
            AdbOperations.pullFile(packagePath, file.prop.fileSourcePath);
        }
    }

    public void importFiles(ProjectItemNode parent) {
        new Thread(() -> {
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
                        updateProgress("Pulling", pullFrom, fileIndex, true);
                        AdbOperations.pull(pullFrom, parent);
                    }
                }
                if (!Adb.logs.equals("")) {
                    JOptionPane.showMessageDialog(null, Adb.logs);
                }
                if (fileListSize > 0) {
                    updateProgress("", "Files Successfully Imported", 100, false);
                    JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
                    updateProgress("", "", 0, false);
                }
                setCardLayout(1);
            } else {
                JOptionPane.showMessageDialog(null, "Device Not Compatible!");
            }
        }).start();
    }

    public void pushZipToDevice(String source, String destination) {
        if (checkDeviceConnectivity() == 1) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to push file to Device?", "", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                MyTree.setCardLayout(2);
                AdbOperations.pushToDevice(source, destination);
                MyTree.setCardLayout(1);
            }
        }
    }

    public static void updateProgress(String progressTitle, String progressText, float progressValue, boolean increase) {
        String str = progressText;
        if (progressText.length() > 60) {
            str = str.substring(0, progressText.length() / 3) + "..." + str.substring(progressText.length() - 10, progressText.length());
        } else if (progressText.length() > 40) {
            str = str.substring(0, progressText.length() / 2) + "..." + str.substring(progressText.length() - 10, progressText.length());
        }
        MyTree.txtProgressTitle.setText(progressTitle);
        MyTree.txtProgressContent.setText(str);
        if (progressValue != (-1)) {
            MyTree.circularProgressBar.updateProgress(progressValue);
            if (increase) {
                index++;
            }
        }
    }
}
