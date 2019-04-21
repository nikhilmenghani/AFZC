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
import java.util.ArrayList;
import javax.swing.JDialog;

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

    static AdbOperations adbOp = new AdbOperations();

    public static String logs = "";

    public Adb() {

    }

    public static ArrayList<String> runProcess(boolean isWin, boolean wait, String... command) {
        return adbOp.runProcess(isWin, wait, command);
    }

    public void importGapps(ProjectItemNode parent) {
        new Thread(() -> {
            ArrayList<Package> packages = Gapps.getPackage(parent.prop.modType);
            if (packages.size() > 0) {
                adbOp.importPackages(packages, parent);
            }
        }).start();
    }

    public void checkForUpdate(ProjectItemNode parent) {
        new Thread(() -> {
            adbOp.checkForUpdate(parent);
        }).start();
    }

    public void importPackage(FileNode file, String packageName, ProjectItemNode parent, float startFileIndex, float maxFileIndex) {
        adbOp.importPackage(file, packageName, parent, startFileIndex, maxFileIndex);
    }

    public void importFiles(ProjectItemNode parent) {
        adbOp.importFiles(parent);
    }
}
