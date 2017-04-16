/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preferences;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class FolderNode extends ProjectItemNode {

    public FolderNode(NodeProperties properties) {
        super(properties);
    }

    public FolderNode(String title, int type, ProjectItemNode parent) {
        super(title, type, parent);
        prop.originalParent = parent;
    }

    public FolderNode(String title, GroupNode parent) {
        super(title, Types.NODE_FOLDER, parent);
        prop.originalParent = parent;
        prop.groupParent = parent;
        if (!title.endsWith("-1") && parent.prop.groupType == Types.GROUP_DATA_APP) {
            title += "-1";
            prop.title = title;
        }
        prop.folderName = title;
        prop.path = parent.prop.path + File.separator + title;
        prop.zipPath = parent.prop.zipPath + "/" + prop.folderZipPathPrefix + title;
        prop.location = parent.prop.location;
        prop.folderLocation = parent.prop.location + File.separator + title;
        prop.permission = parent.prop.permission;
        prop.setPermissions = parent.prop.setPermissions;
        prop.defaultFolderPerm = (Preferences.pp.useUniversalBinary) ? "1000" + " " + "1000" + " " + "0755" + " "
                : "1000" + ", " + "1000" + ", " + "0755" + ", ";
        setPermissions();
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
    }

    public FolderNode(String title, FolderNode parent) {
        super(title, Types.NODE_FOLDER, parent);
        prop.originalParent = parent.prop.originalParent;
        prop.groupParent = parent.prop.groupParent;
        prop.folderName = title;
        prop.path = parent.prop.path + File.separator + title;
        prop.zipPath = parent.prop.zipPath + "/" + prop.folderZipPathPrefix + title;
        prop.location = parent.prop.location;
        prop.folderLocation = parent.prop.folderLocation + File.separator + title;
        prop.permission = parent.prop.permission;
        prop.setPermissions = parent.prop.setPermissions;
        prop.defaultFolderPerm = (Preferences.pp.useUniversalBinary) ? "1000" + " " + "1000" + " " + "0755" + " "
                : "1000" + ", " + "1000" + ", " + "0755" + ", ";
        setPermissions();
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
    }

    public void setPermissions() {
        prop.folderPermission = prop.defaultFolderPerm + "\"" + prop.folderLocation + "\"";
        prop.folderPermission = prop.folderPermission.replaceAll("\\\\", "/");
    }

    public void renameMe(String newName) throws IOException {
        prop.title = newName;
        prop.folderName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.folderZipPathPrefix + newName;
        prop.location = prop.parent.prop.location + File.separator + newName;
        prop.folderLocation = prop.folderLocation.replaceAll("\\\\", "/");
        prop.folderLocation = prop.folderLocation.substring(0, prop.folderLocation.lastIndexOf("/") + 1) + newName;
        setPermissions();
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public String getLocation() {
        return prop.location;
    }

    public void updateZipPath() {
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.folderZipPathPrefix + prop.title;
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : prop.children) {
            switch (node.prop.type) {
                case Types.NODE_FILE:
                    ((FileNode) node).prop.updateFileZipPath();
                    ((FileNode) node).prop.updateFileInstallLocation();
                    if (prop.isBootAnimationGroup) {
                        ((FileNode) node).prop.setPermissions(prop.owner, prop.group, prop.perm, "bootanimation.zip");
                    } else {
                        ((FileNode) node).prop.setPermissions(prop.owner, prop.group, prop.perm, ((FileNode) node).prop.title);
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
