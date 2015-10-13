/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import static flashablezipcreator.AFZC.Protocols.show;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.DiskOperations.WriteZip;
import flashablezipcreator.MyTree;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.Operations.UpdaterScriptOperations;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Nikhil
 */
public class Export {

    static WriteZip wz;
    static TreeOperations to;

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
        to = new TreeOperations(rootNode);
        boolean isCustomGroupPresent = false;
        boolean isDeleteGroupPresent = false;
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
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
                                wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
                                //wz.writeStringToZip(UpdaterScript.getDpiScript(((FileNode) node).title), ((FileNode) node).fileZipPath);
                            }
                        }
                        continue;
                    }
                    for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                        if (node.type == ProjectItemNode.NODE_SUBGROUP) {
                            for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                                if (((FileNode) fileNode).title.equals("DroidSans.ttf")) {
                                    wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, "META-INF/com/google/android/aroma/ttf/" + ((SubGroupNode) node).title + ".ttf");
                                }
                                wz.writeFileToZip(((FileNode) fileNode).fileSourcePath, ((FileNode) fileNode).fileZipPath);
                            }
                        } else if (node.type == ProjectItemNode.NODE_FILE) {

                            wz.writeFileToZip(((FileNode) node).fileSourcePath, ((FileNode) node).fileZipPath);
                            //show("hi1");
                        }
                        //show("hi2");
                    }
                    //show("hi3");
                    if (((GroupNode) groupNode).groupType == GroupNode.GROUP_CUSTOM) {
                        isCustomGroupPresent = true;
                    } else if (((GroupNode) groupNode).groupType == GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP) {
                        //show("hi4");
                        wz.writeStringToZip(UpdaterScript.symlinkScript, UpdaterScript.symlinkScriptPath);
                    }
                }
            } else {
                for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                    for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                        //show(((FileNode) node).fileSourcePath + " " + ((FileNode) node).fileZipPath);
                        wz.writeFileToZip(JarOperations.getInputStream(((FileNode) node).fileSourcePath), ((FileNode) node).fileZipPath);
                        //show(((FileNode) node).fileSourcePath + " " + ((FileNode) node).fileZipPath);
                    }
                }
            }
        }
        if (isCustomGroupPresent) {
            wz.writeStringToZip(Xml.getString(GroupNode.GROUP_CUSTOM, rootNode), Xml.custom_path);
        }
        if (isDeleteGroupPresent) {
            wz.writeStringToZip(Xml.getString(GroupNode.GROUP_DELETE_FILES, rootNode), Xml.delete_path);
        }
        wz.writeStringToZip(Xml.getString(0, rootNode), Xml.data_path);
        wz.writeStringToZip(AromaConfig.build(rootNode), AromaConfig.aromaConfigPath);
        wz.writeStringToZip(UpdaterScript.build(rootNode), UpdaterScript.updaterScriptPath);
        try {
            wz.writeByteToFile(Binary.getInstallerBinary(rootNode), Binary.updateBinaryInstallerPath);
            wz.writeByteToFile(Binary.getUpdateBinary(rootNode), Binary.updateBinaryPath);
            writeTempFiles();
            for (String file : Jar.getOtherFileList()) {
                wz.writeFileToZip(JarOperations.getInputStream(file), file);
            }
        } catch (NullPointerException npe) {
            System.out.println("Executing through Netbeans hence skipping Jar Operations");
        }

        wz.close();
        JOptionPane.showMessageDialog(null, "Zip Created Successfully..!!");
    }

    //this is required to fix status 7 error while installing Rom
    public static void writeTempFiles() throws IOException {
        for (String path : Project.getTempFilesList()) {
            wz.writeStringToZip("delete this file", path);
        }
    }
}
