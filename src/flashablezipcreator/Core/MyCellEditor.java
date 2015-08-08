/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Nikhil
 */
public class MyCellEditor extends DefaultCellEditor {
    public MyCellEditor(){
        super(new JTextField());
    }
    public MyCellEditor(JTree tree, JTextField field) {
      super(field);
    }
    public boolean isCellEditable(EventObject e) {
      if(e instanceof MouseEvent) {
     MouseEvent me = (MouseEvent)e;
     if(me.getClickCount() == 3) {
       JTree tree = (JTree)me.getSource();
       TreePath path = tree.getPathForLocation(me.getX(), me.getY());
       if(path.getPathCount() == 1) {
         //System.out.println("Root");
         return false; // root node
       }
       DefaultMutableTreeNode node =
           (DefaultMutableTreeNode) path.getLastPathComponent();
       Object hitObject=path.getLastPathComponent();
       if(hitObject instanceof TreeNode) {
         TreeNode t = (TreeNode)hitObject;
         boolean b = t.isLeaf();
         if (b) {
           //System.out.println("Leaf");
           return (((TreeNode)hitObject).isLeaf());
         } //else System.out.println("Not Leaf");
       }
     }
      }
//      System.out.println("Exit");
      return false;
    }
  }
