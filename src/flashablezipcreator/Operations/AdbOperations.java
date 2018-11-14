/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Adb.Adb;
import static flashablezipcreator.Adb.Adb.index;
import flashablezipcreator.Adb.Package;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class AdbOperations {

    private static final String[] WIN_RUNTIME = {"cmd.exe", "/C"};
    private static final String[] OS_LINUX_RUNTIME = {"/bin/bash", "-l", "-c"};
    public static TreeOperations to = new TreeOperations();

    

    public ArrayList<String> getFileList(String folderPath) {
        folderPath = folderPath.replaceAll("\\\\", "/");
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        sb.append(folderPath);
        sb.append('"');
        Commands.COMMAND_LIST_FILES_RECURSIVELY[4] = sb.toString().replace(" ", "\\ ");
        ArrayList<String> list;
        if (folderPath.equals("/system/media")) {
            Commands.COMMAND_LIST_FILES[3] = sb.toString().replace(" ", "\\ ");
            list = runProcess(true, false, Commands.COMMAND_LIST_FILES);
        } else {
            list = runProcess(true, false, Commands.COMMAND_LIST_FILES_RECURSIVELY);
        }
        ArrayList<String> fList = new ArrayList<>();
        fList = getFileList(fList, folderPath, list);
        return fList;
    }

    public ArrayList<String> getFileList(ArrayList<String> fileList, String path, ArrayList<String> fullList) {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        sb.append(path);
        sb.append('"');
        Commands.COMMAND_LIST_FILES[3] = sb.toString().replace(" ", "\\ ");
        try {
            updateProgress("Scanning Device", path, -1, false);
        } catch (Exception e) {
            System.out.println("Method not found at run time!");
        }
        ArrayList<String> pathFileList = runProcess(true, false, Commands.COMMAND_LIST_FILES);
        for (String dir : pathFileList) {
            String childPath = path + "/" + dir;
            if (childPath.endsWith("oat") || childPath.contains("split_config.en.apk")) {
                continue;
            }
            if (fullList.contains(childPath + ":")) {
                fileList = getFileList(fileList, childPath, fullList);
            } else {
                fileList.add(childPath);
            }
        }
        return fileList;
    }

    public ArrayList<Package> getAppList(ArrayList<String> partitionList) {
        ArrayList<Package> appList = new ArrayList<>();
        ArrayList<String> list = runProcess(true, false, Commands.COMMAND_LIST_PACKAGES_EXTENDED);
        for (String packages : list) {
            Package app = new Package();
            String str = packages.substring(packages.indexOf(":") + 1, packages.length());
            String packageName = str.substring(str.lastIndexOf("=") + 1, str.length());
            String installedPath = str.substring(0, str.lastIndexOf("="));
            String[] data = str.split("=");
            app.installedPath = data[0];
            app.packageName = data[1];
            data[0] = installedPath;
            data[1] = packageName;
            updateProgress("Scanning Device", app.installedPath, -1, false);
            java.io.File file = new java.io.File(data[0]);
            String parent = file.getParent().replaceAll("\\\\", "/") + "/";
            String location = "";
            if (data[0].startsWith("/system/app/")) {
                location = "/system/app";
            } else if (data[0].startsWith("/data/app/")) {
                location = "/data/app";
            } else if (data[0].startsWith("/system/priv-app/")) {
                location = "/system/priv-app";
            } else if (data[0].startsWith("/vendor/app/")) {
                location = "/vendor/app";
            }
            if (location.equals("") || !partitionList.contains(location)) {
                continue;
            }
            location += "/";
//            if (data[0].contains("com.palmerintech.firetube")) {
//                int i = 0;
//            } else {
//                continue;
//            }
            if (parent.equals(location)) {
                app.associatedFileList.add(data[0]);
                appList.add(app);
                continue;
            }
            String temp = data[0].substring(location.length(), data[0].length());
            String folder = "";
            if (temp.contains("/")) {
                folder = location + temp.substring(0, temp.indexOf("/"));
                app.associatedFileList = getFileList(folder);
            }
            appList.add(app);
        }
        return appList;
    }

    public ArrayList<FileNode> getFilesToUpdate(ProjectItemNode parent, ArrayList<FileNode> files) {
        if (parent.prop.type == Types.NODE_FILE) {
            files.add((FileNode) parent);
            return files;
        }
        for (ProjectItemNode child : parent.prop.children) {
            switch (child.prop.type) {
                case Types.NODE_GROUP:
                case Types.NODE_SUBGROUP:
                case Types.NODE_FOLDER:
                    files = getFilesToUpdate(child, files);
                    break;
                case Types.NODE_FILE:
                    if (!(child.prop.packageType == Types.PACKAGE_APP && child.prop.title.endsWith(".so"))) {
                        files.add((FileNode) child);
                    }
                    break;
            }
        }
        return files;
    }

    public String getPackageName(String filePath) {
        ArrayList<String> list = runProcess(true, false, Commands.getAaptDumpBadging(filePath));
        String packageName = list.get(0);
        try {
            packageName = packageName.substring("package: name='".length(), packageName.length());
            packageName = packageName.substring(0, packageName.indexOf("'"));
        } catch (Exception e) {
            packageName = "";
        }
        return packageName;
    }

    public String getPackagePath(String pack) {
        ArrayList<String> list = runProcess(true, false, Commands.getAdbPackagePath(pack));
        if (list.isEmpty()) {
            return "";
        }
        String packagePath = "";
        try {
            packagePath = list.get(0);
            packagePath = packagePath.substring("package:".length(), packagePath.length());
        } catch (Exception e) {
            packagePath = "";
        }
        return packagePath;
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
        String packagesNotFound = "Following Packages were not found in Device while building Gapps";
        for (Package p : packages) {
            windowIndex++;
            startFileIndex = (windowIndex - 1) * window;
            maxFileIndex = (windowIndex * window);
            float filesIndex = 0;
            index = 1;
            String zipPath = "";
            if (!p.packageName.equals("")) {
                String packagePath = getPackagePath(p.packageName);
                if (packagePath.equals("") && p.isOptional == false) {
                    packagesNotFound += "\n" + p.packageName;
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
                //if the file is placed in /system/app or /system/priv-app following will prevent fetching all the files of parent folder
                String parentFolder = f.getParent().replaceAll("\\\\", "/");
                if (parentFolder.startsWith("/data/app") || parentFolder.endsWith(f.getName().replaceFirst("[.][^.]+$", ""))) {
                    fileList = getFileList(f.getParent());//need to check if parent is not file node
                } else {
                    fileList.add(packagePath);
                }
                for (String pullFrom : fileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
                    updateProgress("Pulling", pullFrom, filesIndex, true);
                    if (!pullFrom.contains(packagePath)) {
                        if (pullFrom.startsWith("/data/app")) {
                            String tempPullFrom = pullFrom.substring("/data/app/".length(), pullFrom.length());
                            tempPullFrom = p.installedPath.substring(0, p.installedPath.lastIndexOf("/")) + tempPullFrom.substring(tempPullFrom.indexOf("/"), tempPullFrom.length());
                            Device.pull(pullFrom, tempPullFrom, parent);
                            System.out.println(pullFrom);
                            continue;
                        } else {
                            Device.pull(pullFrom, parent);
                            System.out.println(pullFrom);
                        }
                    } else {
                        String data[] = p.getImportFilePath(installPath, parent);
                        zipPath = data[1];
                        String pullTo = data[0];
                        Device.pull(pullFrom, pullTo, zipPath, parent);
                    }
                }
                for (String file : p.associatedFileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
                    updateProgress("Pulling", file, filesIndex, true);
                    Device.pull(file, parent);
                }
            } else {
                for (String file : p.associatedFileList) {
                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / p.associatedFileList.size());
                    updateProgress("Pulling", file, filesIndex, true);
                    Device.pull(file, parent);
                }
            }
        }
        if (!packagesNotFound.equals("Following Packages were not found in Device while building Gapps")) {
            updateProgress("", "Files Successfully Imported", 100, false);
            JOptionPane.showMessageDialog(null, packagesNotFound);
            updateProgress("", "", 0, false);
        } else {
            if (!Adb.logs.equals("")) {
                JOptionPane.showMessageDialog(null, Adb.logs);
            }
            if (packageListSize > 0) {
                updateProgress("", "Files Successfully Imported", 100, false);
                JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
                updateProgress("", "", 0, false);
            }
        }
        setCardLayout(1);
    }

    public void checkForUpdate(ProjectItemNode parent) {
        new Thread(() -> {
            ArrayList<FileNode> files = new ArrayList<>();
            files = getFilesToUpdate(parent, files);
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
                            String packageName = getPackageName(file.prop.fileSourcePath);
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
                            Device.pullFile(fileInstallPath.replaceAll("\\\\", "/"), file.prop.fileSourcePath);
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
        String packagePath = getPackagePath(packageName);
        File f = new File(packagePath);
        ArrayList<String> fileList = new ArrayList<>();
        if (parent.prop.type != Types.NODE_FILE) {
            //if the file is placed in /system/app or /system/priv-app following will prevent fetching all the files of parent folder
            String parentFolder = f.getParent().replaceAll("\\\\", "/");
            if (parentFolder.startsWith("/data/app") || parentFolder.endsWith(file.prop.title.replaceFirst("[.][^.]+$", ""))) {
                fileList = getFileList(f.getParent());
            } else {
                fileList.add(packagePath);
            }
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
                            Device.pull(pullFrom, tempPullFrom, parent);
                            System.out.println(pullFrom);
                            continue;
                        }
                        break;
                }
                Device.pull(pullFrom, parent);
                System.out.println(pullFrom);
            } else {
                updateProgress("Updating", file.prop.title, filesIndex, true);
                Device.pullFile(packagePath, file.prop.fileSourcePath);
            }
        }
        if (fileList.isEmpty()) {
            filesIndex = startFileIndex + (index * (maxFileIndex - startFileIndex));
            updateProgress("Updating", file.prop.title, filesIndex, true);
            Device.pullFile(packagePath, file.prop.fileSourcePath);
        }
    }

    public void importFiles(ProjectItemNode parent) {
        new Thread(() -> {
            Adb.logs = "";
            boolean connectivityFlag = Device.checkDeviceConnectivity(Device.IPAddress);
            if (connectivityFlag) {
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
                        Device.pull(pullFrom, parent);
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
    
    public ArrayList<String> runProcess(boolean isWin, boolean wait, String... command) {
        System.out.print("command to run: ");
        String[] allCommand = null;
        try {
            if (isWin) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(OS_LINUX_RUNTIME, command);
            }
            for (String s : allCommand) {
                System.out.print(s + " ");
            }
            System.out.println();
            ProcessBuilder pb = new ProcessBuilder(allCommand);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            if (wait) {
                p.waitFor();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp = null;
            ArrayList<String> line = new ArrayList<>();
            while ((_temp = in.readLine()) != null) {
                line.add(_temp);
            }
            if (line.size() > 0) {
                if (line.get(0).equals("error: no devices/emulators found") || line.get(0).contains("error")) {
                    line = new ArrayList<>();
                    //JOptionPane.showMessageDialog(null, "error: no devices/emulators found!");
                }
            }
            return line;
        } catch (IOException e) {
            return null;
        } catch (InterruptedException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    //common errors
//    error: device unauthorized.
//    This adb server's $ADB_VENDOR_KEYS is not set
//    Try 'adb kill-server' if that seems wrong.
//    Otherwise check for a confirmation dialog on your device.
}
