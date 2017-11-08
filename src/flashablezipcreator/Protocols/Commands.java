/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

/**
 *
 * @author Nikhil
 */
public class Commands {

    public static String[] COMMAND_ADB_DEVICES = {"adb", "devices"};
    public static String[] COMMAND_ADB_PRODUCT_MODEL = {"adb", "shell", "getprop", "ro.product.model"};
    public static String[] COMMAND_ADB_PRODUCT_NAME = {"adb", "shell", "getprop", "ro.product.name"};
    public static String[] COMMAND_ADB_PRODUCT_DEVICE = {"adb", "shell", "getprop", "ro.product.device"};
    private static String[] COMMAND_ADB_PULL = {"adb", "pull", "source", "destination"};
    public static String[] COMMAND_LIST_PACKAGES = {"adb", "shell", "pm", "list", "packages"};
    public static String[] COMMAND_LIST_FILES = {"adb", "shell", "ls", ""};
    public static String[] COMMAND_LIST_FILES_RECURSIVELY = {"adb", "shell", "ls", "-R", ""};
    public static String[] COMMAND_LIST_PACKAGES_EXTENDED = {"adb", "shell", "pm", "list", "packages", "-f"};
    private static String[] COMMAND_AAPT_DUMP_BADGING = {"aapt", "dump", "badging", ""};
    public static String[] COMMAND_LIST_FILES_SU = {"ls", "/data/app"};
    public static String[] COMMAND_ADB_SHELL_SU = {"adb", "shell", "su"};

    public static String[] getAdbPull(String mPath, String sPath) {
        COMMAND_ADB_PULL[2] = "\"" + mPath + "\"";
        COMMAND_ADB_PULL[3] = "\"" + sPath + "\"";
        return COMMAND_ADB_PULL;
    }

    public static String[] getAaptDumpBadging(String apkPath) {
        COMMAND_AAPT_DUMP_BADGING[3] = apkPath;
        return COMMAND_AAPT_DUMP_BADGING;
    }
}
