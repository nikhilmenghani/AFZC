/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;


import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Operations.DeviceOperations;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Device {

    static DeviceOperations dop = new DeviceOperations();
    public static byte[] binary = null;
    public static String IPAddress = "";

    public static byte[] getBinary() throws IOException {
        if (Device.binary != null) {
            return Device.binary;
        }
        return null;
    }

    public static boolean checkDeviceConnectivity(String ipAddress) {
        int status = dop.checkDeviceConnectivity(ipAddress);
        switch (status) {
            case 2:
                JOptionPane.showMessageDialog(null, "Device is Unauthorized!");
                return false;
            case 3:
                JOptionPane.showMessageDialog(null, "A connection attempt failed because the connected party did not properly respond after a period of time");
        }
        return true;
    }

    public static void quickConnect() {
        dop.quickConnect();
    }

    public static void connectWithUSB() {
        checkDeviceConnectivity("");
    }

    public static String getName() {
        return dop.getDeviceName();
    }

    public static String getAndroidVersion() {
        return dop.getDeviceAndroidVersion();
    }

    public static String getArchitecture() {
        return dop.getDeviceArchitecture();
    }

    public static boolean pushToDevice(String pushSource, String pushDestination) {
        return dop.pushToDevice(pushSource, pushDestination);
    }

    public static boolean pullFile(String pullFrom, String pullTo) {
        return dop.pullFile(pullFrom, pullTo);
    }

    public void pushZipToDevice(String source, String destination) {
        
    }

    public static boolean push(String pushSource, String pushDestination) {
        return dop.push(pushSource, pushDestination);
    }

    public static void pull(String pullFrom, String pullTo, String zipPath, ProjectItemNode parent) {
        dop.pull(pullFrom, pullTo, zipPath, parent);
    }

    public static void pull(String pullFrom, ProjectItemNode parent) {
        dop.pull(pullFrom, parent);
    }

    public static void pull(String dataPullFrom, String systemPullFrom, ProjectItemNode parent) {
        dop.pull(dataPullFrom, systemPullFrom, parent);
    }
}
