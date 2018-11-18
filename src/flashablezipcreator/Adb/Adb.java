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
import static flashablezipcreator.Operations.AdbOperations.updateProgress;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Types;
import java.io.File;
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
        if (packages.size() > 0) {
            adbOp.importPackages(packages, parent);
        }
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

//    public boolean getPackage(String packageName){
//        if (!packageName.equals("")) {
//                String packagePath = adbOp.getPackagePath(packageName);
//                if (packagePath.equals("")) {
//                    return false;
//                }
//                String installPath = "";
//                if (!packagePath.startsWith("/data/app")) {
//                    installPath = packagePath;
//                } else {
//                    installPath = p.installedPath;
//                }
//                File f = new File(packagePath);
//                ArrayList<String> fileList = new ArrayList<>();
//                //if the file is placed in /system/app or /system/priv-app following will prevent fetching all the files of parent folder
//                String parentFolder = f.getParent().replaceAll("\\\\", "/");
//                if (parentFolder.startsWith("/data/app") || parentFolder.endsWith(f.getName().replaceFirst("[.][^.]+$", ""))) {
//                    fileList = getFileList(f.getParent());//need to check if parent is not file node
//                } else {
//                    fileList.add(packagePath);
//                }
//                for (String pullFrom : fileList) {
//                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
//                    updateProgress("Pulling", pullFrom, filesIndex, true);
//                    if (!pullFrom.contains(packagePath)) {
//                        if (pullFrom.startsWith("/data/app")) {
//                            String tempPullFrom = pullFrom.substring("/data/app/".length(), pullFrom.length());
//                            tempPullFrom = p.installedPath.substring(0, p.installedPath.lastIndexOf("/")) + tempPullFrom.substring(tempPullFrom.indexOf("/"), tempPullFrom.length());
//                            Device.pull(pullFrom, tempPullFrom, parent);
//                            System.out.println(pullFrom);
//                            continue;
//                        } else {
//                            Device.pull(pullFrom, parent);
//                            System.out.println(pullFrom);
//                        }
//                    } else {
//                        String data[] = p.getImportFilePath(installPath, parent);
//                        zipPath = data[1];
//                        String pullTo = data[0];
//                        Device.pull(pullFrom, pullTo, zipPath, parent);
//                    }
//                }
//                for (String file : p.associatedFileList) {
//                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / (fileList.size() + p.associatedFileList.size()));
//                    updateProgress("Pulling", file, filesIndex, true);
//                    Device.pull(file, parent);
//                }
//            } else {
//                for (String file : p.associatedFileList) {
//                    filesIndex = startFileIndex + ((index * (maxFileIndex - startFileIndex)) / p.associatedFileList.size());
//                    updateProgress("Pulling", file, filesIndex, true);
//                    Device.pull(file, parent);
//                }
//            }
//    }
}
