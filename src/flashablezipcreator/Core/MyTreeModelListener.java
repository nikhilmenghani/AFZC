/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import static flashablezipcreator.AFZC.Protocols.show;
import flashablezipcreator.Operations.TreeOperations;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Nikhil
 */
public class MyTreeModelListener implements TreeModelListener {

    TreeOperations to = new TreeOperations(ProjectTreeBuilder.rootNode);
    public void treeNodesChanged(TreeModelEvent e) {
        ProjectItemNode node;
        node = (ProjectItemNode) (e.getTreePath().getLastPathComponent());
        
        /*
         * If the event lists children, then the changed
         * node is the child of the node we have already
         * gotten.  Otherwise, the changed node and the
         * specified node are the same.
         */
        try {
            int index = e.getChildIndices()[0];
            node = (ProjectItemNode) (node.getChildAt(index));
        } catch (NullPointerException exc) {
        }

        System.out.println("The user has finished editing the node.");
        System.out.println("New value: " + node.getUserObject());
        
        try {
            to.renameNode(node, node.getUserObject().toString());
            
            //node.title = node.getUserObject().toString();
        } catch (IOException ex) {
            Logger.getLogger(MyTreeModelListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void treeNodesInserted(TreeModelEvent e) {
    }

    public void treeNodesRemoved(TreeModelEvent e) {
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }
}
