/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

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
import flashablezipcreator.UserInterface.Preferences;
import java.io.IOException;
import java.util.ArrayList;
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
        boolean isCustomGroupPresent = false;
        boolean isDeleteGroupPresent = false;
        int fileIndex = 0;
//        ArrayList<String> tempPaths = new ArrayList<>();
        List<ProjectItemNode> projectNodeList = to.getProjectsSorted(rootNode);
        maxSize = getNodeCount(to.getNodeList(Types.NODE_FILE)) + 10; //10 because we write more files than node count
        try {
            for (ProjectItemNode project : projectNodeList) {
                if (((ProjectNode) project).prop.projectType != Types.PROJECT_THEMES) {
                    for (ProjectItemNode groupNode : ((ProjectNode) project).prop.children) {
                        if (groupNode.prop.children.isEmpty()) {
                            Logs.write("Removing " + groupNode.prop.title + " as it is empty");
                            groupNode.removeMe();
                        }
                        for (ProjectItemNode node : ((GroupNode) groupNode).prop.children) {
                            if (node.prop.children.isEmpty() && node.prop.type != Types.NODE_FILE) {
                                Logs.write("Removing " + node.prop.title + " as it is empty");
                                node.removeMe();
                            }
                            switch (node.prop.type) {
                                case Types.NODE_SUBGROUP:
                                    for (ProjectItemNode fileNode : ((SubGroupNode) node).prop.children) {
                                        if (((FileNode) fileNode).prop.title.equals("DroidSans.ttf")
                                                || ((FileNode) fileNode).prop.title.equals("Roboto-Regular.ttf")) {
                                            increaseProgressBar(fileIndex, ((FileNode) fileNode).prop.fileSourcePath);
                                            fileIndex++;
                                            wz.writeFileToZip(((FileNode) fileNode).prop.fileSourcePath, "META-INF/com/google/android/aroma/ttf/" + ((SubGroupNode) node).prop.title + ".ttf");
                                        }
                                        increaseProgressBar(fileIndex, ((FileNode) fileNode).prop.fileSourcePath);
                                        fileIndex++;
                                        wz.writeFileToZip(((FileNode) fileNode).prop.fileSourcePath, ((FileNode) fileNode).prop.fileZipPath);
                                    }
                                    break;
                                case Types.NODE_FILE:
                                    increaseProgressBar(fileIndex, ((FileNode) node).prop.fileSourcePath);
                                    fileIndex++;
                                    wz.writeFileToZip(((FileNode) node).prop.fileSourcePath, ((FileNode) node).prop.fileZipPath);
                                    break;
                                case Types.NODE_FOLDER:
                                    ArrayList<FileNode> files = new ArrayList<FileNode>();
                                    for (FileNode file : getFilesOfFolder((FolderNode) node, files)) {
                                        increaseProgressBar(fileIndex, file.prop.fileSourcePath);
                                        fileIndex++;
                                        wz.writeFileToZip(file.prop.fileSourcePath, file.prop.fileZipPath);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (((GroupNode) groupNode).prop.groupType == Types.GROUP_CUSTOM) {
                            isCustomGroupPresent = true;
                        }
                    }
                } else {
                    for (ProjectItemNode groupNode : ((ProjectNode) project).prop.children) {
                        for (ProjectItemNode node : ((GroupNode) groupNode).prop.children) {
                            increaseProgressBar(fileIndex, ((FileNode) node).prop.fileSourcePath);
                            fileIndex++;
                            wz.writeFileToZip(JarOperations.getInputStream(((FileNode) node).prop.fileSourcePath), ((FileNode) node).prop.fileZipPath);
                        }
                    }
                }
            }
            increaseProgressBar(fileIndex, "Zip Data");
            fileIndex++;
            Logs.write("Writing zip data to " + Xml.data_path);
            wz.writeStringToZip(Xml.generateFileDataXml(), Xml.data_path);
            increaseProgressBar(fileIndex, "Aroma Config");
            fileIndex++;
            Logs.write("Building Aroma.config");
            String ac = AromaConfig.build(rootNode);
            Logs.write("Writing Aroma.config");
            wz.writeStringToZip(ac, AromaConfig.aromaConfigPath);
            increaseProgressBar(fileIndex, "Updater-Script");
            fileIndex++;
            try {
                if (Preferences.pp.useUniversalBinary) {
                    Logs.write("Writing updater-script");
                    wz.writeStringToZip("# This is a dummy file. Magic happens in binary file", UpdaterScript.updaterScriptPath);  //updater-script
                    Logs.write("Building update-binary-installer");
                    String ubi = UpdateBinary.build(rootNode);
                    increaseProgressBar(fileIndex, "Update Binary Installer");
                    fileIndex++;
                    Logs.write("Writing update-binary-installer");
                    wz.writeStringToZip(ubi, Binary.updateBinaryInstallerPath); //update-binary-installer
                } else {
                    Logs.write("Building updater-script");
                    String us = UpdaterScript.build(rootNode);
                    Logs.write("Writing updater-script");
                    wz.writeStringToZip(us, UpdaterScript.updaterScriptPath); //updater-script
                    increaseProgressBar(fileIndex, "Update Binary Installer");
                    fileIndex++;
                    Logs.write("Writing update-binary-installer");
                    wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryInstallerPath); //update-binary-installer
                }
                increaseProgressBar(fileIndex, "Update Binary Installer");
                fileIndex++;
                increaseProgressBar(fileIndex, "Update Binary");
                fileIndex++;
                Logs.write("Writing update-binary");
                wz.writeByteToFile(Binary.getUpdateBinary(rootNode), Binary.updateBinaryPath);
                increaseProgressBar(fileIndex, "Jar Items");
                fileIndex++;
                Logs.write("Writing Rest of Jar Files");
                for (String file : Jar.getOtherFileList()) {
                    wz.writeFileToZip(JarOperations.getInputStream(file), file);
                }
            } catch (NullPointerException npe) {
                System.out.println("Executing through Netbeans hence skipping Jar Operations");
            }
            wz.close();
            Logs.write("Zip Created Successfully..");
            progressBarImportExport.setValue(100);
            progressBarImportExport.setString("Zip Created Successfully..!!");
            JOptionPane.showMessageDialog(null, "Zip Created Successfully..!!");
            progressBarImportExport.setString("0%");
            progressBarImportExport.setValue(0);
            progressBarFlag = 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Went Wrong!\nShare logs with developer!\n" + Logs.getExceptionTrace(e));
            Logs.write(Logs.getExceptionTrace(e));
            MyTree.setCardLayout(1);
        }
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
