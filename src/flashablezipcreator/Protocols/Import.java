/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.DiskOperations.ReadZip;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.progressBarFlag;
import static flashablezipcreator.UserInterface.MyTree.progressBarImportExport;
import flashablezipcreator.Operations.TreeOperations;
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
        Logs.write("Trying to import from path: " + path);
        progressValue = 0;
        boolean containsDataXml = false;
        Xml.initialize();
        rz = new ReadZip(path);
        to = new TreeOperations();
        int maxSize = rz.filesCount;
        fileIndex = 0;
        Identify.fileName = (new File(path)).getName().replaceFirst("[.][^.]+$", "");
        int modType = Mod.getModType(rz);
        Logs.write("Reading Zip...");
        try {
            for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
                ZipEntry ze = e.nextElement();
                String name = ze.getName();
                Logs.write("Reading: " + name);
                progressValue = (fileIndex * 100) / maxSize;
                progressBarImportExport.setValue(progressValue);
                setProgressBar("Importing " + (new File(name)).getName() + "");
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
                switch (projectType) {
                    case Types.PROJECT_AROMA:
                        importAromaZip(filePath, projectName, in);
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
                setProgressBar("Setting file details..");
                Logs.write("Parsing file_data.xml");
                Xml.parseXml(0); //this is to set additional details like description to nodes
                Logs.write("Xml Parsing Successful");
                setProgressBar("Setting file details done.");

            }
            progressBarImportExport.setString("Successfully Imported");
            progressBarImportExport.setValue(100);
            Logs.write("File Imported Successfully");
            JOptionPane.showMessageDialog(null, "Successfully Imported");
            progressBarImportExport.setString("0%");
            progressBarImportExport.setValue(0);
            progressBarFlag = 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Went Wrong!\nShare logs with developer!\n" + Logs.getExceptionTrace(e));
            Logs.write(Logs.getExceptionTrace(e));
            MyTree.setCardLayout(1);
        }
    }

    public static void importModZip(String filePath, String projectName, InputStream in, int modType) throws IOException {
        String fName = (new File(filePath)).getName();
        switch (modType) {
            case Mod.TITANIUM_BACKUP:
                String groupName = "Titanium Backup Apps";
                int groupType = Types.GROUP_DATA_APP;
                String folderName = fName.contains("-") ? fName.substring(0, fName.indexOf("-")) : fName.replaceFirst("[.][^.]+$", "");
                ArrayList<String> folderList = new ArrayList<>();
                if (!filePath.contains(Identify.folderSeparator)) {
                    folderList.add(folderName);
                } else {
                    folderList = Identify.getFolderNames(filePath, Types.PROJECT_MOD);
                }
                fName = folderName + ".apk";
                FileNode file = to.addFileToTree(fName, "", -1, groupName, groupType, "", folderList, projectName, Types.PROJECT_MOD, Mod.TITANIUM_BACKUP);
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
        NodeProperties np = new NodeProperties();
        FileNode file = np.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_AROMA, Mod.MOD_LESS);
        file.prop.fileSourcePath = file.prop.path;
        rz.writeFileFromZip(in, file.prop.fileSourcePath);
        Logs.write("Written File: " + fName);
    }

    public static void setProgressBar(String value) {
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
