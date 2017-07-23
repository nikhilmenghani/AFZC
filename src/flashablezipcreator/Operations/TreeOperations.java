/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.Protocols.Identify;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.MyTree;
import flashablezipcreator.UserInterface.Preference;
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
        switch (node.prop.type) {
            case Types.NODE_ROOT:
                break;
            case Types.NODE_PROJECT:
                ((ProjectNode) node).renameMe(newName);
                break;
            case Types.NODE_GROUP:
                ((GroupNode) node).renameMe(newName);
                break;
            case Types.NODE_SUBGROUP:
                ((SubGroupNode) node).renameMe(newName);
                break;
            case Types.NODE_FOLDER:
                ((FolderNode) node).renameMe(newName);
                break;
            case Types.NODE_FILE:
                oldName = ((FileNode) node).prop.fileSourcePath;
                ((FileNode) node).renameMe(newName);
                //w.rename(oldName, newName); //check if this is required
                break;
        }
    }

    public void removeNode(ProjectItemNode node) {
        node.removeMe();
    }

    public FileNode addFileToTree(String fileName, String subGroupName, int subGroupType, String groupName, int groupType, String originalGroupType,
            ArrayList<String> folders, String projectName, int projectType, int modType) {
        NodeProperties np = new NodeProperties(projectName, projectType, modType, rootNode);
        ProjectNode pNode = (ProjectNode) rootNode.addChild(new ProjectNode(np), false);
        np = new NodeProperties(groupName, groupType, pNode);
        if (groupType == Types.GROUP_CUSTOM && !originalGroupType.equals("")) {
            np.location = Identify.getLocation(originalGroupType);
            np.permission = Identify.getPermissions(originalGroupType);
            np.reloadOriginalStringType();
            np.reloadZipPath();
        }
        GroupNode gNode = (GroupNode) pNode.addChild(new GroupNode(np), false);
        SubGroupNode sgNode = null;
        if (!subGroupName.equals("")) {
            np = new NodeProperties(subGroupName, subGroupType, gNode);
            sgNode = (SubGroupNode) gNode.addChild(new SubGroupNode(np), false);
        }
        FolderNode folNode = null;
        if (folders.size() > 0) {
            int count = 1;
            for (String folder : folders) {
                FolderNode fNode = null;
                if (count++ == 1) {
                    np = new NodeProperties(folder, gNode);
                    fNode = (FolderNode) gNode.addChild(new FolderNode(np), false);
                } else if (folNode != null) {
                    np = new NodeProperties(folder, folNode);
                    fNode = (FolderNode) folNode.addChild(new FolderNode(np), false);
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
            if (((ProjectItemNode) node.getChildAt(i)).prop.type == type) {
                switch (((ProjectItemNode) node.getChildAt(i)).prop.type) {
                    case Types.NODE_PROJECT:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case Types.NODE_GROUP:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case Types.NODE_FOLDER:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case Types.NODE_SUBGROUP:
                        if (!list.contains((ProjectItemNode) node.getChildAt(i))) {
                            list.add((ProjectItemNode) node.getChildAt(i));
                        }
                        break;
                    case Types.NODE_FILE:
                        if (Preference.pp.createZipType.equals("Normal") && node.prop.groupType == Types.GROUP_AROMA_THEMES) {
                            continue;
                        }
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
        ArrayList<ProjectItemNode> projectCustom = new ArrayList<>();
        ArrayList<ProjectItemNode> projectMod = new ArrayList<>();

        for (ProjectItemNode project : getNodeList(Types.NODE_PROJECT)) {
            switch (((ProjectNode) project).prop.projectType) {
                case Types.PROJECT_AROMA:
                    projectAroma.add(project);
                    break;
                case Types.PROJECT_THEMES:
                    projectThemes.add(project);
                    break;
                case Types.PROJECT_CUSTOM:
                    projectCustom.add(project);
                    break;
                case Types.PROJECT_MOD:
                    projectMod.add(project);

            }
        }
        projectAroma.stream().forEach((node) -> {
            projects.add(node);
        });
        projectThemes.stream().forEach((node) -> {
            projects.add(node);
        });
        projectCustom.stream().forEach((node) -> {
            projects.add(node);
        });
        projectMod.stream().forEach((node) -> {
            projects.add(node);
        });
        return projects;
    }

    public FolderNode getFolderNode(ArrayList<String> folders, String folderName, String groupName, int groupType, String subGroupName, int subGroupType, String projectName) {
        for (ProjectItemNode node : getNodeList(Types.NODE_FOLDER)) {
            FolderNode fNode = (FolderNode) node;
            if (fNode.prop.title.equals(folderName)) {
                switch (fNode.prop.parent.prop.type) {
                    case Types.NODE_GROUP:
                        GroupNode gNode = (GroupNode) fNode.prop.parent;
                        ProjectNode pNode = (ProjectNode) gNode.prop.parent;
                        if (gNode.prop.groupName.equals(groupName) && gNode.prop.groupType == groupType && pNode.prop.projectName.equals(projectName)) {
                            return fNode;
                        }
                        break;
                    case Types.NODE_SUBGROUP:
                        SubGroupNode sgNode = (SubGroupNode) fNode.prop.parent;
                        GroupNode sgParent = (GroupNode) sgNode.prop.parent;
                        ProjectNode gParent = (ProjectNode) sgNode.prop.parent;
                        if (sgNode.prop.subGroupName.equals(subGroupName) && sgNode.prop.subGroupType == subGroupType
                                && sgParent.prop.groupName.equals(groupName) && sgParent.prop.groupType == groupType
                                && gParent.prop.projectName.equals(projectName)) {
                            return fNode;
                        }
                        break;
                    case Types.NODE_FOLDER:
                        switch (fNode.prop.originalParent.prop.type) {
                            case Types.NODE_GROUP:
                                GroupNode fgNode = (GroupNode) fNode.prop.originalParent;
                                if (fgNode.prop.groupName.equals(groupName) && fgNode.prop.groupType == groupType && fgNode.prop.projectName.equals(projectName)) {
                                    return getFolderNode(fNode, folders);
                                }
                                break;
                            case Types.NODE_SUBGROUP:
                                SubGroupNode fsgNode = (SubGroupNode) fNode.prop.originalParent;
                                GroupNode fsgParent = (GroupNode) fsgNode.prop.parent;
                                ProjectNode fsgpParent = (ProjectNode) fsgParent.prop.parent;
                                if (fsgNode.prop.subGroupName.equals(subGroupName) && fsgNode.prop.subGroupType == subGroupType
                                        && fsgParent.prop.groupName.equals(groupName) && fsgParent.prop.groupType == groupType
                                        && fsgpParent.prop.projectName.equals(projectName)) {
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
        FolderNode temp = (FolderNode) fNode.prop.parent;
        while (count > 0) {
            if (count == 1 && temp.prop.title.equals(folders.get(0))) {
                return fNode;
            } else if (folders.contains(temp.prop.title)) {
                temp = (FolderNode) temp.prop.parent;
                count--;
            } else {
                break;
            }
        }
        return null;
    }

    public FileNode getFileNode(String name, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(Types.NODE_FILE)) {
            if (node.prop.title.equals(name) && node.prop.parent.prop.title.equals(groupName) && node.prop.parent.prop.parent.prop.title.equals(projectName)) {
                return (FileNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
        return null;
    }

    public FileNode getFileNode(String name, ArrayList<String> folders, String subGroupName, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(Types.NODE_FILE)) {
            FileNode file = (FileNode) node;
            if (file.prop.title.equals(name)) {
                switch (file.prop.parent.prop.type) {
                    case Types.NODE_GROUP:
                        if (file.prop.parent.prop.title.equals(groupName) && file.prop.parent.prop.parent.prop.title.equals(projectName)) {
                            return file;
                        }
                        break;
                    case Types.NODE_SUBGROUP:
                        if (file.prop.parent.prop.title.equals(subGroupName)
                                && file.prop.parent.prop.parent.prop.title.equals(groupName)
                                && file.prop.parent.prop.parent.prop.parent.prop.title.equals(projectName)) {
                            return file;
                        }
                        break;
                    case Types.NODE_FOLDER:
                        FolderNode folder = (FolderNode) file.prop.parent;
                        int size = folders.size();
                        boolean folderMatch = true;
                        while (size > 0) {
                            if (folder.prop.title.equals(folders.get(size - 1))) {
                                if (size > 1) {
                                    folder = (FolderNode) folder.prop.parent;
                                }
                                size--;
                            } else {
                                folderMatch = false;
                                break;
                            }
                        }
                        if (folderMatch) {
                            switch (folder.prop.parent.prop.type) {
                                case Types.NODE_GROUP:
                                    if (folder.prop.parent.prop.title.equals(groupName) && folder.prop.parent.prop.parent.prop.title.equals(projectName)) {
                                        return file;
                                    }
                                    break;
                                case Types.NODE_SUBGROUP:
                                    if (folder.prop.parent.prop.title.equals(subGroupName)
                                            && folder.prop.parent.prop.parent.prop.title.equals(groupName)
                                            && folder.prop.parent.prop.parent.prop.parent.prop.title.equals(projectName)) {
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
        for (ProjectItemNode node : getNodeList(Types.NODE_FILE)) {
            if (node.prop.title.equals(name) && node.prop.parent.prop.title.equals(subGroupName) && node.prop.parent.prop.parent.prop.title.equals(groupName)
                    && node.prop.parent.prop.parent.prop.parent.prop.title.equals(projectName)) {
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
