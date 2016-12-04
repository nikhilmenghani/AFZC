/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class FolderNode extends ProjectItemNode {

    String folderName;
    String permission;
    boolean isBootAnimationGroup = false;
    ProjectItemNode originalParent;
    String projectName;
    String originalGroupType;
    public String description;
    String zipPathPrefix = "Folder_";

    public FolderNode(String title, int type, ProjectItemNode parent) {
        super(title, type, parent);
        this.originalParent = parent;
    }

    public FolderNode(String title, GroupNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + title;
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + title;
        super.location = parent.location + File.separator + title;
        this.permission = parent.permission;
        this.originalParent = parent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public FolderNode(String title, SubGroupNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + title;
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + title;
        super.location = parent.location + File.separator + title;
        this.permission = parent.permission;
        this.originalParent = parent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public FolderNode(String title, FolderNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + title;
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + title;
        super.location = parent.location + File.separator + title;
        this.permission = parent.permission;
        this.originalParent = parent.originalParent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public void renameMe(String newName) throws IOException {
        super.setTitle(newName);
        this.folderName = newName;
        super.path = parent.path + File.separator + newName;
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + newName;
        super.location = parent.location + File.separator + newName;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public String getLocation() {
        return location;
    }

    public void updateZipPath() {
        super.zipPath = parent.zipPath + "/" + zipPathPrefix + title;
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : children) {
            switch (node.type) {
                case ProjectItemNode.NODE_FILE:
                    ((FileNode) node).updateZipPath();
                    ((FileNode) node).updateInstallLocation();
                    if (this.isBootAnimationGroup) {
                        ((FileNode) node).setPermissions(this.permission, "bootanimation.zip");
                    } else {
                        ((FileNode) node).setPermissions(this.permission, ((FileNode) node).title);
                    }
                    break;
            }
        }
    }

    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
    }
}
