/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;


import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Operations.DeviceOperations;
import java.io.IOException;

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

    public static int checkDeviceConnectivity(String ipAddress) {
        return dop.checkDeviceConnectivity(ipAddress);
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

    public static int pullFile(String pullFrom, String pullTo) {
        return dop.pullFile(pullFrom, pullTo);
    }

    public void pushZipToDevice(String source, String destination) {
        
    }

    public static boolean push(String pushSource, String pushDestination) {
        return dop.push(pushSource, pushDestination);
    }

    public static int pull(String pullFrom, String pullTo, String zipPath, ProjectItemNode parent) {
        return dop.pull(pullFrom, pullTo, zipPath, parent);
    }

    public static int pull(String pullFrom, ProjectItemNode parent) {
        return dop.pull(pullFrom, parent);
    }

    public static int pull(String dataPullFrom, String systemPullFrom, ProjectItemNode parent) {
        return dop.pull(dataPullFrom, systemPullFrom, parent);
    }
}
