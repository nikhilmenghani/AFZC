/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.DeleteNode;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.DiskOperations.WriteZip;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.progressBarFlag;
import static flashablezipcreator.UserInterface.MyTree.progressBarImportExport;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import static flashablezipcreator.UserInterface.MyTree.circularProgressBar;
import static flashablezipcreator.UserInterface.MyTree.txtProgress;
import flashablezipcreator.UserInterface.Preference;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Nikhil
 */
public class Export implements Runnable {

    static WriteZip wz;
    static TreeOperations to;
    static int progressValue = 0;
    static int maxSize = 0;

    public static void createZip(ArrayList<ProjectItemNode> fileNode) throws IOException {
        wz = new WriteZip(Project.outputPath);
        for (ProjectItemNode node : fileNode) {
            //this will simply take each file from source and create the same file in zip at specified destination path.
            wz.writeFileToZip(((FileNode) node).prop.fileSourcePath, ((FileNode) node).prop.fileZipPath);
        }
        wz.close();
    }

    public static void zip() throws IOException, ParserConfigurationException, TransformerException {
        Logs.write("Trying to export zip to path: " + Project.outputPath);
        ProjectItemNode rootNode = MyTree.rootNode;
        wz = new WriteZip(Project.outputPath);
        to = new TreeOperations();
        txtProgress.setText("");
        int fileIndex = 0;
//        ArrayList<String> tempPaths = new ArrayList<>();
        List<ProjectItemNode> projectNodeList = to.getProjectsSorted(rootNode);
        maxSize = getNodeCount(to.getNodeList(Types.NODE_FILE)) + 10; //10 because we write more files than node count
        try {
            if (isNormalZipValidated(projectNodeList)) {
                for (ProjectItemNode project : projectNodeList) {
                    if (((ProjectNode) project).prop.projectType != Types.PROJECT_THEMES) {
                        for (ProjectItemNode groupNode : ((ProjectNode) project).prop.children) {
                            if (groupNode.prop.children.isEmpty()) {
                                Logs.write("Removing " + groupNode.prop.title + " as it is empty");
                                groupNode.removeMe();
                            }
                            if (groupNode.prop.groupType != Types.GROUP_DELETE_FILES) {
                                for (ProjectItemNode node : ((GroupNode) groupNode).prop.children) {
                                    if (node.prop.children.isEmpty() && node.prop.type != Types.NODE_FILE) {
                                        Logs.write("Removing " + node.prop.title + " as it is empty");
                                        node.removeMe();
                                    }
                                    switch (node.prop.type) {
                                        case Types.NODE_SUBGROUP:
                                            for (ProjectItemNode fileNode : ((SubGroupNode) node).prop.children) {
                                                if (Preference.pp.createZipType.equals("Aroma")
                                                        && (((FileNode) fileNode).prop.title.equals("DroidSans.ttf")
                                                        || ((FileNode) fileNode).prop.title.equals("Roboto-Regular.ttf"))) {
                                                    increaseProgressBar(fileIndex++, ((FileNode) fileNode).prop.fileSourcePath);
                                                    wz.writeFileToZip(((FileNode) fileNode).prop.fileSourcePath, "META-INF/com/google/android/aroma/ttf/" + ((SubGroupNode) node).prop.title + ".ttf");
                                                }
                                                increaseProgressBar(fileIndex++, ((FileNode) fileNode).prop.fileSourcePath);
                                                wz.writeFileToZip(((FileNode) fileNode).prop.fileSourcePath, ((FileNode) fileNode).prop.fileZipPath);
                                            }
                                            break;
                                        case Types.NODE_FILE:
                                            increaseProgressBar(fileIndex++, ((FileNode) node).prop.fileSourcePath);
                                            wz.writeFileToZip(((FileNode) node).prop.fileSourcePath, ((FileNode) node).prop.fileZipPath);
                                            break;
                                        case Types.NODE_FOLDER:
                                            ArrayList<FileNode> files = new ArrayList<>();
                                            for (FileNode file : getFilesOfFolder((FolderNode) node, files)) {
                                                increaseProgressBar(fileIndex++, file.prop.fileSourcePath);
                                                wz.writeFileToZip(file.prop.fileSourcePath, file.prop.fileZipPath);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            if (((GroupNode) groupNode).prop.groupType == Types.GROUP_DELETE_FILES) {
                                String writeAt = ((GroupNode) groupNode).prop.zipPath + "/DeleteFilesPath";
                                wz.writeStringToZip(getDeleteData((GroupNode) groupNode), writeAt);
                            }
                        }
                    } else if (Preference.pp.createZipType.equals("Aroma")) {
                        for (ProjectItemNode groupNode : ((ProjectNode) project).prop.children) {
                            for (ProjectItemNode node : ((GroupNode) groupNode).prop.children) {
                                increaseProgressBar(fileIndex++, ((FileNode) node).prop.fileSourcePath);
                                wz.writeFileToZip(JarOperations.getInputStream(((FileNode) node).prop.fileSourcePath), ((FileNode) node).prop.fileZipPath);
                            }
                        }
                    }
                }
                increaseProgressBar(fileIndex++, "Zip Data");
                Logs.write("Writing zip data to " + Xml.data_path);
                wz.writeStringToZip(Xml.generateFileDataXml(), Xml.data_path);
                if (projectNodeList.size() > 0) {
                    if (Preference.pp.createZipType.equals("Aroma")) {
                        Logs.write("Writing Rest of Jar Files");
                        for (String file : Jar.getOtherFileList()) {
                            wz.writeFileToZip(JarOperations.getInputStream(file), file);
                        }
                        increaseProgressBar(fileIndex++, "Aroma Config");
                        String ac = AromaConfig.build(rootNode);
                        Logs.write("Writing Aroma.config");
                        wz.writeStringToZip(ac, AromaConfig.aromaConfigPath);
                    }
                    increaseProgressBar(fileIndex++, "Updater-Script");
                    try {
                        if (Preference.pp.useUniversalBinary) {
                            Logs.write("Writing updater-script");
                            wz.writeStringToZip("# This is a dummy file. Magic happens in binary file", UpdaterScript.updaterScriptPath);  //updater-script
                            increaseProgressBar(fileIndex++, "Update Binary");
                            Logs.write("Writing update-binary");
                            switch (Preference.pp.createZipType) {
                                case "Aroma":
                                    wz.writeByteToFile(Binary.getUpdateBinary(rootNode), Binary.updateBinaryPath);
                                    increaseProgressBar(fileIndex++, "Update Binary Installer");
                                    Logs.write("Writing update-binary-installer");
                                    wz.writeStringToZip(UpdateBinary.build(rootNode), Binary.updateBinaryInstallerPath); //update-binary-installer
                                    break;
                                case "Normal":
                                    wz.writeStringToZip(UpdateBinary.build(rootNode), Binary.updateBinaryPath); //update-binary-installer
                                    break;
                            }

                        } else {
                            String us = UpdaterScript.build(rootNode);
                            Logs.write("Writing updater-script");
                            wz.writeStringToZip(us, UpdaterScript.updaterScriptPath); //updater-script
                            increaseProgressBar(fileIndex++, "Update Binary");
                            Logs.write("Writing update-binary");
                            switch (Preference.pp.createZipType) {
                                case "Aroma":
                                    wz.writeByteToFile(Binary.getUpdateBinary(rootNode), Binary.updateBinaryPath);
                                    increaseProgressBar(fileIndex++, "Update Binary Installer");
                                    Logs.write("Writing update-binary-installer");
                                    wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryInstallerPath); //update-binary-installer
                                    break;
                                case "Normal":
                                    wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryPath); //update-binary-installer
                                    break;
                            }
                        }
                        increaseProgressBar(fileIndex++, "Update Binary Installer");
                        increaseProgressBar(fileIndex++, "Jar Items");
                        Logs.write("Writing AFZC Binary");
                        increaseProgressBar(fileIndex++, "AFZC Binary");
                        wz.writeStringToZip(Script.getAfzcString(), Script.afzcScriptZipPath);
                        Logs.write("Writing Addon Binary");
                        increaseProgressBar(fileIndex++, "Addon Binary");
                        wz.writeStringToZip(Script.getAddonString(), Script.addonScriptZipPath);
                    } catch (NullPointerException npe) {
                        System.out.println("Executing through Netbeans hence skipping Jar Operations");
                    }
                    txtProgress.setText("Just a moment!");
                }
                wz.close();
                Logs.write("Zip Created Successfully..");
                txtProgress.setText("Zip Created Successfully..");
                circularProgressBar.updateProgress(100);
                progressBarImportExport.setValue(100);
                progressBarImportExport.setString("Zip Created Successfully..!!");
                JOptionPane.showMessageDialog(null, "Zip Created Successfully..!!");
                progressBarImportExport.setString("0%");
                progressBarImportExport.setValue(0);
                txtProgress.setText("");
                circularProgressBar.updateProgress(0);
                progressBarFlag = 0;
            }
        } catch (HeadlessException | IOException | ParserConfigurationException | TransformerException e) {
            JOptionPane.showMessageDialog(null, "Something Went Wrong!\nShare logs with developer!\n" + Logs.getExceptionTrace(e));
            Logs.write(Logs.getExceptionTrace(e));
            MyTree.setCardLayout(1);
        }
    }

    public static boolean isNormalZipValidated(List<ProjectItemNode> projectNodeList) {
        if (Preference.pp.createZipType.equals("Normal")) {
            for (ProjectItemNode project : projectNodeList) {
                if (((ProjectNode) project).prop.projectType != Types.PROJECT_THEMES) {
                    for (ProjectItemNode groupNode : ((ProjectNode) project).prop.children) {
                        if (groupNode.prop.isSelectBox && groupNode.prop.children.size() > 1) {
                            JOptionPane.showMessageDialog(null, "Create zip type is set to Normal in preferences.\n"
                                    + groupNode.prop.title + " can have only one child group in this case.!\n"
                                    + "Set Create zip type to Aroma in preferences if you want multiple child groups");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String getDeleteData(GroupNode dNode) {
        String str = "";
        for (Iterator<ProjectItemNode> it = dNode.prop.children.iterator(); it.hasNext();) {
            DeleteNode node = (DeleteNode) it.next();
            str += node.getDeleteLocation() + "\n";
        }
        return str.substring(0, str.length() - 1);
    }

    public static ArrayList<FileNode> getFilesOfFolder(FolderNode folder, ArrayList<FileNode> fileList) {
        for (ProjectItemNode node : folder.prop.children) {
            if (node.prop.type == Types.NODE_FOLDER) {
                fileList = getFilesOfFolder((FolderNode) node, fileList);
            }
            if (node.prop.type == Types.NODE_FILE) {
                fileList.add((FileNode) node);
            }
        }
        return fileList;
    }

    @Override
    public void run() {
        try {
            MyTree.setCardLayout(2);
            zip();
            MyTree.setCardLayout(1);
        } catch (IOException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void increaseProgressBar(int fileIndex, String fileName) {
        progressValue = (fileIndex * 100) / maxSize;
        if (progressValue > 99) {
            progressValue = 99;
        }
        String str = (new File(fileName)).getName();
        if (str.length() > 60) {
            str = str.substring(0, str.length() / 3) + "..." + str.substring(str.length() - 10, str.length());
        } else if (str.length() > 40) {
            str = str.substring(0, str.length() / 2) + "..." + str.substring(str.length() - 10, str.length());
        }
        txtProgress.setText("Exporting " + str);
        circularProgressBar.updateProgress(progressValue);
        progressBarImportExport.setValue(progressValue);
        switch (MyTree.progressBarFlag) {
            case 0:
                progressBarImportExport.setString(progressValue + "%");
                break;
            case 1:
                progressBarImportExport.setString("Exporting " + fileName + "");
                break;
            case 2:
                progressBarImportExport.setString(" ");
                break;
        }
    }

    //this is required to fix status 7 error while installing Rom
    public static void writeTempFiles(ArrayList<String> tempFiles) throws IOException {
        for (String path : tempFiles) {
            wz.writeStringToZip("delete this file", path);
        }
    }

    public static int getNodeCount(ArrayList<ProjectItemNode> fileList) {
        int count = 0;
        for (ProjectItemNode file : fileList) {
            count++;
        }
        return count;
    }
}
