/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import static flashablezipcreator.AFZC.Protocols.show;
import flashablezipcreator.Operations.MyFileFilter;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class MyPopup {

    static JPopupMenu popup;
    //static TreeOperations to = new TreeOperations(ProjectTreeBuilder.rootNode);

    public static JPopupMenu getPopup(TreePath[] paths, JTree myTree, DefaultTreeModel model) {
        popup = new JPopupMenu();
        ArrayList<ProjectItemNode> nodeList = new ArrayList<>();
        Set<Integer> typeList = new HashSet<>();
        for (TreePath path : paths) {
            nodeList.add((ProjectItemNode) path.getLastPathComponent());
            typeList.add(((ProjectItemNode) path.getLastPathComponent()).type);
        }
        if (typeList.size() > 1) {
            show("You cannot select nodes of different types.");
        } else {
            switch (nodeList.get(0).type) {
                case ProjectItemNode.NODE_ROOT:
                    popup = getRootMenu();
                    break;
                case ProjectItemNode.NODE_PROJECT:
                    popup = getProjectMenu(nodeList, model);
                    break;
                case ProjectItemNode.NODE_GROUP:
                    popup = getGroupMenu(nodeList, myTree, model);
                    break;
                case ProjectItemNode.NODE_SUBGROUP:
                    popup = getSubGroupMenu(nodeList, model);
                    break;
                case ProjectItemNode.NODE_FILE:
                    popup = getFileMenu(nodeList, model);
                    break;
            }
        }
        return popup;
    }

    public static JPopupMenu getRootMenu() {
        JMenuItem mitemAddProject = new JMenuItem("Add Project");
        mitemAddProject.addActionListener((ActionEvent ae) -> {
            show("Add Project");
        });
        JMenuItem mitemAddThemes = new JMenuItem("Add Themes");
        mitemAddThemes.addActionListener((ActionEvent ae) -> {
            show("Add Themes");
        });
        popup = new JPopupMenu();
        popup.add(mitemAddProject);
        popup.add(mitemAddThemes);
        return popup;
    }

    public static JPopupMenu getProjectMenu(ArrayList<ProjectItemNode> nodeList, DefaultTreeModel model) {
        JMenuItem mitemAddGroup = new JMenuItem("Add Group");
        mitemAddGroup.addActionListener((ActionEvent ae) -> {
            //show("Group will be added to " + node.title);
        });
        JMenuItem mitemDeleteProject = new JMenuItem("Delete Projects/Themes");
        mitemDeleteProject.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList, model);
        });
        JMenu myPopup = new JMenu("Add Group");
        //myPopup.add(mitemDeleteProject);
        popup = new JPopupMenu();
        
        if (nodeList.size() == 1) {
            myPopup.add(mitemAddGroup);
            popup.add(myPopup);
            //popup.add(myPopup);
            
        }
        popup.add(mitemDeleteProject);
        return popup;
    }

    public static JPopupMenu getGroupMenu(ArrayList<ProjectItemNode> nodeList, JTree myTree, DefaultTreeModel model) {
        JMenuItem mitemAddSubGroup = new JMenuItem("Add SubGroup");
        mitemAddSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    //show("SubGroup will be added to " + node.title);
                }
        );
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    addNode(nodeList, model);
                }
        );
        JMenuItem mitemDeleteGroup = new JMenuItem("Delete Group(s)");
        mitemDeleteGroup.addActionListener(
                (ActionEvent ae) -> {
                    deleteNode(nodeList, model);
                }
        );
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            popup.add(mitemAddSubGroup);
            popup.add(mitemAddFile);
        }
        popup.add(mitemDeleteGroup);
        return popup;
    }

    public static JPopupMenu getSubGroupMenu(ArrayList<ProjectItemNode> nodeList, DefaultTreeModel model) {
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    addNode(nodeList, model);
                }
        );
        JMenuItem mitemDeleteSubGroup = new JMenuItem("Delete SubGroup(s)");
        mitemDeleteSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    deleteNode(nodeList, model);
                }
        );
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            popup.add(mitemAddFile);
        }
        popup.add(mitemDeleteSubGroup);
        return popup;
    }

    public static JPopupMenu getFileMenu(ArrayList<ProjectItemNode> nodeList, DefaultTreeModel model) {
        JMenuItem mitemDeleteFile = new JMenuItem("Delete File(s)");
        mitemDeleteFile.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList, model);
        });
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            //all other popups will be added here.
        }
        popup.add(mitemDeleteFile);
        return popup;
    }

    public static void addNode(ArrayList<ProjectItemNode> nodeList, DefaultTreeModel model) {
        switch (nodeList.get(0).type) {
            case ProjectNode.NODE_GROUP:
                for (File tempFile : MyFileFilter.getSelectedFiles(((GroupNode) nodeList.get(0)).extension)) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (GroupNode) nodeList.get(0));
                    //fnode.fileSourcePath = tempFile.getAbsolutePath();
                    ((GroupNode) nodeList.get(0)).addChild(fnode, model);
                }
                break;
            case ProjectNode.NODE_SUBGROUP:
                for (File tempFile : MyFileFilter.getSelectedFiles(((SubGroupNode) nodeList.get(0)).extension)) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (SubGroupNode) nodeList.get(0));
                    //fnode.fileSourcePath = tempFile.getAbsolutePath();
                    ((SubGroupNode) nodeList.get(0)).addChild(fnode, model);
                }
                break;
        }

    }

    public static void deleteNode(ArrayList<ProjectItemNode> nodeList, DefaultTreeModel model) {
        for (ProjectItemNode node : nodeList) {
            node.removeMe(model);
        }
    }
}
