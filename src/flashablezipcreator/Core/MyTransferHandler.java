/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Import;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preference;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class MyTransferHandler extends TransferHandler {

    public ProjectItemNode rootNode = null;
    DefaultTreeModel model = null;

    public MyTransferHandler(ProjectItemNode rootNode, DefaultTreeModel model) {
        this.rootNode = rootNode;
        this.model = model;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferSupport support) {
        try {
            if (!canImport(support)) {
                return false;
            }
            Logs.write("File Importing via Drag and Drop..");
            GroupNode groupNode = null;
            SubGroupNode subGroupNode = null;
            FolderNode folderNode = null;
            FileNode fileNode = null;

            JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
            TreePath path = dropLocation.getPath();
            Transferable t = support.getTransferable();
            List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            ProjectItemNode parentNode = null;
            try {
                parentNode = (ProjectItemNode) path.getLastPathComponent();
            } catch (Exception e) {
                System.out.println("Trying to import zip..");
            }
            if (data.size() == 1) {
                File f = (File) data.get(0);
                if (f.getAbsoluteFile().toString().endsWith(".zip") && parentNode == null) {
                    Logs.write("Importing zip: " + f.getAbsolutePath());
                    Thread importZip = new Thread(new Import(f.getAbsolutePath()), "ImportZip");
                    importZip.start();
                    return true;
                }
            }

            if (parentNode != null) {
                switch (parentNode.prop.type) {
                    case Types.NODE_ROOT:
                        JOptionPane.showMessageDialog(null, "You cannot drop things on Project(s)!");
                        break;
                    case Types.NODE_GROUP:
                        groupNode = (GroupNode) parentNode;
                        Logs.write("Dropping on Group: " + groupNode.prop.title);
                        addDataToNode(groupNode, data);
                        break;
                    case Types.NODE_SUBGROUP:
                        subGroupNode = (SubGroupNode) parentNode;
                        Logs.write("Dropping on SubGroup: " + subGroupNode.prop.title);
                        addDataToNode(subGroupNode, data);
                        break;
                    case Types.NODE_FOLDER:
                        folderNode = (FolderNode) parentNode;
                        Logs.write("Dropping on Folder: " + folderNode.prop.title);
                        addDataToNode(folderNode, data);
                        break;
                    case Types.NODE_FILE:
                        JOptionPane.showMessageDialog(null, "You cannot drop things on File(s)!");
                        break;
                }
                return true;
            }
            JOptionPane.showMessageDialog(null, "Cannot import this file(s)!");
            return false;
        } catch (UnsupportedFlavorException | IOException | ParserConfigurationException | TransformerException | SAXException | InterruptedException ex) {
            Logger.getLogger(MyTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        if (comp instanceof JTree) {
            for (int i = 0; i < transferFlavors.length; i++) {
                if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void addDataToNode(GroupNode groupNode, List data) throws UnsupportedFlavorException, IOException {
        Iterator i = data.iterator();
        while (i.hasNext()) {
            File f = (File) i.next();
            switch (groupNode.prop.groupType) {
                case Types.GROUP_SYSTEM:
                case Types.GROUP_SYSTEM_APK:
                case Types.GROUP_SYSTEM_PRIV_APK:
                case Types.GROUP_DATA_APP:
                case Types.GROUP_SYSTEM_BIN:
                case Types.GROUP_SYSTEM_ETC:
                case Types.GROUP_SYSTEM_FRAMEWORK:
                    addFolderNode(groupNode, f);
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                    addFileNode(groupNode, f);
                    break;
                case Types.GROUP_SYSTEM_FONTS:
                case Types.GROUP_DATA_LOCAL:
                case Types.GROUP_SYSTEM_MEDIA:
                    addSubGroupNode(groupNode, f);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Cannot Add Files in this Group");
            }
        }
    }

    public void addDataToNode(SubGroupNode sgNode, List data) {
        Iterator i = data.iterator();
        while (i.hasNext()) {
            File f = (File) i.next();
            if (f.isFile() && acceptFile(f, sgNode.prop.extension)) {
                Logs.write("Adding File " + f + " to SubGroup: " + sgNode.prop.title);
                sgNode.addChild(new FileNode(f.getPath(), sgNode), true);
            } else {
                JOptionPane.showMessageDialog(null, f.getName() + " cannot be added to this sub group!");
            }
        }
    }

    public void addDataToNode(FolderNode folderNode, List data) throws UnsupportedFlavorException, IOException, ParserConfigurationException, TransformerException, SAXException, InterruptedException {
        Iterator i = data.iterator();
        while (i.hasNext()) {
            File f = (File) i.next();
            addFolderNode(folderNode, f);
        }
    }

    public void addFolderNode(FolderNode folderNode, File fPath) {
        if (fPath.isDirectory()) {
            FolderNode fNode = (FolderNode) folderNode.addChild(new FolderNode(fPath.getName(), folderNode), false);
            for (String fileName : fPath.list()) {
                String filePath = fPath + File.separator + fileName;
                File f = new File(filePath);
                if (f.isDirectory()) {
                    addFolderNode(fNode, f);
                } else if (f.isFile()) {
                    Logs.write("Adding File " + f + " to Folder: " + fNode.prop.title);
                    fNode.addChild(new FileNode(filePath, fNode), true);
                }
            }
        } else {
            Logs.write("Adding File " + fPath + " to Folder: " + folderNode.prop.title);
            folderNode.addChild(new FileNode(fPath.getPath(), folderNode), true);
        }
    }

    public void addFolderNode(GroupNode groupNode, File fPath) {
        if (fPath.isDirectory()) {
            FolderNode folderNode = (FolderNode) groupNode.addChild(new FolderNode(fPath.getName(), groupNode), false);
            for (String fileName : fPath.list()) {
                String filePath = fPath + File.separator + fileName;
                File f = new File(filePath);
                if (f.isDirectory()) {
                    addFolderNode(folderNode, f);
                } else if (f.isFile() && fileName.endsWith(groupNode.prop.extension)) {
                    Logs.write("Adding File " + f + " to Folder: " + folderNode.prop.title);
                    folderNode.addChild(new FileNode(filePath, folderNode), true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incompatible file found! Skipping it!");
                }
            }
        } else if (fPath.isFile()) {
            String folderName = fPath.getName().replaceFirst("[.][^.]+$", "");
            if (groupNode.prop.groupType == Types.GROUP_DATA_APP) {
                folderName = fPath.getName().replaceFirst("[.][^.]+$", "") + "-1";
            }
            FolderNode folderNode = new FolderNode(folderName, groupNode);
            Logs.write("Adding File " + fPath + " to Folder: " + folderNode.prop.title);
            folderNode.addChild(new FileNode(fPath.getPath(), folderNode), true);
            groupNode.addChild(folderNode, false);
        }
    }

    public void addSubGroupNode(GroupNode groupNode, File fPath) {
        if (fPath.isDirectory()) {
            SubGroupNode sgNode = (SubGroupNode) groupNode.addChild(new SubGroupNode(fPath.getName(), groupNode.prop.groupType, groupNode), false);
            for (String fileName : fPath.list()) {
                if (fileName.endsWith(groupNode.prop.extension)) {
                    Logs.write("Adding File " + fPath + " to Group: " + sgNode.prop.title);
                    sgNode.addChild(new FileNode(fPath + File.separator + fileName, sgNode), true);
                } else {
                    Logs.write("Incompatible file " + fPath + " found! Skipping it!");
                    JOptionPane.showMessageDialog(null, "Incompatible file found! Skipping it!");
                }
            }
            if (sgNode.prop.children.isEmpty()) {
                sgNode.removeMe();
            }
        } else if (fPath.isFile()) {
            JOptionPane.showMessageDialog(null, "Cannot add files to Group " + groupNode.prop.title + ", Add " + fPath.getName() + " to SubGroup");
        }
    }

    public void addFileNode(GroupNode groupNode, File fPath) {
        if (fPath.isFile() && acceptFile(fPath, groupNode.prop.extension)) {
            Logs.write("Adding File " + fPath + " to Group: " + groupNode.prop.title);
            groupNode.addChild(new FileNode(fPath.getPath(), groupNode), true);
        } else {
            Logs.write("Cannot add " + fPath.getName() + " to Group " + groupNode.prop.title);
            JOptionPane.showMessageDialog(null, "Cannot add " + fPath.getName() + " to Group.");
        }
    }

    public boolean acceptFile(File file, String extn) {
        String extension = getExtension(file);
        if (extension.equals(extn)) {
            return true;
        } else if (extn.equals("audio") && (extension.equals("aac")
                || extension.equals("mp3")
                || extension.equals("m4a")
                || extension.equals("ogg")
                || extension.equals("wav"))) {
            return true;
        }
        return false;
    }

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
