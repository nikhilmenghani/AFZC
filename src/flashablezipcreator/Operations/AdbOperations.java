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
import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Types;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikhil
 */
public class AdbOperations {

    private static final String[] WIN_RUNTIME = {"cmd.exe", "/C"};
    private static final String[] OS_LINUX_RUNTIME = {"/bin/bash", "-l", "-c"};
    public static TreeOperations to = new TreeOperations();

    public static int checkDeviceConnectivity() {
        int flag = 0;
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list = runProcess(isWin, wait, Commands.COMMAND_ADB_DEVICES);
        try {
            String device = list.get(list.size() - 2);
            String[] devices = device.split("\t");
            if (devices.length > 1) {
                if (devices[1].contains("unauthorized")) {
                    flag = 2;
                    System.out.println("Unauthorized Device Detected with ID: " + devices[0]);
                } else if (devices[1].contains("device")) {
                    flag = 1;
                    System.out.println("Device Detected with ID: " + devices[0]);
                } else {
                    return 0;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception Caught: " + Logs.getExceptionTrace(e));
        }
        return flag;
    }

    public static String getDeviceName() {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list = runProcess(isWin, wait, Commands.COMMAND_ADB_PRODUCT_MODEL);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return "";
        }
    }

    public static boolean pullFile(String pullFrom, String pullTo) {
        java.io.File sFile = new java.io.File(pullTo);
        Write w = new Write();
        String absolutePath = sFile.getAbsolutePath();
        sFile = new java.io.File(absolutePath);
        w.createFolders(sFile.getParent());
        ArrayList<String> pullList = runProcess(true, false, Commands.getAdbPull(pullFrom, pullTo));
        if (pullList.get(0).startsWith("adb: error:")) {
            Adb.logs += pullFrom + ": " + pullList.get(0) + Logs.newLine;
            return false;
        }
        //adb: error: failed to stat remote object ' not running. starting it now at tcp:5037 *': No such file or directory
        //for (String str : pullList) {
        //System.out.println(str);
        //}
        return true;
    }

    public static boolean push(String pushSource, String pushDestination) {
        ArrayList<String> pushList = runProcess(true, false, Commands.getAdbPush(pushSource, pushDestination));
        if (pushList.get(0).startsWith("adb: error:")) {
            Adb.logs += pushSource + ": " + pushList.get(0) + Logs.newLine;
            return false;
        }
        return true;
    }

    public static void pull(String pullFrom, String pullTo, String zipPath, ProjectItemNode parent) {
        Package f = new Package();
        System.out.println(pullTo);
        if (!pullTo.equals("")) {
            try {
                if (AdbOperations.pullFile(pullFrom, pullTo)) {
                    to.addFileNode(zipPath, parent);
                }
            } catch (IOException ex) {
                Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            index += 1;
        }
    }

    public static void pull(String pullFrom, String zipPath, ProjectItemNode parent) {
        Package f = new Package();
        String pullTo = f.getImportFilePath(pullFrom, parent);
        pull(pullFrom, pullTo, zipPath, parent);
    }

    public static void pull(String pullFrom, ProjectItemNode parent) {
        Package f = new Package();
        String pullTo = f.getImportFilePath(pullFrom, parent);
        String zipPath = f.getZipPath(pullFrom);
        pull(pullFrom, pullTo, zipPath, parent);
    }

    public static ArrayList<String> getFileList(String folderPath) {
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

    public static ArrayList<String> getFileList(ArrayList<String> fileList, String path, ArrayList<String> fullList) {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        sb.append(path);
        sb.append('"');
        Commands.COMMAND_LIST_FILES[3] = sb.toString().replace(" ", "\\ ");
        Adb.updateProgress("Scanning: " + path, 0, false);
        ArrayList<String> pathFileList = runProcess(true, false, Commands.COMMAND_LIST_FILES);
        for (String dir : pathFileList) {
            String childPath = path + "/" + dir;
            if (childPath.endsWith("oat")) {
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

    public static ArrayList<Package> getAppList(ArrayList<String> partitionList) {
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
            Adb.updateProgress("Scanning: " + app.installedPath, 0, false);
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

    public static ArrayList<FileNode> getApkFiles(ProjectItemNode parent, ArrayList<FileNode> apkFiles) {
        if (parent.prop.title.endsWith(".apk")) {
            apkFiles.add((FileNode) parent);
            return apkFiles;
        }
        for (ProjectItemNode child : parent.prop.children) {
            switch (child.prop.type) {
                case Types.NODE_GROUP:
                case Types.NODE_SUBGROUP:
                case Types.NODE_FOLDER:
                    apkFiles = getApkFiles(child, apkFiles);
                    break;
                case Types.NODE_FILE:
                    if (child.prop.title.endsWith(".apk")) {
                        apkFiles.add((FileNode) child);
                    }
                    break;
            }
        }
        return apkFiles;
    }

    public static String getPackageName(String filePath) {
        ArrayList<String> list = runProcess(true, false, Commands.getAaptDumpBadging(filePath));
        return list.get(0);
    }

    public static String getPackagePath(String pack) {
        ArrayList<String> list = runProcess(true, false, Commands.getAdbPackagePath(pack));
        return list.get(0);
    }

    public static ArrayList<String> runProcess(boolean isWin, boolean wait, String... command) {
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
//            ArrayList<String> data = runProcess(true, false, Commands.COMMAND_LIST_FILES_SU);
            return line;
        } catch (IOException e) {
            return null;
        } catch (InterruptedException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static <T> T[] concat(T[] first, T[] second) {
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
