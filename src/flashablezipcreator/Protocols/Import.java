/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.DeleteNode;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.DiskOperations.ReadZip;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.progressBarFlag;
import static flashablezipcreator.UserInterface.MyTree.progressBarImportExport;
import flashablezipcreator.Operations.TreeOperations;
import static flashablezipcreator.UserInterface.MyTree.circularProgressBar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class Import implements Runnable {

    static ReadZip rz;
    static TreeOperations to;
    static String exisingUpdaterScript = "";
    static String fileName = "";
    static int zipType;
    public static int progressValue = 0;
    public static int fileIndex = 0;
    String path;
    ProjectItemNode rootNode;
    DefaultTreeModel model;
    public JDialog dialog;

    public Import(String path) throws IOException, ParserConfigurationException, TransformerException, SAXException, InterruptedException {
        this.rootNode = MyTree.rootNode;
        this.path = path;
        this.model = MyTree.model;
    }

    public static void fromTheZip(String path) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        try {
            Logs.write("Trying to import from path: " + path);
            progressValue = 0;
            boolean containsDataXml = false;
            Xml.initialize();
            rz = new ReadZip(path);
            if (rz.zf != null) {
                to = new TreeOperations();
                int maxSize = rz.filesCount;
                fileIndex = 0;
                Identify.fileName = (new File(path)).getName().replaceFirst("[.][^.]+$", "");
                Logs.write("Reading Zip...");
                for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
                    ZipEntry ze = e.nextElement();
                    String name = ze.getName();
                    int modType = Mod.getModType(name);
                    Logs.write("Reading: " + name);
                    progressValue = (fileIndex * 100) / maxSize;
                    progressBarImportExport.setValue(progressValue);
                    circularProgressBar.updateProgress(progressValue);
                    setProgressBar("Importing", (new File(name)).getName() + "");
                    if (name.endsWith("/") || Project.getTempFilesList().contains(name)
                            || name.startsWith("META-INF")
                            || name.contains("Extract_")) {
                        Logs.write("Skipping " + name);
                        continue;
                    }
                    InputStream in = rz.zf.getInputStream(ze);
                    if (name.equals(Xml.data_path)) {
                        Xml.fileData = rz.getStringFromFile(in);
                        containsDataXml = true;
                        Logs.write("Skipping " + name);
                        continue;
                    }
                    String filePath = name;
                    String projectName = Identify.getProjectName(name);
                    int projectType = Identify.getProjectType(filePath);
                    if (name.endsWith("DeleteFilesPath")) {
                        importDeleteNodes(filePath, projectName, projectType, modType, in);
                        continue;
                    }
                    switch (projectType) {
                        case Types.PROJECT_AROMA:
                            importAromaZip(filePath, projectName, in);
                            break;
                        case Types.PROJECT_GAPPS:
                            importGappsZip(filePath, projectName, in);
                            break;
                        case Types.PROJECT_MOD:
                            importModZip(filePath, projectName, in, modType);
                            break;
                        case Types.PROJECT_CUSTOM:
                            importCustomZip(filePath, projectName, in);
                            break;
                    }
                }

                //adding nodes to tree should be done here.
                Xml.terminate();

                if (containsDataXml) {
                    setProgressBar("", "Setting file details..");
                    try {
                        Logs.write("Parsing file_data.xml");
                        Xml.parseXml(0); //this is to set additional details like description to nodes
                        Logs.write("Xml Parsing Successful");
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(null, "Something Went Wrong!\nShare logs with developer!\n" + Logs.getExceptionTrace(e));
                        Logs.write(Logs.getExceptionTrace(e));
                    }
                    setProgressBar("", "Setting file details done.");
                }
                progressBarImportExport.setString("Successfully Imported");
                MyTree.txtProgressTitle.setText("");
                MyTree.txtProgressContent.setText("Successfully Imported");
                circularProgressBar.updateProgress(100);
                progressBarImportExport.setValue(100);
                Logs.write("File Imported Successfully");
                JOptionPane.showMessageDialog(null, "Successfully Imported");
                MyTree.txtProgressContent.setText("");
                circularProgressBar.updateProgress(0);
                progressBarImportExport.setString("0%");
                progressBarImportExport.setValue(0);
                progressBarFlag = 0;
            } else {
                JOptionPane.showMessageDialog(null, "Zip file seems to be corrupted!");
                MyTree.setCardLayout(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Went Wrong!\nShare logs with developer!\n" + Logs.getExceptionTrace(e));
            Logs.write(Logs.getExceptionTrace(e));
            MyTree.setCardLayout(1);
        }
    }

    public static void importModZip(String filePath, String projectName, InputStream in, int modType) throws IOException {
        String fName = (new File(filePath)).getName();
        switch (modType) {
            case Mod.MOD_LESS:
                String groupName = Identify.getGroupName(filePath);
                int groupType;// = Identify.getGroupType(filePath);
                groupType = Types.GROUP_MOD;
                String originalGroupType = "";
                ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_AROMA);
                String subGroupName = Identify.getSubGroupName(groupName, filePath);
                int subGroupType = groupType; //Groups that have subGroups have same type.
                TreeOperations to = new TreeOperations();
                FileNode file = to.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_MOD, Mod.MOD_LESS);
                file.prop.fileSourcePath = file.prop.path;
                rz.writeFileFromZip(in, file.prop.fileSourcePath);
                Logs.write("Written File: " + fName);
                break;
        }
    }

    public static void importCustomZip(String filePath, String projectName, InputStream in) throws IOException {
        String groupName = Identify.getGroupName(filePath);
        int groupType = Identify.getGroupType(filePath);
        String originalGroupType = "";
        if (groupType == Types.GROUP_CUSTOM) {
            try {
                originalGroupType = Identify.getOriginalGroupType(filePath);
            } catch (Exception e) {
                originalGroupType = "";
            }
        }
        ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_CUSTOM);
        String subGroupName = Identify.getSubGroupName(groupName, filePath);
        int subGroupType = groupType; //Groups that have subGroups have same type.
        String fName = (new File(filePath)).getName();
        FileNode file = to.addFileToTree(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_CUSTOM, Mod.MOD_LESS);
        file.prop.fileSourcePath = file.prop.path;
        rz.writeFileFromZip(in, file.prop.fileSourcePath);
        Logs.write("Written File: " + fName);
    }

    public static void importAromaZip(String filePath, String projectName, InputStream in) throws IOException {
        String groupName = Identify.getGroupName(filePath);
        int groupType = Identify.getGroupType(filePath);
        String originalGroupType = "";
        if (groupType == Types.GROUP_CUSTOM) {
            try {
                originalGroupType = Identify.getOriginalGroupType(filePath);
            } catch (Exception e) {
                originalGroupType = "";
            }
        }
        ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_AROMA);
        String subGroupName = Identify.getSubGroupName(groupName, filePath);
        int subGroupType = groupType; //Groups that have subGroups have same type.
        String fName = (new File(filePath)).getName();
        TreeOperations to = new TreeOperations();
        FileNode file = to.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_AROMA, Mod.MOD_LESS);
        file.prop.fileSourcePath = file.prop.path;
        rz.writeFileFromZip(in, file.prop.fileSourcePath);
        Logs.write("Written File: " + fName);
    }

    public static void importGappsZip(String filePath, String projectName, InputStream in) throws IOException {
        String groupName = Identify.getGroupName(filePath);
        int groupType = Identify.getGroupType(filePath);
        String originalGroupType = "";
        if (groupType == Types.GROUP_CUSTOM) {
            try {
                originalGroupType = Identify.getOriginalGroupType(filePath);
            } catch (Exception e) {
                originalGroupType = "";
            }
        }
        ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_GAPPS);
        String subGroupName = Identify.getSubGroupName(groupName, filePath);
        int subGroupType = groupType; //Groups that have subGroups have same type.
        String fName = (new File(filePath)).getName();
        TreeOperations to = new TreeOperations();
        FileNode file = to.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_GAPPS, Types.GAPPS_BASIC);
        file.prop.fileSourcePath = file.prop.path;
        rz.writeFileFromZip(in, file.prop.fileSourcePath);
        Logs.write("Written File: " + fName);
    }

    public static void importDeleteNodes(String filePath, String projectName, int projectType, int modType, InputStream in) throws IOException {
        String groupName = Identify.getGroupName(filePath);
        int groupType = Identify.getGroupType(filePath);
        NodeProperties np = new NodeProperties(projectName, projectType, modType, MyTree.rootNode);
        ProjectNode pNode = (ProjectNode) MyTree.rootNode.addChild(new ProjectNode(np), false);
        np = new NodeProperties(groupName, groupType, pNode);
        GroupNode gNode = (GroupNode) pNode.addChild(new GroupNode(np), false);
        for (String path : rz.getStringFromFile(in).split("\n")) {
            gNode.addChild(new DeleteNode(path, gNode), true);
        }
    }

    public static void setProgressBar(String title, String value) {
        String str = value;
        if (value.length() > 60) {
            str = str.substring(0, value.length() / 3) + "..." + str.substring(value.length() - 10, value.length());
        } else if (value.length() > 40) {
            str = str.substring(0, value.length() / 2) + "..." + str.substring(value.length() - 10, value.length());
        }
        MyTree.txtProgressTitle.setText(title);
        MyTree.txtProgressContent.setText(str);
        circularProgressBar.updateProgress(value);
        circularProgressBar.updateProgress(progressValue);
        switch (MyTree.progressBarFlag) {
            case 0:
                progressBarImportExport.setString(progressValue + "%");
                break;
            case 1:
                progressBarImportExport.setString(value);
                break;
            case 2:
                progressBarImportExport.setString(" ");
                break;
        }
        fileIndex++;
    }

    @Override
    public void run() {
        try {
            MyTree.setCardLayout(2);
            fromTheZip(path);
            MyTree.setCardLayout(1);
        } catch (Exception e) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, e);
            Logs.write(Logs.getExceptionTrace(e));
        }
    }
}
