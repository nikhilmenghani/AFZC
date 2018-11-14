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
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.setCardLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

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
            switch (parent.prop.modType) {
                case Types.GAPPS_CORE:
                    importPackages(Gapps.getCoreList(), parent);
                    break;
                case Types.GAPPS_PICO:
                    importPackages(Gapps.getPicoList(), parent);
                    break;
                case Types.GAPPS_MY:
                    importPackages(Gapps.getMyList(), parent);
                    break;
                case Types.GAPPS_NANO:
                    importPackages(Gapps.getNanoList(), parent);
                    break;
            }
        }).start();
    }

    public static void importPackages(ArrayList<Package> packages, ProjectItemNode parent) {
        adbOp.importPackages(packages, parent);
    }

    public void checkForUpdate(ProjectItemNode parent) {
        adbOp.checkForUpdate(parent);
    }

    public void importPackage(FileNode file, String packageName, ProjectItemNode parent, float startFileIndex, float maxFileIndex) {
        adbOp.importPackage(file, packageName, parent, startFileIndex, maxFileIndex);
    }

    public void importFiles(ProjectItemNode parent) {
        adbOp.importFiles(parent);
    }
}
