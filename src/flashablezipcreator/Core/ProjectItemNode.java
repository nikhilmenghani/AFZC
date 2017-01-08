/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import static flashablezipcreator.AFZC.Protocols.p;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import static flashablezipcreator.AFZC.Protocols.show;
import flashablezipcreator.MyTree;
import java.io.File;

/**
 *
 * @author Nikhil
 */
public class ProjectItemNode extends DefaultMutableTreeNode implements TreeNode {

    public String title;
    public String path = "AFZC Projects";
    public String zipPath = "customize/aroma";
    public String extractZipPath = "";
    public String location = "";

    public int type; //helpful in setting appropriate icon for the node

    //Using vector is better than ArrayList here
    public Vector<ProjectItemNode> children = new Vector<ProjectItemNode>();
    public ProjectItemNode parent;
    public DefaultTreeModel model;

    //Constants for types of node
    public static final int NODE_ROOT = 0;
    public static final int NODE_PROJECT = 1;
    public static final int NODE_GROUP = 2;
    public static final int NODE_SUBGROUP = 3;
    public static final int NODE_FOLDER = 4;
    public static final int NODE_FILE = 5;

    //not required yet
    public ProjectItemNode(String title) {
        super(title);
        this.title = title;
    }

    //required in root node
    public ProjectItemNode(String title, int type) {
        super(title);
        this.title = title;
        this.type = type;
    }

    public ProjectItemNode(String title, int type, ProjectItemNode parent) {
        super(title);
        this.title = title;
        this.type = type;
        setParent(parent);
    }

//    public void addChild(ProjectItemNode child) {
//        children.add(child);
//    }
    public ProjectItemNode addChild(ProjectItemNode child, boolean overwrite) {
        boolean childExists = false;
        for (ProjectItemNode childNode : children) {
            if (childNode.title.equals(child.title)) {
                childExists = true;
                child = childNode;
            }
        }
        if (overwrite) {
            if (childExists) {
                children.remove(child);
            }
            children.add(child);
        } else if (!childExists) {
            children.add(child);
        }

        MyTree.model.reload(this);
        return child;
    }

//    public void removeChild(ProjectItemNode child) {
//        children.remove(child);
//    }
    public void removeChild(ProjectItemNode child) {
        children.remove(child);
        MyTree.model.reload(parent);
    }

    //should be used this when we want to remove by child object
    public void removeMe() {
        parent.children.remove(this);
        MyTree.model.reload(parent);
    }

    public void setParent(ProjectItemNode parent) {
        this.parent = parent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void updateChildrenPath() {
        for (ProjectItemNode node : children) {
            node.path = this.path + File.separator + node.title;
            node.updateChildrenPath();
        }
    }

    /**
     *
     * @return
     */
    //following method cannot override DefaultMutableTreeNode class' method
//    public String getPath(){
//        return path;
//    }
    @Override
    public ProjectItemNode getChildAt(int i) {
        return children.elementAt(i);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public ProjectItemNode getParent() {
        return this.parent;
    }

    @Override
    public int getIndex(TreeNode tn) {
        return children.indexOf(tn);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return (children.isEmpty());
    }

    @Override
    public Enumeration children() {
        return children.elements();
    }

    @Override
    public String toString() {
        return title;
    }

    public int getType() {
        return type;
    }
}
