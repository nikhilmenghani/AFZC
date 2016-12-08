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
import java.awt.CardLayout;
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
            //wz.writeByteToFile(to.getProjectNode(projectName, projectType).update_binary,"Temp" + File.separator + projectName + File.separator + "update-binary");
        }
        wz.close();
    }

    public static void zip() throws IOException, ParserConfigurationException, TransformerException {
        ProjectItemNode rootNode = MyTree.rootNode;
        wz = new WriteZip(Project.outputPath);
        to = new TreeOperations();
        boolean isCustomGroupPresent = false;
        boolean isDeleteGroupPresent = false;
        int fileIndex = 0;
        List<ProjectItemNode> projectNodeList = to.getProjectsSorted(rootNode);
        maxSize = getNodeCount(projectNodeList) + 10; //10 because we write more files than node count
        for (ProjectItemNode project : projectNodeList) {
            if (((ProjectNode) project).projectType != ProjectNode.PROJECT_THEMES) {
                for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                    if (((GroupNode) groupNode).groupType == GroupNode.GROUP_DELETE_FILES) {
                        isDeleteGroupPresent = true;
                        continue;
                    }
                    if (((GroupNode) groupNode).groupType == GroupNode.GROUP_SCRIPT) {
                        for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                            if (node.type == ProjectItemNode.NODE_FILE) {
                                //not yet tested this
                                increaseProgressBar(fileIndex, ((FileNode) node).fileSourcePath);
                                fileIndex++;
                                wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
                            }
                        }
                        continue;
                    }
                    for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                        if (node.type == ProjectItemNode.NODE_SUBGROUP) {
                            for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                                if (((FileNode) fileNode).title.equals("DroidSans.ttf")) {
                                    increaseProgressBar(fileIndex, ((FileNode) fileNode).fileSourcePath);
                                    fileIndex++;
                                    wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, "META-INF/com/google/android/aroma/ttf/" + ((SubGroupNode) node).title + ".ttf");
                                }
                                increaseProgressBar(fileIndex, ((FileNode) fileNode).fileSourcePath);
                                fileIndex++;
                                wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, ((FileNode) fileNode).fileZipPath);
                            }
                        } else if (node.type == ProjectItemNode.NODE_FILE) {
                            increaseProgressBar(fileIndex, ((FileNode) node).fileSourcePath);
                            fileIndex++;
                            wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
                        } else if (node.type == ProjectItemNode.NODE_FOLDER) {
                            ArrayList<FileNode> files = new ArrayList<FileNode>();
                            for (FileNode file : getFilesOfFolder((FolderNode) node, files)) {
                                increaseProgressBar(fileIndex, file.fileSourcePath);
                                fileIndex++;
                                wz.writeFileToZip(file.fileSourcePath, file.fileZipPath);
                            }

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
        if (isCustomGroupPresent) {
            increaseProgressBar(fileIndex, "Custom Group Data");
            fileIndex++;
            //wz.writeStringToZip(Xml.getString(GroupNode.GROUP_CUSTOM, rootNode), Xml.custom_path);
        }
        if (isDeleteGroupPresent) {
            increaseProgressBar(fileIndex, "Delete Group Data");
            fileIndex++;
            //wz.writeStringToZip(Xml.getString(GroupNode.GROUP_DELETE_FILES, rootNode), Xml.delete_path);
        }
        increaseProgressBar(fileIndex, "Zip Data");
        fileIndex++;
        wz.writeStringToZip(Xml.makeString(), Xml.data_path);
        increaseProgressBar(fileIndex, "Aroma Config");
        fileIndex++;
        wz.writeStringToZip(AromaConfig.build(rootNode), AromaConfig.aromaConfigPath);
        increaseProgressBar(fileIndex, "Updater-Script");
        fileIndex++;
        wz.writeStringToZip(UpdaterScript.build(rootNode), UpdaterScript.updaterScriptPath);
        try {
            increaseProgressBar(fileIndex, "Update Binary Installer");
            fileIndex++;
            wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryInstallerPath);
            increaseProgressBar(fileIndex, "Update Binary");
            fileIndex++;
            wz.writeByteToFile(Binary.getUpdateBinary(rootNode), Binary.updateBinaryPath);
            increaseProgressBar(fileIndex, "Jar Items");
            fileIndex++;
            writeTempFiles();
            for (String file : Jar.getOtherFileList()) {
                wz.writeFileToZip(JarOperations.getInputStream(file), file);
            }
        } catch (NullPointerException npe) {
            System.out.println("Executing through Netbeans hence skipping Jar Operations");
        }

        wz.close();
        progressBarImportExport.setValue(100);
        progressBarImportExport.setString("Zip Created Successfully..!!");
        JOptionPane.showMessageDialog(null, "Zip Created Successfully..!!");
        progressBarImportExport.setString("0%");
        progressBarImportExport.setValue(0);
        progressBarFlag = 0;
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

    @Override
    public void run() {
        try {
            CardLayout cardLayout = (CardLayout) panelLower.getLayout();
            cardLayout.show(panelLower, "card2");
            zip();
            cardLayout.show(panelLower, "card1");
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
    public static void writeTempFiles() throws IOException {
        for (String path : Project.getTempFilesList()) {
            wz.writeStringToZip("delete this file", path);
        }
    }

    public static int getNodeCount(List<ProjectItemNode> projectNodeList) {
        int count = 0;
        for (ProjectItemNode project : projectNodeList) {
            for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                    if (node.type == ProjectItemNode.NODE_SUBGROUP) {
                        for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                            count++;
                        }
                    } else if (node.type == ProjectItemNode.NODE_FILE) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
