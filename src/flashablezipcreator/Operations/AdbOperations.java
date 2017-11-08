/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Adb.Adb;
import flashablezipcreator.Adb.App;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Logs;
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

    public static ArrayList<App> getAppList(ArrayList<String> partitionList) {
        ArrayList<App> appList = new ArrayList<>();
        ArrayList<String> list = runProcess(true, false, Commands.COMMAND_LIST_PACKAGES_EXTENDED);
        for (String packages : list) {
            App app = new App();
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

    public static String getAppName(String appPath) {
        String Name = "";
        return Name;
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
