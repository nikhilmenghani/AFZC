/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
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
    
    public TreeOperations(){
        this.rootNode = MyTree.rootNode;
        this.model = MyTree.model;
    }

    public void addChildTo(ProjectItemNode parent, String childTitle, int childType) {

        switch (parent.type) {
            case ProjectItemNode.NODE_ROOT:
                parent.addChild(new ProjectNode(childTitle, childType, parent));
                break;
            case ProjectItemNode.NODE_PROJECT:
                parent.addChild(new GroupNode(childTitle, childType, (ProjectNode) parent));
                break;
            case ProjectItemNode.NODE_GROUP:
                switch (((GroupNode) parent).groupType) {
                    //Group of predefined locations
                    case GroupNode.GROUP_SYSTEM_APK:
                    case GroupNode.GROUP_SYSTEM_PRIV_APK:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                    case GroupNode.GROUP_DATA_APP:
                    case GroupNode.GROUP_OTHER:
                    case GroupNode.GROUP_AROMA_THEMES:
                    case GroupNode.GROUP_DELETE_FILES:
                    case GroupNode.GROUP_SCRIPT:
                        if (childType == ProjectItemNode.NODE_FILE) {
                            parent.addChild(new FileNode(childTitle, (GroupNode) parent));
                        } else {
                            System.out.println("You cannot add subgroup for this type..!!");
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
                                parent.addChild(new SubGroupNode(childTitle, childType, (GroupNode) parent));
                                break;
                            case ProjectItemNode.NODE_FILE:
                                System.out.println("You cannot add files for this type..!!\nadd a subgroup and then files to it..!!");
                                break;
                        }
                        break;
                    //Group of custom location.
                    case GroupNode.GROUP_CUSTOM:
                        if (childType == SubGroupNode.TYPE_CUSTOM) {
                            parent.addChild(new SubGroupNode(childTitle, childType, (GroupNode) parent));
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
                        parent.addChild(new FileNode(childTitle, (SubGroupNode) parent));
                        break;
                }
            default:
                System.out.println("Entered Default.");
        }
    }

    public void addChildTo(ProjectItemNode parent, String childTitle, String installLocation, String permission) {
        if ((parent.type == ProjectItemNode.NODE_GROUP && ((GroupNode) parent).groupType == GroupNode.GROUP_CUSTOM)) {
            parent.addChild(new FileNode(childTitle, installLocation, permission, (GroupNode) parent));
        } else if ((parent.type == ProjectItemNode.NODE_SUBGROUP && ((SubGroupNode) parent).subGroupType == SubGroupNode.TYPE_CUSTOM)) {
            parent.addChild(new FileNode(childTitle, installLocation, permission, (SubGroupNode) parent));
        }
    }

    public void renameNode(ProjectItemNode node, String newName) throws IOException {
        String oldName = "";
        switch (node.type) {
            case ProjectItemNode.NODE_ROOT:
                break;
            case ProjectItemNode.NODE_PROJECT:
                oldName = ((ProjectNode)node).path;
                ((ProjectNode)node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_GROUP:
                 oldName = ((GroupNode)node).path;
                ((GroupNode)node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                oldName = ((SubGroupNode)node).path;
                ((SubGroupNode)node).renameMe(newName);
                //w.rename(oldName, newName);
                break;
            case ProjectItemNode.NODE_FILE:
                oldName = ((FileNode) node).fileSourcePath;
                ((FileNode) node).renameMe(newName);
                w.rename(oldName, newName);
                break;
        }
    }

    public void removeNode(ProjectItemNode node, DefaultTreeModel model) {
        node.removeMe();
    }

    public FileNode addFileToTree(String fileName, String subGroupName, int subGroupType, String groupName, int groupType, String projectName, int projectType,
            ProjectItemNode rootNode, DefaultTreeModel model) {
        if (getProjectNode(projectName, projectType) == null) {
            addChildTo(rootNode, projectName, projectType);
            System.out.println("Added project " + projectName + " project type " + projectType);
        }
        if (getGroupNode(groupName, groupType, projectName) == null) {
            addChildTo(getProjectNode(projectName, projectType), groupName, groupType);
        }
        if (getSubGroupNode(subGroupName, groupType, groupName, projectName) == null) {
            addChildTo(getGroupNode(groupName, groupType, projectName), subGroupName, subGroupType);
        }
        if (subGroupType == SubGroupNode.TYPE_CUSTOM) {
            addChildTo(getSubGroupNode(subGroupName, subGroupType, groupName, projectName), fileName, "", "");
        } else {
            addChildTo(getSubGroupNode(subGroupName, subGroupType, groupName, projectName), fileName, ProjectItemNode.NODE_FILE);
        }
        return getFileNode(fileName, subGroupName, groupName, projectName);
    }

    public FileNode addFileToTree(String fileName, String groupName, int groupType, String projectName, int projectType) {
        if (getProjectNode(projectName, projectType) == null) {
            addChildTo(rootNode, projectName, projectType);
            System.out.println("Added project " + projectName + " project type " + projectType);
        }
        if (getGroupNode(groupName, groupType, projectName) == null) {
            addChildTo(getProjectNode(projectName, projectType), groupName, groupType);
        }
        if (getFileNode(fileName, groupName, projectName) != null) {
            if (groupType == GroupNode.GROUP_OTHER) {
                fileName += "_1";
                fileName = getUniqueName(fileName);
            }
        }
        if (groupType == GroupNode.GROUP_CUSTOM) {
            addChildTo(getGroupNode(groupName, groupType, projectName), fileName, "", "");
        } else {
            addChildTo(getGroupNode(groupName, groupType, projectName), fileName, ProjectItemNode.NODE_FILE);
        }
        return getFileNode(fileName, groupName, projectName);
    }

    /*files with same name and located at multiple places are unique inspite of having same name.
     this is to prevent them from skipping.*/
    public String getUniqueName(String fileName) {
        if (repeatList.contains(fileName)) {
            fileName += "_1";
            return getUniqueName(fileName);
        } else {
            repeatList.add(fileName);
            return fileName;
        }
    }

    public void buildDirectory() throws IOException {
        buildDirectory(this.rootNode);
    }

    public void buildDirectory(ProjectItemNode node) throws IOException {
        if (!node.isLeaf()) {
            w.createFolder(((ProjectItemNode) node).path);
            for (int i = 0; i < node.getChildCount(); i++) {
                buildDirectory((ProjectItemNode) node.getChildAt(i));
            }
        } else {
            try {
                //this will create a file in project folder.
                w.writeFile(((FileNode) node).fileSourcePath, ((FileNode) node).path);
                //w.writeFile(((FileNode) node).fileSourcePath, ((FileNode) node).fileDestPath)
                //this will make location of file from user selected location to project location.
                ((FileNode) node).fileSourcePath = ((FileNode) node).path;
                //((FileNode) node).fileSourcePath = ((FileNode) node).fileDestPath;
            } catch (ClassCastException cce) {
                System.out.println("Exception caught while casting..");
            }
        }
    }

    //this will iterate from given node to leaf node i.e. File Node and return list of FileNodes.
//    public ArrayList<ProjectItemNode> parseNode(ProjectItemNode node) {
//        if (!node.isLeaf()) {
//            for (int i = 0; i < node.getChildCount(); i++) {
//                parseNode((ProjectItemNode) node.getChildAt(i));
//            }
//        } else {
//            try {
//                list.add((FileNode) node);
//            } catch (ClassCastException cce) {
//                System.out.println("The node is of type " + node.type);
//            }
//        }
//        return list;
//    }
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
        ArrayList<ProjectItemNode> projectRom = new ArrayList<>();
        ArrayList<ProjectItemNode> projectGapps = new ArrayList<>();
        ArrayList<ProjectItemNode> projectAroma = new ArrayList<>();
        ArrayList<ProjectItemNode> projectNormal = new ArrayList<>();
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

        projectRom.stream().forEach((node) -> {
            projects.add(node);
        });
        projectGapps.stream().forEach((node) -> {
            projects.add(node);
        });

        projectAroma.stream().forEach((node) -> {
            projects.add(node);
        });

        projectNormal.stream().forEach((node) -> {
            projects.add(node);
        });

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
