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
            if (!dir.equals("")) {
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
                    if (!(child.prop.packageType == Types.PACKAGE_APP
                            && (child.prop.title.endsWith(".so") || child.prop.title.endsWith("split_config.en.apk")))) {
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
            if (!(packagePath.contains("no devices/emulators found") || packagePath.contains("error"))) {
                packagePath = packagePath.substring("package:".length(), packagePath.length());
            }
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
        float window = 100 / packageListSize, windowIndex = 0;
        String packagesNotFound = "Following Packages were not found in Device while building Gapps";
        int packageImportStatus = Types.PACKAGE_IMPORT_SUCCESS;
        for (Package p : packages) {
            packageImportStatus = importOnePackage(p, ++windowIndex, window, parent);
            switch (packageImportStatus) {
                case Types.PACKAGE_PATH_NOT_FOUND:
                case Types.PACKAGE_INVALID:
                    packagesNotFound += "\n" + p.packageName;
                    break;
                case Types.DEVICE_ERROR_NOT_AUTHORIZED:
                case Types.DEVICE_ERROR_NOT_CONNECTED:
                    packagesNotFound = "Device Not Connected!\nPlease connect the device and try again!";
                    break;
            }
        }
        if (!packagesNotFound.equals("Following Packages were not found in Device while building Gapps")) {
            JOptionPane.showMessageDialog(null, packagesNotFound);
            updateProgress("", "Process Completed!", 100, false);
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

    public int importOnePackage(Package p, float windowIndex, float window, ProjectItemNode parent) {
        index = 1;
        if (!p.packageName.equals("")) {
            p = validatePackage(p);
            //this executes 2 times in the execution, need to find a way to make it once //p.Optional check is removed, need to add with correct condition
            if (p.packagePath.equals("")) {
                return Types.PACKAGE_PATH_NOT_FOUND;
            } else if (p.packagePath.contains("no devices/emulators found")) {
                return Types.DEVICE_ERROR_NOT_CONNECTED;
            }
            return importFileList(p, windowIndex, window, parent);
        } else {
            if (p.associatedFileList.size() > 0) {
                return importFileList(p, windowIndex, window, parent);
            } else {
                return Types.PACKAGE_INVALID;
            }
        }
    }

    public Package validatePackage(Package p) {
        p.packagePath = getPackagePath(p.packageName);
        if ((p.packagePath.contains("no devices/emulators found") || p.packagePath.equals(""))) {
            return p;
        }
        File f = new File(p.packagePath);
        if (!p.packagePath.startsWith("/data/app")) {
            p.updatedInstalledPath = p.packagePath;
        } else {
            if (p.installedPath.equals("")) {
                String fileName = f.getName().replaceFirst("[.][^.]+$", "");
                if (fileName.endsWith("base.apk")) {
                    //fetch the parent folder name and create a file with that name
                } else {
                    p.updatedInstalledPath = "/system/priv-app/" + fileName + "/" + fileName + ".apk";
                }
            } else {
                p.updatedInstalledPath = p.installedPath;
            }
        }
        ArrayList<String> fileList = new ArrayList<>();
        //if the file is placed in /system/app or /system/priv-app following will prevent fetching all the files of parent folder
        String parentFolder = f.getParent().replaceAll("\\\\", "/");
        if (parentFolder.startsWith("/data/app") || parentFolder.endsWith(f.getName().replaceFirst("[.][^.]+$", ""))) {
            fileList = getFileList(f.getParent());//need to check if parent is not file node
        } else {
            fileList.add(p.packagePath);
        }
        for (String pullFrom : fileList) {
            p.associatedFileList.add(pullFrom);
        }
        return p;
    }

    public int importFileList(Package p, float windowIndex, float window, ProjectItemNode parent) {
        float startFileIndex = (windowIndex - 1) * window, maxFileIndex = (windowIndex * window), filesIndex = 0;
        int pullStatus = Types.DEVICE_PULL_FILE_SUCCESS;
        for (String pullFrom : p.associatedFileList) {
            filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / p.associatedFileList.size());
            updateProgress("Pulling", pullFrom, filesIndex, true);
            if (!pullFrom.contains(p.packagePath)) {
                if (pullFrom.startsWith("/data/app")) {
                    String tempPullFrom = pullFrom.substring("/data/app/".length(), pullFrom.length());
                    tempPullFrom = p.updatedInstalledPath.substring(0, p.updatedInstalledPath.lastIndexOf("/")) + tempPullFrom.substring(tempPullFrom.indexOf("/"), tempPullFrom.length());
                    pullStatus = Device.pull(pullFrom, tempPullFrom, parent);
                    System.out.println(pullFrom);
                } else {
                    pullStatus = Device.pull(pullFrom, parent);
                    System.out.println(pullFrom);
                }
            } else {
                String data[] = p.getImportFilePath(p.updatedInstalledPath, parent);
                String zipPath = data[1];
                String pullTo = data[0];
                pullStatus = Device.pull(pullFrom, pullTo, zipPath, parent);
            }
            if (pullStatus == Types.DEVICE_ERROR_NOT_CONNECTED || pullStatus == Types.DEVICE_ERROR_NOT_AUTHORIZED) {
                return pullStatus;
            }
        }
        return pullStatus;
    }

    public void checkForUpdate(ProjectItemNode parent) {
        ArrayList<FileNode> files = new ArrayList<>();
        files = getFilesToUpdate(parent, files);
        int filesSize = files.size();
        if (filesSize > 0) {
            setCardLayout(2);
            String packagesNotFound = "Something went wrong! Couldn't update following apps..";
            float windowIndex = 0, startFileIndex = 0, maxFileIndex = 0, window = 100 / filesSize;
            int pullStatus = Types.DEVICE_PULL_FILE_SUCCESS;
            for (FileNode file : files) {
                windowIndex++;
                startFileIndex = (windowIndex - 1) * window;
                maxFileIndex = (windowIndex * window);
                index = 1;
                try {
                    if (file.prop.title.endsWith(".apk")) {
                        String packageName = getPackageName(file.prop.fileSourcePath);
                        Package p = new Package();
                        p.packageName = packageName;
                        p.installedPath = file.prop.fileInstallLocation + "/" + file.prop.title;
                        pullStatus = importOnePackage(p, windowIndex, window, parent);
                        switch (pullStatus) {
                            case Types.PACKAGE_PATH_NOT_FOUND:
                            case Types.PACKAGE_INVALID:
                                packagesNotFound += "\n" + p.packageName;
                                break;
                            case Types.DEVICE_ERROR_NOT_AUTHORIZED:
                            case Types.DEVICE_ERROR_NOT_CONNECTED:
                                packagesNotFound = "Device Not Connected!\nPlease connect the device and try again!";
                                break;
                        }
//                        importPackage(file, packageName, parent, startFileIndex, maxFileIndex);
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
                        pullStatus = Device.pullFile(fileInstallPath.replaceAll("\\\\", "/"), file.prop.fileSourcePath);
                        if (pullStatus == Types.DEVICE_ERROR_NOT_CONNECTED || pullStatus == Types.DEVICE_ERROR_NOT_AUTHORIZED) {
                            packagesNotFound = "Device doesn't seem to be connected!\nPlease connect the device and try again!";
                            break;
                        }
                        updateProgress("Updating", file.prop.title, (startFileIndex + ((index * (maxFileIndex - startFileIndex)))), true);
                    }
                } catch (Exception e) {
                    packagesNotFound += "\n" + file.prop.title;
                }
            }
            if (!packagesNotFound.equals("Something went wrong! Couldn't update following apps..")) {
                JOptionPane.showMessageDialog(null, packagesNotFound);
            } else {
                updateProgress("", "Updating Process Completed", 100, false);
                JOptionPane.showMessageDialog(null, "Updating Process Completed");
            }
            updateProgress("", "", 0, false);
            setCardLayout(1);
        } else {
            JOptionPane.showMessageDialog(null, "No Files Found!");
        }
    }

    //eventually when the testing is completed, following function can be removed!
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
            int connectivityFlag = Device.checkDeviceConnectivity(Device.IPAddress);
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
                        int pullStatus = Device.pull(pullFrom, parent);
                        if (pullStatus == Types.DEVICE_ERROR_NOT_CONNECTED || pullStatus == Types.DEVICE_ERROR_NOT_AUTHORIZED) {
                            Adb.logs = "Device Not Connected!\nPlease connect the device and try again!";
                            break;
                        }
                    }
                }
                if (!Adb.logs.equals("")) {
                    JOptionPane.showMessageDialog(null, Adb.logs);
                } else if (fileListSize > 0) {
                    updateProgress("", "Files Successfully Imported", 100, false);
                    JOptionPane.showMessageDialog(null, "Files Successfully Imported!");
                }
                updateProgress("", "", 0, false);
                setCardLayout(1);
            } else {
                JOptionPane.showMessageDialog(null, "Device Not Connected!\nPlease connect the device and try again!");
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
                if (line.get(0).contains("no devices/emulators found")) {
                    line = new ArrayList<>();
                    line.add("no devices/emulators found");
                } else if (line.get(0).contains("No such file or directory")) {
                    line = new ArrayList<>();
                    line.add("No such file or directory");
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
