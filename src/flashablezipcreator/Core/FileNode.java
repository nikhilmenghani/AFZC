/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preference;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public final class FileNode extends ProjectItemNode {

    public FileNode(NodeProperties properties){
        super(properties);
    }
    
    public FileNode(String fileSourcePath, GroupNode parent) {
        super((new File(fileSourcePath)).getName(), Types.NODE_FILE, parent);
        prop.originalParent = parent;
        File file = new File(fileSourcePath);
        prop.fileSourcePath = fileSourcePath;
        prop.fileInstallLocation = parent.prop.location.replaceAll("\\\\", "/");
        prop.path = parent.prop.path + File.separator + file.getName();
        prop.setPermissions(parent.prop.permission, prop.title);
        prop.setPermissions = parent.prop.setPermissions;
        prop.fileSourcePath = file.getAbsolutePath();
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
        prop.fileZipPath = parent.prop.zipPath + "/" + prop.title;
        if (parent.prop.groupType == Types.GROUP_AROMA_THEMES) {
            prop.fileZipPath = prop.path.replaceAll("\\\\", "/");
        }
        prop.location = parent.prop.location;
    }

    public FileNode(String fileSourcePath, SubGroupNode parent) {
        super((new File(fileSourcePath)).getName(), Types.NODE_FILE, parent);
        prop.originalParent = parent.prop.originalParent;
        File file = new File(fileSourcePath);
        prop.fileInstallLocation = parent.prop.location.replaceAll("\\\\", "/");
        prop.path = parent.prop.path + File.separator + file.getName();
        if (parent.prop.isBootAnimationGroup) {
            prop.setPermissions(parent.prop.permission, "bootanimation.zip");
        } else {
            prop.setPermissions(parent.prop.permission, prop.title);
        }
        prop.setPermissions = parent.prop.setPermissions;
        prop.fileSourcePath = fileSourcePath;
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
        prop.fileZipPath = parent.prop.zipPath + "/" + prop.title;
        prop.location = parent.prop.location;
    }

    public FileNode(String fileSourcePath, FolderNode parent) {
        super((new File(fileSourcePath)).getName(), Types.NODE_FILE, parent);
        prop.fileInstallLocation = parent.prop.folderLocation.replaceAll("\\\\", "/");
        prop.path = parent.prop.path + File.separator + (new File(fileSourcePath)).getName();
        prop.description = prop.parent.prop.description;
        if (parent.prop.isBootAnimationGroup) {
            prop.setPermissions(parent.prop.permission, "bootanimation.zip");
        } else if (((GroupNode) parent.prop.originalParent).prop.groupType == Types.GROUP_DATA_APP) {
            prop.setPermissions(parent.prop.permission, "base.apk");
        } else {
            prop.setPermissions(parent.prop.permission, prop.title);
        }
        prop.setPermissions = parent.prop.setPermissions;
        prop.fileSourcePath = fileSourcePath;
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
        prop.fileZipPath = parent.prop.zipPath + "/" + prop.title;
        prop.location = parent.prop.location;
    }

//    public void updateZipPath() {
//        prop.fileZipPath = prop.parent.prop.zipPath + "/" + prop.title;
//    }

//    public void updateInstallLocation() {
//        prop.fileInstallLocation = prop.parent.prop.location.replaceAll("\\\\", "/");
//    }

    public String getDeleteLocation() {
        return ((GroupNode) parent).getLocation() + "/" + prop.title;
    }

    public void setDescription(String desc) {
        prop.description = desc;
    }

    public void renameMe(String newName) throws IOException {
        prop.title = newName;
        prop.fileName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        //following lines should be considered when we are actually renaming the file. which we aren't currently.
//        Path p = FileSystems.getDefault().getPath(prop.fileSourcePath);
//        prop.fileSourcePath = p.resolveSibling(newName).toString();
        prop.fileZipPath = prop.parent.prop.zipPath + "/" + prop.title;
    }

}
