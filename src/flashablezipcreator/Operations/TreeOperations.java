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

    public ProjectItemNode addChildTo(ProjectItemNode parent, String childTitle, int childType) {

        switch (parent.type) {
            case ProjectItemNode.NODE_ROOT:
                return parent.addChild(new ProjectNode(childTitle, childType, parent));
            case ProjectItemNode.NODE_PROJECT:
                return parent.addChild(new GroupNode(childTitle, childType, (ProjectNode) parent));
            case ProjectItemNode.NODE_GROUP:
                switch (((GroupNode) parent).groupType) {
                    //Group of predefined locations
                    case GroupNode.GROUP_SYSTEM_APK:
                    case GroupNode.GROUP_SYSTEM_PRIV_APK:
                    case GroupNode.GROUP_DATA_APP:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                    case GroupNode.GROUP_OTHER:
                    case GroupNode.GROUP_AROMA_THEMES:
                    case GroupNode.GROUP_DELETE_FILES:
                    case GroupNode.GROUP_SCRIPT:
                        switch (childType) {
                            case ProjectItemNode.NODE_FILE:
                                return parent.addChild(new FileNode(childTitle, (GroupNode) parent));
                            case ProjectItemNode.NODE_FOLDER:
                                return parent.addChild(new FolderNode(childTitle, (GroupNode) parent));
                            default:
                                System.out.println("You cannot add subgroup for this type..!!");
                                break;
                        }
                        break;
                    //Group of predefined locations that need subgroups
                    case GroupNode.GROUP_SYSTEM_FONTS:
                    case GroupNode.GROUP_DATA_LOCAL:
                    case GroupNode.GROUP_SYSTEM_MEDIA:
                        switch (childType) {
                            case SubGroupNode.TYPE_SYSTEM_FONTS:
                            case SubGroupNode.TYPE_SYSTEM_MEDIA:
                            case SubGroupNode.TYPE_DATA_LOCAL:
                                return parent.addChild(new SubGroupNode(childTitle, childType, (GroupNode) parent));
                            case ProjectItemNode.NODE_FILE:
                                System.out.println("You cannot add files for this type..!!\nadd a subgroup and then files to it..!!");
                                break;
                        }
                        break;
                    //Group of custom location.
                    case GroupNode.GROUP_CUSTOM:
                        if (childType == SubGroupNode.TYPE_CUSTOM) {
                            return parent.addChild(new SubGroupNode(childTitle, childType, (GroupNode) parent));
                        }
                        break;
                    //here File Node can also act as child but due to different requirements of parameters,
                    //explicit call to another addChildTo function is required.
                }
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                System.out.println(parent.title);
                System.out.println(parent.type);
                switch (((SubGroupNode) parent).subGroupType) {
                    case SubGroupNode.TYPE_SYSTEM_FONTS:
                    case SubGroupNode.TYPE_SYSTEM_MEDIA:
                    case SubGroupNode.TYPE_DATA_LOCAL:
                        return parent.addChild(new FileNode(childTitle, (SubGroupNode) parent));
                }
            case ProjectItemNode.NODE_FOLDER:
                switch (childType) {
                    case ProjectItemNode.NODE_FOLDER:
                        return parent.addChild(new FolderNode(childTitle, (FolderNode) parent));
                    case ProjectItemNode.NODE_FILE:
                        return parent.addChild(new FileNode(childTitle, (FolderNode) parent));
                }

            default:
                System.out.println("Entered Default.");
        }
        return null;
    }

    public void renameNode(ProjectItemNode node, String newName) throws IOException {
        String oldName = "";
        switch (node.type) {
            case ProjectItemNode.NODE_ROOT:
                break;
            case ProjectItemNode.NODE_PROJECT:
                oldName = ((ProjectNode) node).path;
                ((ProjectNode) node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_GROUP:
                oldName = ((GroupNode) node).path;
                ((GroupNode) node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                oldName = ((SubGroupNode) node).path;
                ((SubGroupNode) node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_FOLDER:
                oldName = ((FolderNode) node).path;
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
        ProjectNode pNode = getProjectNode(projectName, projectType);
        if (pNode == null) {
            pNode = (ProjectNode) addChildTo(rootNode, projectName, projectType);
            System.out.println("Added project " + projectName + " project type " + projectType);
        }
        GroupNode gNode = getGroupNode(groupName, groupType, projectName);
        if (gNode == null) {
            gNode = (GroupNode) addChildTo(pNode, groupName, groupType);
        }
        SubGroupNode sgNode = null;
        if (!subGroupName.equals("")) {
            sgNode = getSubGroupNode(subGroupName, groupType, groupName, projectName);
            if (sgNode == null) {
                sgNode = (SubGroupNode) addChildTo(gNode, subGroupName, subGroupType);
            }
        }
        FolderNode folNode = null;
        if (folders.size() > 0) {
            int count = 1;
            for (String folder : folders) {
                FolderNode fNode = null;
                fNode = getFolderNode(folders, folder, groupName, groupType, subGroupName, subGroupType, projectName);
                if (count++ == 1 && fNode == null) {
                    if (sgNode != null) {
                        fNode = (FolderNode) addChildTo(sgNode, folder, ProjectItemNode.NODE_FOLDER);
                    } else {
                        fNode = (FolderNode) addChildTo(gNode, folder, ProjectItemNode.NODE_FOLDER);
                    }
                } else if (fNode == null) {
                    fNode = (FolderNode) addChildTo(folNode, folder, ProjectItemNode.NODE_FOLDER);
                }
                folNode = fNode;
            }
        }
        FileNode fileNode = null;
        if (folNode == null) {
            if (sgNode != null) {
                fileNode = (FileNode) addChildTo(sgNode, fileName, ProjectItemNode.NODE_FILE);
            } else {
                fileNode = (FileNode) addChildTo(gNode, fileName, ProjectItemNode.NODE_FILE);
            }
        } else {
            fileNode = (FileNode) addChildTo(folNode, fileName, ProjectItemNode.NODE_FILE);
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
//        ArrayList<ProjectItemNode> projectRom = new ArrayList<>();
//        ArrayList<ProjectItemNode> projectGapps = new ArrayList<>();
        ArrayList<ProjectItemNode> projectAroma = new ArrayList<>();
//        ArrayList<ProjectItemNode> projectNormal = new ArrayList<>();
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

//        projectRom.stream().forEach((node) -> {
//            projects.add(node);
//        });
//        projectGapps.stream().forEach((node) -> {
//            projects.add(node);
//        });
        projectAroma.stream().forEach((node) -> {
            projects.add(node);
        });

//        projectNormal.stream().forEach((node) -> {
//            projects.add(node);
//        });
        projectThemes.stream().forEach((node) -> {
            projects.add(node);
        });
        return projects;
    }

    public ProjectNode getProjectNode(String name, int projectType) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_PROJECT)) {
            if (node.title.equals(name) && ((ProjectNode) node).projectType == projectType) {
                return (ProjectNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
        return null;
    }

    public GroupNode getGroupNode(String name, int groupType, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_GROUP)) {
            //System.out.println(projectName + ".. " + node.parent + " .. " + node.parent.title.equals(projectName));
            if (node.title.equals(name) && ((GroupNode) node).groupType == groupType && node.parent.title.equals(projectName)) {
                return (GroupNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
        return null;
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

    public SubGroupNode getSubGroupNode(String name, int subGroupType, String groupName, String projectName) {
        for (ProjectItemNode node : getNodeList(ProjectItemNode.NODE_SUBGROUP)) {
            if (node.title.equals(name) && ((SubGroupNode) node).subGroupType == subGroupType && node.parent.title.equals(groupName) && node.parent.parent.title.equals(projectName)) {
                return (SubGroupNode) node;
            }
        }
        System.out.println("Returning null in search for " + name);
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
