/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class FileNode extends ProjectItemNode {

    public String fileName;
    public String fileSourcePath;
    //public String fileDestPath;
    public String installLocation = "";
    public String belongsToGroup;
    public String filePermission = "";
    public String description = "";
    public String fileZipPath = "";
    public String projectName;
    public String originalGroupType;

    public FileNode(String fileSourcePath, GroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        File file = new File(fileSourcePath);
        this.installLocation = parent.getLocation().replaceAll("\\\\", "/");
        super.path = parent.path + File.separator + file.getName();
        setPermissions(parent.permission, title);
        //this.fileDestPath = parent.path + File.separator + (new File(fileSourcePath)).getName();
        //this.fileSourcePath = this.fileDestPath;
        this.fileSourcePath = file.getAbsolutePath();
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        fileZipPath = getZipPath();
    }

    public FileNode(String fileSourcePath, SubGroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        File file = new File(fileSourcePath);
        this.installLocation = parent.getLocation().replaceAll("\\\\", "/");
        super.path = parent.path + File.separator + file.getName();
        if (parent.isBootAnimationGroup) {
            setPermissions(parent.permission, "bootanimation.zip");
        } else {
            setPermissions(parent.permission, title);
        }
        this.fileSourcePath = fileSourcePath;
        //this.fileDestPath = parent.path + File.separator + (new File(fileSourcePath)).getName();
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        fileZipPath = getZipPath();
    }

    public FileNode(String fileSourcePath, FolderNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        this.installLocation = parent.getLocation().replaceAll("\\\\", "/");
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
        fileZipPath = getZipPath();
    }

    public FileNode(String fileSourcePath, String installLocation, String permission, ProjectItemNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        this.installLocation = installLocation;
        fileName = title;
        super.path = parent.path + File.separator + (new File(fileSourcePath)).getName();
        this.fileSourcePath = fileSourcePath;
        //this.fileDestPath = parent.path + File.separator + (new File(fileSourcePath)).getName();
        this.filePermission = permission;
        belongsToGroup = (parent.type == SubGroupNode.TYPE_CUSTOM) ? parent.parent.toString() : parent.toString();
        fileZipPath = getZipPath();
    }

    //this will generate a path that will be used as destination path of file in output zip.
    public final String getZipPath() {
        String str = "";
        str = super.path;
        //str = fileDestPath;
        //System.out.println("String before : " + str);
        //str = str.substring(str.indexOf(File.separator) + 1, str.length());
        //str = str.substring(str.indexOf(File.separator) + 1, str.length());
        str = "aroma" + File.separator + this.projectName + File.separator
                + this.originalGroupType + File.separator + str;
        str = "customize" + File.separator + str;
        //System.out.println("String after : " + str);
        str = str.replaceAll("\\\\", "/");
        return str;
    }

    public String getDeleteLocation() {
        return ((GroupNode) parent).getLocation() + "/" + title;
    }

    public void setPermissions(String parentPermission, String title) {
        this.filePermission = (parentPermission + "\"" + this.installLocation + "/" + title + "\"").replaceAll("\\\\", "/");;
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
        Path p = FileSystems.getDefault().getPath(this.fileSourcePath);;
        this.fileSourcePath = p.resolveSibling(newName).toString();
        this.fileZipPath = getZipPath();
    }

}
