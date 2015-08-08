/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.Operations.JarOperations;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Device {

    static Read r = new Read();
    //public static String selected = null;
    public static String selected = "Samsung Galaxy R (i9103)";
    public static final int CodeName = 1;
    public static final int NeonCompatibility = 2;
    public static final int MountPoint = 3;
    public static ArrayList<String> deviceList = new ArrayList<>();
    public static ArrayList<String> deviceBlackList = new ArrayList<>();//list of device without mountpoint
    public static ArrayList<String> deviceWhiteList = new ArrayList<>();//list of device with mountpoint
    public static byte[] binary = null;
    public static String codeName = null;
    //public static boolean binaryType;
    public static String mountPoint = null;

    //needs to be executed after loading Device
    public static void setSelectedDevice(String device) {
        selected = device;
    }

    //needs to be executed at the start of the program
    public static void loadDeviceList() {
        for (String list : JarOperations.supported_devices.split("\n")) {
            if (list.contains("xxx")) {
                deviceBlackList.add(list.substring(0, list.indexOf("_")));
            } else {
                deviceWhiteList.add(list.substring(0, list.indexOf("_")));
            }
            list = list.substring(0, list.indexOf("_"));
            deviceList.add(list);
        }
    }

    public static boolean isInBlackList() {
        if (deviceBlackList.contains(selected)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInWhiteList() {
        return !isInBlackList();
    }

    public static String getMountPoint() {
        return get(MountPoint);
    }

    public static String getCodeName() {
        return get(CodeName);
    }

    public static String getType() {
        return get(NeonCompatibility);
    }

    public static boolean isNeonCompatible() {
        switch (Device.getType()) {
            case "nonneon":
                return false;
            case "neon":
                return true;
        }
        return true;
    }

    public static String get(int type) {
        for (String list : JarOperations.supported_devices.split("\n")) {
            if (list.contains(selected)) {
                switch (type) {
                    case CodeName:
                        return list.substring(list.indexOf("_") + 1, list.indexOf("_", list.indexOf("_") + 1));
                    case NeonCompatibility:
                        //.equals method strangely not working.
                        String temp[] = list.split("_");
                        if ((temp[temp.length - 1].indexOf("neon") == 3)) {
                            return "nonneon";
                        } else {
                            return "neon";
                        }
                    case MountPoint:
                        return list.substring(list.indexOf("_", list.indexOf("_") + 1) + 1, list.indexOf("_", list.indexOf("_", list.indexOf("_") + 1) + 1));
                }
            }
        }
        return null;
    }

    public static byte[] getBinary() throws IOException {
        String codeName = Device.getCodeName();
        for (String path : Jar.getBinaryList()) {
            if (path.contains(codeName)) {
                return r.getBytesFromFile(JarOperations.getInputStream(path));
            }
        }
        return null;
    }
}
