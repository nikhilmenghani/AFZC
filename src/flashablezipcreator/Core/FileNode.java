/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 *
 * @author Nikhil
 */
public final class FileNode extends ProjectItemNode {

    public String fileName;
    public String fileSourcePath;
    public String installLocation = "";
    public String belongsToGroup;
    public String filePermission = "";
    public String description = "";
    public String fileZipPath = "";
    public String projectName;
    public String originalGroupType;
    public String groupLocation;

    public FileNode(String fileSourcePath, GroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        File file = new File(fileSourcePath);
        this.installLocation = parent.location.replaceAll("\\\\", "/");
        super.path = parent.path + File.separator + file.getName();
        setPermissions(parent.permission, title);
        this.fileSourcePath = file.getAbsolutePath();
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        fileZipPath = parent.zipPath + "/" + title;
        if (parent.groupType == GroupNode.GROUP_AROMA_THEMES) {
            fileZipPath = super.path.replaceAll("\\\\", "/");
        }
        super.extractZipPath = (parent.extractZipPath + "/" + "afzc_temp").replaceAll("\\\\", "/");
        this.groupLocation = parent.location;
    }

    public FileNode(String fileSourcePath, SubGroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        File file = new File(fileSourcePath);
        this.installLocation = parent.location.replaceAll("\\\\", "/");
        super.path = parent.path + File.separator + file.getName();
        if (parent.isBootAnimationGroup) {
            setPermissions(parent.permission, "bootanimation.zip");
        } else {
            setPermissions(parent.permission, title);
        }
        this.fileSourcePath = fileSourcePath;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        fileZipPath = parent.zipPath + "/" + title;
        super.extractZipPath = (parent.extractZipPath + "/" + "afzc_temp").replaceAll("\\\\", "/");
        this.groupLocation = parent.location;
    }

    public FileNode(String fileSourcePath, FolderNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        this.installLocation = parent.folderLocation.replaceAll("\\\\", "/");
        super.path = parent.path + File.separator + (new File(fileSourcePath)).getName();
        parent.description = this.description;
        if (parent.isBootAnimationGroup) {
            setPermissions(parent.permission, "bootanimation.zip");
        } else {
            setPermissions(parent.permission, title);
        }
        this.fileSourcePath = fileSourcePath;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        fileZipPath = parent.zipPath + "/" + title;
        super.extractZipPath = (parent.extractZipPath + "/" + "afzc_temp").replaceAll("\\\\", "/");
        this.groupLocation = parent.location;
    }

    //this will generate a path that will be used as destination path of file in output zip.
    public final String getZipPath() {
        return parent.zipPath + "/" + title;
    }

    public void updateZipPath() {
        fileZipPath = parent.zipPath + "/" + title;
        super.extractZipPath = (parent.extractZipPath + "/" + "afzc_temp").replaceAll("\\\\", "/");
    }

    public void updateInstallLocation() {
        this.installLocation = parent.location.replaceAll("\\\\", "/");
    }

    public String getDeleteLocation() {
        return ((GroupNode) parent).getLocation() + "/" + title;
    }

    public void setPermissions(String parentPermission, String title) {
        this.filePermission = (parentPermission + "\"" + this.installLocation + "/" + title + "\"").replaceAll("\\\\", "/");
    }

    public String getProjectType(ProjectNode project) {
        switch (project.projectType) {
            case ProjectNode.PROJECT_AROMA:
                return "aroma";
        }
        return null;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void renameMe(String newName) throws IOException {
        super.setTitle(newName);
        this.fileName = newName;
        super.path = parent.path + File.separator + newName;
        //this.fileDestPath = parent.path + File.separator + newName;
        Path p = FileSystems.getDefault().getPath(this.fileSourcePath);
        this.fileSourcePath = p.resolveSibling(newName).toString();
        this.fileZipPath = this.parent.zipPath + "/" + title;
    }

}
