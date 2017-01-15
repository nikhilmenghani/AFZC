/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.MyTree;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class TreeOperations {

    Write w = new Write();
    ArrayList<ProjectItemNode> list = new ArrayList<>();
    ArrayList<ProjectItemNode> projectList = new ArrayList<>();
    ArrayList<ProjectItemNode> groupList = new ArrayList<>();
    ArrayList<ProjectItemNode> subGroupList = new ArrayList<>();
    ArrayList<ProjectItemNode> fileList = new ArrayList<>();
    ArrayList<String> repeatList = new ArrayList<>();//for files that have same names and different location in zip.

    ProjectItemNode rootNode;
    DefaultTreeModel model;

    public TreeOperations() {
        this.rootNode = MyTree.rootNode;
        this.model = MyTree.model;
    }

    public void renameNode(ProjectItemNode node, String newName) throws IOException {
        String oldName = "";
        switch (node.type) {
            case ProjectItemNode.NODE_ROOT:
                break;
            case ProjectItemNode.NODE_PROJECT:
                ((ProjectNode) node).renameMe(newName);
                break;
            case ProjectItemNode.NODE_GROUP:
                ((GroupNode) node).renameMe(newName);
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                ((SubGroupNode) node).renameMe(newName);
                break;
            case ProjectItemNode.NODE_FOLDER:
                ((FolderNode) node).renameMe(newName);
                break;
            case ProjectItemNode.NODE_FILE:
                oldName = ((FileNode) node).fileSourcePath;
                ((FileNode) node).renameMe(newName);
                w.rename(oldName, newName); //check if this is required
                break;
        }
    }

    public void removeNode(ProjectItemNode node) {
        node.removeMe();
    }

    public FileNode addFileToTree(String fileName, String subGroupName, int subGroupType, String groupName, int groupType, ArrayList<String> folders, String projectName, int projectType) {
        ProjectNode pNode = (ProjectNode) rootNode.addChild(new ProjectNode(projectName, projectType, rootNode), false);
        
        GroupNode gNode = (GroupNode) pNode.addChild(new GroupNode(groupName, groupType, pNode), false);
        SubGroupNode sgNode = null;
        if (!subGroupName.equals("")) {
            sgNode = (SubGroupNode) gNode.addChild(new SubGroupNode(subGroupName, subGroupType, gNode), false);
        }
        FolderNode folNode = null;
        if (folders.size() > 0) {
            int count = 1;
            for (String folder : folders) {
                FolderNode fNode = null;
                if (count++ == 1) {
                    if (sgNode != null) {
                        fNode = (FolderNode) sgNode.addChild(new FolderNode(folder, sgNode), false);
                    } else {
                        fNode = (FolderNode) gNode.addChild(new FolderNode(folder, gNode), false);
                    }
                } else if (folNode != null) {
                    fNode = (FolderNode) folNode.addChild(new FolderNode(folder, folNode), false);
                } else {
                    JOptionPane.showMessageDialog(null, "Something went wrong!");
                }
                folNode = fNode;
            }
        }
        FileNode fileNode = null;
        if (folNode == null) {
            if (sgNode != null) {
                fileNode = (FileNode) sgNode.addChild(new FileNode(fileName, sgNode), true);
            } else {
                fileNode = (FileNode) (FileNode) gNode.addChild(new FileNode(fileName, gNode), true);
            }
        } else {
            fileNode = (FileNode) (FileNode) folNode.addChild(new FileNode(fileName, folNode), true);
        }
        return fileNode;
    }

    public ArrayList<ProjectItemNode> parseNode(ProjectItemNode node, int type) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (((ProjectItemNode) node.getChildAt(i)).type == type) {
                switch (((ProjectItemNode) node.getChildAt(i)).type) {
                    case ProjectItemNode.NODE_PROJECT:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case ProjectItemNode.NODE_GROUP:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case ProjectItemNode.NODE_FOLDER:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case ProjectItemNode.NODE_SUBGROUP:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case ProjectItemNode.NODE_FILE:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                }
            }
            parseNode((ProjectItemNode) node.getChildAt(i), type);
        }
        return list;
    }

    public ArrayList<ProjectItemNode> getNodeList(int nodeType) {
        list = new ArrayList<>();
        return parseNode(this.rootNode, nodeType);
    }

    public ArrayList<ProjectItemNode> getProjectsSorted(ProjectItemNode rootNode) {
        ArrayList<ProjectItemNode> projects = new ArrayList<>();
        ArrayList<ProjectItemNode> projectAroma = new ArrayList<>();
        ArrayList<ProjectItemNode> projectThemes = new ArrayList<>();

        for (ProjectItemNode project : getNodeList(ProjectItemNode.NODE_PROJECT)) {
            switch (((ProjectNode) project).projectType) {
                case ProjectNode.PROJECT_AROMA:
                    projectAroma.add(project);
                    break;
                case ProjectNode.PROJECT_THEMES:
                    projectThemes.add(project);
                    break;
            }
        }
        projectAroma.stream().forEach((node) -> {
            projects.add(node);
        });
        projectThemes.stream().forEach((node) -> {
            projects.add(node);
        });
        return projects;
    }

    public FolderNode getFolderNode(ArrayList<String> folders, String folderName, String groupName, int groupType, String subGroupName, int subGroupType, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_FOLDER)) {
            FolderNode fNode = (FolderNode) node;
            if (fNode.title.equals(folderName)) {
                switch (fNode.parent.type) {
                    case ProjectItemNode.NODE_GROUP:
                        GroupNode gNode = (GroupNode) fNode.parent;
                        ProjectNode pNode = (ProjectNode) gNode.parent;
                        if (gNode.groupName.equals(groupName) && gNode.groupType == groupType && pNode.projectName.equals(projectName)) {
                            return fNode;
                        }
                        break;
                    case ProjectItemNode.NODE_SUBGROUP:
                        SubGroupNode sgNode = (SubGroupNode) fNode.parent;
                        GroupNode sgParent = (GroupNode) sgNode.parent;
                        ProjectNode gParent = (ProjectNode) sgNode.parent;
                        if (sgNode.subGroupName.equals(subGroupName) && sgNode.subGroupType == subGroupType
                                && sgParent.groupName.equals(groupName) && sgParent.groupType == groupType
                                && gParent.projectName.equals(projectName)) {
                            return fNode;
                        }
                        break;
                    case ProjectItemNode.NODE_FOLDER:
                        switch (fNode.originalParent.type) {
                            case ProjectItemNode.NODE_GROUP:
                                GroupNode fgNode = (GroupNode) fNode.originalParent;
                                if (fgNode.groupName.equals(groupName) && fgNode.groupType == groupType && fgNode.projectName.equals(projectName)) {
                                    return getFolderNode(fNode, folders);
                                }
                                break;
                            case ProjectItemNode.NODE_SUBGROUP:
                                SubGroupNode fsgNode = (SubGroupNode) fNode.originalParent;
                                GroupNode fsgParent = (GroupNode) fsgNode.parent;
                                ProjectNode fsgpParent = (ProjectNode) fsgParent.parent;
                                if (fsgNode.subGroupName.equals(subGroupName) && fsgNode.subGroupType == subGroupType
                                        && fsgParent.groupName.equals(groupName) && fsgParent.groupType == groupType
                                        && fsgpParent.projectName.equals(projectName)) {
                                    return getFolderNode(fNode, folders);
                                }
                                break;
                        }
                        break;
                }
            }
        }
        return null;
    }

    public FolderNode getFolderNode(FolderNode fNode, ArrayList<String> folders) {
        int count = folders.size() - 1;
        FolderNode temp = (FolderNode) fNode.parent;
        while (count > 0) {
            if (count == 1 && temp.title.equals(folders.get(0))) {
                return fNode;
            } else if (folders.contains(temp.title)) {
                temp = (FolderNode) temp.parent;
                count--;
            } else {
                break;
            }
        }
        return null;
    }

    public FileNode getFileNode(String name, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_FILE)) {
            if (node.title.equals(name) && node.parent.title.equals(groupName) && node.parent.parent.title.equals(projectName)) {
                return (FileNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
        return null;
    }

    public FileNode getFileNode(String name, ArrayList<String> folders, String subGroupName, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_FILE)) {
            FileNode file = (FileNode) node;
            if (file.title.equals(name)) {
                switch (file.parent.type) {
                    case ProjectItemNode.NODE_GROUP:
                        if (file.parent.title.equals(groupName) && file.parent.parent.title.equals(projectName)) {
                            return file;
                        }
                        break;
                    case ProjectItemNode.NODE_SUBGROUP:
                        if (file.parent.title.equals(subGroupName)
                                && file.parent.parent.title.equals(groupName)
                                && file.parent.parent.parent.title.equals(projectName)) {
                            return file;
                        }
                        break;
                    case ProjectItemNode.NODE_FOLDER:
                        FolderNode folder = (FolderNode) file.parent;
                        int size = folders.size();
                        boolean folderMatch = true;
                        while (size > 0) {
                            if (folder.title.equals(folders.get(size - 1))) {
                                if (size > 1) {
                                    folder = (FolderNode) folder.parent;
                                }
                                size--;
                            } else {
                                folderMatch = false;
                                break;
                            }
                        }
                        if (folderMatch) {
                            switch (folder.parent.type) {
                                case ProjectItemNode.NODE_GROUP:
                                    if (folder.parent.title.equals(groupName) && folder.parent.parent.title.equals(projectName)) {
                                        return file;
                                    }
                                    break;
                                case ProjectItemNode.NODE_SUBGROUP:
                                    if (folder.parent.title.equals(subGroupName)
                                            && folder.parent.parent.title.equals(groupName)
                                            && folder.parent.parent.parent.title.equals(projectName)) {
                                        return file;
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }
        }
        return null;
    }

    public FileNode getFileNode(String name, String subGroupName, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_FILE)) {
            if (node.title.equals(name) && node.parent.title.equals(subGroupName) && node.parent.parent.title.equals(groupName) && node.parent.parent.parent.title.equals(projectName)) {
                return (FileNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
        return null;
    }

    public void expandDirectories(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
