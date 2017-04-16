/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Logs;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import flashablezipcreator.UserInterface.MyTree;
import java.io.File;
import java.util.Collections;

/**
 *
 * @author Nikhil
 */
public class ProjectItemNode extends DefaultMutableTreeNode implements TreeNode {

    public DefaultTreeModel model;
    public NodeProperties prop = new NodeProperties();

    //not required yet
    public ProjectItemNode(String title) {
        super(title);
        prop.title = title;
    }

    //required in root node
    public ProjectItemNode(String title, int type) {
        super(title);
        prop.title = title;
        prop.type = type;
    }

    public ProjectItemNode(String title, int type, ProjectItemNode parent) {
        super(title);
        prop.title = title;
        prop.type = type;
        prop.parent = parent;
    }

    public ProjectItemNode(NodeProperties properties) {
        this(properties.title, properties.type, properties.parent);
        prop = properties;
    }

    public ProjectItemNode addChild(ProjectItemNode child, boolean overwrite) {
        boolean childExists = false;
        Logs.write("checking if children exists");
        for (ProjectItemNode childNode : prop.children) {
            if (childNode.prop.title.equals(child.prop.title)) {
                childExists = true;
                child = childNode;
            }
        }
        Logs.write("over writing");
        if (overwrite) {
            if (childExists) {
                prop.children.remove(child);
            }
            prop.children.add(child);
        } else if (!childExists) {
            prop.children.add(child);
        }
        Logs.write("added children" + child.prop.title);
        MyTree.model.reload(this);
        return child;
    }

    public void removeChild(ProjectItemNode child) {
        prop.children.remove(child);
        MyTree.model.reload(prop.parent);
    }

    //should be used this when we want to remove by child object
    public void removeMe() {
        prop.parent.prop.children.remove(this);
        MyTree.model.reload(prop.parent);
    }

    private void setParent(ProjectItemNode parent) {
        prop.parent = parent;
    }

    public void setTitle(String title) {
        prop.title = title;
    }

    public String getTitle() {
        return prop.title;
    }

    public void updateChildrenPath() {
        for (ProjectItemNode node : prop.children) {
            node.prop.path = prop.path + File.separator + node.prop.title;
            node.updateChildrenPath();
        }
    }

    public boolean contains(String title) {
        for (ProjectItemNode node : prop.children) {
            if(node.prop.title.equals(title))
                return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public ProjectItemNode getChildAt(int i) {
        return prop.children.get(i);
    }

    @Override
    public int getChildCount() {
        return prop.children.size();
    }

    @Override
    public ProjectItemNode getParent() {
        return prop.parent;
    }

    @Override
    public int getIndex(TreeNode tn) {
        return prop.children.indexOf(tn);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return (prop.children.isEmpty());
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(prop.children);
    }

    @Override
    public String toString() {
        return prop.title;
    }

    public int getType() {
        return prop.type;
    }
}
