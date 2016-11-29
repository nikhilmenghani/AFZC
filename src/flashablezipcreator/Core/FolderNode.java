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
    String location;
    String permission;
    boolean isBootAnimationGroup = false;
    ProjectItemNode originalParent;
    String projectName;
    String originalGroupType;
    public String description;

    public FolderNode(String title, int type, ProjectItemNode parent) {
        super(title, type, parent);
        this.originalParent = parent;
    }

    public FolderNode(String title, GroupNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + "F_" + title;
        this.location = parent.getLocation() + File.separator + "F_" + title;
        this.permission = getPermissions(parent);
        this.originalParent = parent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public FolderNode(String title, SubGroupNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + "F_" + title;
        this.location = parent.getLocation() + File.separator + "F_" + title;
        this.originalParent = parent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public FolderNode(String title, FolderNode parent) {
        super(title, ProjectItemNode.NODE_FOLDER, parent);
        this.folderName = title;
        super.path = parent + File.separator + "F_" + title;
        this.location = parent.getLocation() + File.separator + "F_" + title;
        this.permission = parent.permission;
        this.originalParent = parent.originalParent;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
    }

    public String getPermissions(GroupNode parent) {
        switch (parent.groupType) {
            case GroupNode.GROUP_SYSTEM_APK:
            case GroupNode.GROUP_SYSTEM_PRIV_APK:
                setPermissions("0", "0", "0644", parent.location + "/");
                break;
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case GroupNode.GROUP_DATA_APP:
                setPermissions("1000", "1000", "0644", parent.location + "/");
                break;
        }
        return this.permission;
    }

    public String getPermissions(SubGroupNode parent) {
        switch (parent.subGroupType) {
            case SubGroupNode.TYPE_SYSTEM_FONTS:
                setPermissions("1000", "1000", "0644", parent.location + "/");
                break;
            case SubGroupNode.TYPE_SYSTEM_MEDIA:
            case SubGroupNode.TYPE_DATA_LOCAL:
                setPermissions("1000", "1000", "0644", parent.location + "/");
                this.isBootAnimationGroup = true;
                break;
        }
        return this.permission;
    }

    public String setPermissions(String i, String j, String k, String path) {
        this.permission = i + ", " + j + ", " + k + ", \"" + path;
        return this.permission;
    }

    public void renameMe(String newName) throws IOException {
        super.setTitle(newName);
        this.folderName = newName;
        super.path = parent.path + File.separator + newName;
        this.updateChildrenPath();
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
        for (ProjectItemNode node : children) {
            if (node.type == ProjectItemNode.NODE_FILE) {
                ((FileNode) node).fileZipPath = ((FileNode) node).getZipPath();

            }
        }
    }
}
