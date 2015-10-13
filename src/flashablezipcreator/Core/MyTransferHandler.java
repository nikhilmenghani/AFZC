/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.MyTree;
import static flashablezipcreator.MyTree.panelLower;
import flashablezipcreator.Operations.MyFileFilter;
import flashablezipcreator.Protocols.Import;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
        if (!canImport(support)) {
            return false;
        }

        Transferable t = support.getTransferable();
        try {
            List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            if (data.size() != 1) {
                JOptionPane.showMessageDialog(null, "Cannot Import More than one Zip Files at a time.");
//                Iterator i = data.iterator();
//                while (i.hasNext()) {
//                    File f = (File) i.next();
//                }
            } else {
                File f = (File) data.get(0);
                if (f.getAbsoluteFile().toString().endsWith(".zip")) {
                    try {
                        Thread importZip = new Thread(new Import(f.getAbsolutePath()), "ImportZip");
                        importZip.start();
                    } catch (NullPointerException npe) {
                    }
                }
            }
            //model.reload();
            return true;
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
        return false;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        System.out.println("Hello");

        if (!(comp instanceof JTree)) {
            return false;
        }
        if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        JTree tree = (JTree) comp;
//        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        try {
            List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            Iterator i = data.iterator();
            while (i.hasNext()) {
                File f = (File) i.next();
                rootNode.add(new DefaultMutableTreeNode(f.getName()));
            }
            model.reload();
            return true;
        } catch (Exception ioe) {
            System.out.println(ioe);
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
}
