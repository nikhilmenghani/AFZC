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
import flashablezipcreator.MyTree;
import static flashablezipcreator.MyTree.panelLower;
import static flashablezipcreator.MyTree.progressBarFlag;
import static flashablezipcreator.MyTree.progressBarImportExport;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.UserInterface.Preferences;
import java.awt.CardLayout;
import java.awt.HeadlessException;
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
            wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
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
        maxSize = getNodeCount(to.getNodeList(ProjectItemNode.NODE_FILE)) + 10; //10 because we write more files than node count
        try {
            for (ProjectItemNode project : projectNodeList) {
                if (((ProjectNode) project).projectType != ProjectNode.PROJECT_THEMES) {
                    for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                        if (groupNode.children.isEmpty()) {
                            Logs.write("Removing " + groupNode.title + " as it is empty");
                            groupNode.removeMe();
                        }
                        for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                            if (node.children.isEmpty() && node.type != ProjectItemNode.NODE_FILE) {
                                Logs.write("Removing " + node.title + " as it is empty");
                                node.removeMe();
                            }
                            switch (node.type) {
                                case ProjectItemNode.NODE_SUBGROUP:
                                    for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                                        if (((FileNode) fileNode).title.equals("DroidSans.ttf")
                                                || ((FileNode) fileNode).title.equals("Roboto-Regular.ttf")) {
                                            increaseProgressBar(fileIndex, ((FileNode) fileNode).fileSourcePath);
                                            fileIndex++;
                                            wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, "META-INF/com/google/android/aroma/ttf/" + ((SubGroupNode) node).title + ".ttf");
                                        }
                                        increaseProgressBar(fileIndex, ((FileNode) fileNode).fileSourcePath);
                                        fileIndex++;
                                        wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, ((FileNode) fileNode).fileZipPath);
                                    }
                                    break;
                                case ProjectItemNode.NODE_FILE:
                                    increaseProgressBar(fileIndex, ((FileNode) node).fileSourcePath);
                                    fileIndex++;
                                    wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
                                    break;
                                case ProjectItemNode.NODE_FOLDER:
                                    ArrayList<FileNode> files = new ArrayList<FileNode>();
                                    for (FileNode file : getFilesOfFolder((FolderNode) node, files)) {
                                        increaseProgressBar(fileIndex, file.fileSourcePath);
                                        fileIndex++;
                                        wz.writeFileToZip(file.fileSourcePath, file.fileZipPath);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (((GroupNode) groupNode).groupType == GroupNode.GROUP_CUSTOM) {
                            isCustomGroupPresent = true;
                        }
                    }
                } else {
                    for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                        for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                            increaseProgressBar(fileIndex, ((FileNode) node).fileSourcePath);
                            fileIndex++;
                            wz.writeFileToZip(JarOperations.getInputStream(((FileNode) node).fileSourcePath), ((FileNode) node).fileZipPath);
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
            Logs.write("Building updater-script");
            String us = UpdaterScript.build(rootNode);
            Logs.write("Writing updater-script");
            wz.writeStringToZip(us, UpdaterScript.updaterScriptPath);
            try {
                Logs.write("Writing update-binary-installer");
                wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryInstallerPath);
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
            setCardLayout(1);
        }
    }

    public static ArrayList<FileNode> getFilesOfFolder(FolderNode folder, ArrayList<FileNode> fileList) {
        for (ProjectItemNode node : folder.children) {
            if (node.type == ProjectItemNode.NODE_FOLDER) {
                fileList = getFilesOfFolder((FolderNode) node, fileList);
            }
            if (node.type == ProjectItemNode.NODE_FILE) {
                fileList.add((FileNode) node);
            }
        }
        return fileList;
    }

    public static void setCardLayout(int cardNo) {
        CardLayout cardLayout = (CardLayout) panelLower.getLayout();
        cardLayout.show(panelLower, "card" + Integer.toString(cardNo));
    }

    @Override
    public void run() {
        try {
            setCardLayout(2);
            zip();
            setCardLayout(1);
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
