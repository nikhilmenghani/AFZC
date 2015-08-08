/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Nikhil
 */
public class NodeRenderer extends DefaultTreeCellRenderer {

    public ImageIcon iconProject = new ImageIcon(getClass().getResource("../res/Documents-icon.png"));
    public ImageIcon iconGroup = new ImageIcon(getClass().getResource("../res/Documents-icon.png"));
    public ImageIcon iconSubGroup = new ImageIcon(getClass().getResource("../res/Documents-icon.png"));
    public ImageIcon iconFile = new ImageIcon(getClass().getResource("../res/Actions-document-edit-icon.png"));

    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus){
        super.getTreeCellRendererComponent(tree, value, sel, leaf, leaf, row, leaf);
        
        ProjectItemNode node = (ProjectItemNode) value;
        
        switch(node.getType()){
            case ProjectItemNode.NODE_PROJECT:
                setIcon(iconProject);
                break;
            case ProjectItemNode.NODE_GROUP:
                setIcon(iconGroup);
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                setIcon(iconSubGroup);
                break;
            case ProjectItemNode.NODE_FILE:
                setIcon(iconFile);
                break;
        }
        return this;
    }
}
