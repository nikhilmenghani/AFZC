/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import static flashablezipcreator.MyTree.model;
import static flashablezipcreator.MyTree.tree;
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
        rootNode = new ProjectItemNode("AFZC Projects", ProjectItemNode.NODE_ROOT);
        tree = new javax.swing.JTree(rootNode);
        tree.setCellRenderer(new NodeRenderer());
        tree.setDragEnabled(true);
        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);
        tree.setTransferHandler(new MyTransferHandler(rootNode, (DefaultTreeModel) ProjectTreeBuilder.tree.getModel()));
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
