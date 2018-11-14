/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Adb.Adb;
import static flashablezipcreator.Adb.Adb.index;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.DiskOperations.Write;
import static flashablezipcreator.Operations.AdbOperations.to;
import flashablezipcreator.Protocols.Commands;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.UserInterface.MyTree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil Menghani
 */
public class DeviceOperations {

    private static final String[] WIN_RUNTIME = {"cmd.exe", "/C"};
    private static final String[] OS_LINUX_RUNTIME = {"/bin/bash", "-l", "-c"};

    public int checkDeviceConnectivity(String ipAddress) {
        Project.androidVersion = "5.x+";
        int flag = -1;
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list;
        if (ipAddress.startsWith("192.168")) {
            list = Adb.runProcess(isWin, wait, Commands.getAdbConnectCommand(ipAddress));
        } else {
            list = Adb.runProcess(isWin, wait, Commands.COMMAND_ADB_DEVICES);
        }
        for (String response : list) {
            if (response.endsWith("device") || response.endsWith("recovery") || response.contains("connected to 192.168.")) {
                Project.androidVersion = getDeviceAndroidVersion();
                return 1;
            }
            if (response.endsWith("unauthorized")) {
                return 2;
            }
            if (response.equals("") || response.contains("A connection attempt failed because the connected party did not properly respond after a period of time")) {
                return 3;
            }
        }
        return flag;
    }

    public void quickConnect() {
        int i = checkDeviceConnectivity("");
        if (i == 3) {
            String IP = "10";
            i = checkDeviceConnectivity("192.168.0." + IP + ":5555");
            if (i == 3) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Couldn't Connect, Do you want to try with 192.168.0.11:5555?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    IP = "11";
                    i = checkDeviceConnectivity("192.168.0." + IP + ":5555");
                    if (i == 3) {
                        dialogResult = JOptionPane.showConfirmDialog(null, "Couldn't Connect, Do you want to try with 192.168.0.15:5555?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            IP = "15";
                            i = checkDeviceConnectivity("192.168.0." + IP + ":5555");
                            if (i == 3) {
                                JOptionPane.showMessageDialog(null, "Failed to connect, only logs can help now!");
                            }
                        }
                    }
                }
            }
        }
        switch (i) {
            case 1:
                JOptionPane.showMessageDialog(null, "Device Is Connected!");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Device Unauthorized, please allow the device to connect to this computer!");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Cannot Connect To Device\n"
                        + "Please make sure your device is connected via USB or same Wifi network and you have allowed the device to connect to your computer!");
                break;
        }
    }

    public String getDeviceName() {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list = Adb.runProcess(isWin, wait, Commands.COMMAND_ADB_PRODUCT_MODEL);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return "";
        }
    }

    public String getDeviceAndroidVersion() {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list = Adb.runProcess(isWin, wait, Commands.COMMAND_ANDROID_VERSION);
        if (list.size() > 0) {
            return String.valueOf(list.get(0));
        } else {
            return "0";
        }
    }

    public String getDeviceArchitecture() {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("windows");
        boolean wait = false;
        ArrayList<String> list = Adb.runProcess(isWin, wait, Commands.COMMAND_DEVICE_ARCHITECHTURE);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return "";
        }
    }

    public boolean pushToDevice(String pushSource, String pushDestination) {
        ArrayList<String> pushList = pushFileToDevice(true, false, pushSource, pushDestination);
        if (pushList.get(0).startsWith("adb: error:")) {
            Adb.logs += pushSource + ": " + pushList.get(0) + Logs.newLine;
            return false;
        }
        return true;
    }

    public void pushZipToDevice(String source, String destination) {
        if (Device.checkDeviceConnectivity(Device.IPAddress)) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to push file to Device?", "", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                MyTree.setCardLayout(2);
                Device.pushToDevice(source, destination);
                MyTree.setCardLayout(1);
            }
        }

    }

    public boolean pullFile(String pullFrom, String pullTo) {
        java.io.File sFile = new java.io.File(pullTo);
        Write w = new Write();
        String absolutePath = sFile.getAbsolutePath();
        sFile = new java.io.File(absolutePath);
        w.createFolders(sFile.getParent());
        ArrayList<String> pullList = Adb.runProcess(true, false, Commands.getAdbPull(pullFrom, pullTo));
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

    public boolean push(String pushSource, String pushDestination) {
        ArrayList<String> pushList = Adb.runProcess(true, false, Commands.getAdbPush(pushSource, pushDestination));
        if (pushList.get(0).startsWith("adb: error:")) {
            Adb.logs += pushSource + ": " + pushList.get(0) + Logs.newLine;
            return false;
        }
        return true;
    }

    public void pull(String pullFrom, String pullTo, String zipPath, ProjectItemNode parent) {
        flashablezipcreator.Adb.Package f = new flashablezipcreator.Adb.Package();
        System.out.println(pullTo);
        if (!pullTo.equals("")) {
            try {
                if (pullFile(pullFrom, pullTo)) {
                    to.addFileNode(zipPath, parent);
                }
            } catch (IOException ex) {
                Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            index += 1;
        }
    }

    public void pull(String pullFrom, ProjectItemNode parent) {
        flashablezipcreator.Adb.Package f = new flashablezipcreator.Adb.Package();
        String data[] = f.getImportFilePath(pullFrom, parent);
        String pullTo = data[0];
        String zipPath = data[1];
        if (!pullTo.equals("") && !zipPath.equals("")) {
            pull(pullFrom, pullTo, zipPath, parent);
        }
    }

    public void pull(String dataPullFrom, String systemPullFrom, ProjectItemNode parent) {
        flashablezipcreator.Adb.Package f = new flashablezipcreator.Adb.Package();
        String data[] = f.getImportFilePath(systemPullFrom, parent);
        String pullTo = data[0];
        String zipPath = data[1];
        if (!pullTo.equals("") && !zipPath.equals("")) {
            pull(dataPullFrom, pullTo, zipPath, parent);
        }
    }

    public static ArrayList<String> pushFileToDevice(boolean isWin, boolean wait, String source, String destination) {
        System.out.print("command to run: ");
        String[] allCommand = null;
        try {
            if (isWin) {
                allCommand = concat(WIN_RUNTIME, Commands.getAdbPush(source, destination));
            } else {
                allCommand = concat(OS_LINUX_RUNTIME, Commands.getAdbPush(source, destination));
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
                if (_temp.contains("[") && _temp.contains("]")) {
                    try {
                        String updateP = _temp.substring(_temp.indexOf("["), _temp.indexOf("]"));
                        updateP = updateP.replace("[", "");
                        updateP = updateP.replace("]", "");
                        updateP = updateP.trim();
                        if (updateP.endsWith("%")) {
                            updateP = updateP.substring(0, updateP.length() - 1);
                        }
                        AdbOperations.updateProgress("Pushing to", destination, Float.valueOf(updateP), false);
                    } catch (Exception E) {
                        AdbOperations.updateProgress("", "Exception!", 0, true);
                    }
                }
                line.add(_temp);
            }
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
}
