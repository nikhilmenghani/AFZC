/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import static flashablezipcreator.AFZC.Protocols.show;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class ProjectMouseAdapter extends MouseAdapter {

    private void myPopupEvent(MouseEvent e) {
        JPopupMenu popup = null;
        javax.swing.JTree myTree = (javax.swing.JTree) e.getSource();

        TreePath[] paths = myTree.getSelectionPaths();
        if (paths != null) {
            popup = MyPopup.getPopup(paths, myTree);
            popup.show(myTree, e.getX(), e.getY());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            myPopupEvent(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            myPopupEvent(e);
        }
    }
}
