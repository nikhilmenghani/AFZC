/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;
import java.awt.Font;
import java.io.IOException;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class ProjectTreeBuilder {

    public static javax.swing.JTree tree;
    public static javax.swing.JScrollPane jScrollPane;
    public static ProjectItemNode rootNode;

    public static javax.swing.JTree buildTree() throws IOException {
        rootNode = new ProjectItemNode("AFZC Projects", Types.NODE_ROOT);
        rootNode.prop.path = "AFZC Projects";
        rootNode.prop.zipPath = "customize";
        tree = new javax.swing.JTree(rootNode);
        tree.setCellRenderer(new NodeRenderer());
        tree.setDragEnabled(true);
        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);
        tree.setSelectionRow(0);
        tree.setShowsRootHandles(true);
        tree.setTransferHandler(new MyTransferHandler(rootNode, (DefaultTreeModel) ProjectTreeBuilder.tree.getModel()));
        Font currentFont = tree.getFont();
        Font bigFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 1);
        tree.setFont(bigFont);
        tree.setRowHeight(21);
        tree.setVisibleRowCount(18);
        //mouse click configurations
        ProjectMouseAdapter ma = new ProjectMouseAdapter();
        tree.addMouseListener(ma);
        return tree;
    }

    public static DefaultTreeModel buildModel() {
        DefaultTreeModel model = (DefaultTreeModel) ProjectTreeBuilder.tree.getModel();
        model.addTreeModelListener(new MyTreeModelListener());
        return model;
    }

    public static javax.swing.JScrollPane buildScrollPane() {
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView(ProjectTreeBuilder.tree);
        return jScrollPane;
    }

    public static ProjectItemNode createNode(String title, int type) {
        return new ProjectItemNode(title, type);
    }
}
