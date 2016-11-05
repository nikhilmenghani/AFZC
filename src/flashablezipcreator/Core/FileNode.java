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

    public FileNode(String fileSourcePath, GroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        this.installLocation = parent.getLocation();
        setPermissions(parent);
        super.path = parent.path + File.separator + (new File(fileSourcePath)).getName();
        //this.fileDestPath = parent.path + File.separator + (new File(fileSourcePath)).getName();
        //this.fileSourcePath = this.fileDestPath;
        this.fileSourcePath = (new File(fileSourcePath)).getAbsolutePath();
        fileZipPath = getZipPath();
    }

    public FileNode(String fileSourcePath, SubGroupNode parent) {
        super((new File(fileSourcePath)).getName(), ProjectItemNode.NODE_FILE, parent);
        this.installLocation = parent.getLocation();
        setPermissions(parent);
        super.path = parent.path + File.separator + (new File(fileSourcePath)).getName();
        this.fileSourcePath = fileSourcePath;
        //this.fileDestPath = parent.path + File.separator + (new File(fileSourcePath)).getName();
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

    public void setPermissions(GroupNode parent) {
        switch (parent.groupType) {
            case GroupNode.GROUP_SYSTEM_APK:
            case GroupNode.GROUP_SYSTEM_PRIV_APK:
                setPermissions("0", "0", "0644", parent.location + "/" + title);
                break;
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case GroupNode.GROUP_DATA_APP:
                setPermissions("1000", "1000", "0644", parent.location + "/" + title);
                break;

        }
    }

    public void setPermissions(SubGroupNode parent) {
        switch (parent.subGroupType) {
            case SubGroupNode.TYPE_SYSTEM_FONTS:
                setPermissions("1000", "1000", "0644", parent.location + "/" + title);
                break;
            case SubGroupNode.TYPE_SYSTEM_MEDIA:
                setPermissions("1000", "1000", "0644", parent.location + "/" + "bootanimation.zip");
                break;
            case SubGroupNode.TYPE_DATA_LOCAL:
                setPermissions("0", "0", "0777", parent.location + "/" + "bootanimation.zip");
                break;
        }
    }

    //this will generate a path that will be used as destination path of file in output zip.
    public final String getZipPath() {
        String str = "";
        str = super.path;
        //str = fileDestPath;
        //System.out.println("String before : " + str);
        str = str.substring(str.indexOf(File.separator) + 1, str.length());
        str = str.substring(str.indexOf(File.separator) + 1, str.length());
        switch (parent.type) {
            case ProjectItemNode.NODE_GROUP:
                switch (((GroupNode) parent).groupType) {
                    case GroupNode.GROUP_SYSTEM_APK:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_app" + File.separator + str;
                        break;
                    case GroupNode.GROUP_SYSTEM_PRIV_APK:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_priv_app" + File.separator + str;
                        break;
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_media_alarms" + File.separator + str;
                        break;
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_media_notifications" + File.separator + str;
                        break;
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_media_ringtones" + File.separator + str;
                        break;
                    case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "system_media_ui" + File.separator + str;
                        break;
                    case GroupNode.GROUP_DATA_APP:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "data_app" + File.separator + str;
                        break;
                    case GroupNode.GROUP_CUSTOM:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "custom" + File.separator + str;
                        break;
                    case GroupNode.GROUP_AROMA_THEMES:
                        //return this.fileDestPath.replaceAll("\\\\", "/");
                        return super.path.replaceAll("\\\\", "/");
                    case GroupNode.GROUP_DELETE_FILES:
                        //Export.java will handle
                        break;
                    case GroupNode.GROUP_SCRIPT:
                        str = getProjectType((ProjectNode) parent.parent) + File.separator + ((ProjectNode) parent.parent).title + File.separator
                                + "script" + File.separator + str;
                        break;
                    //case GroupNode.GROUP_OTHER not needed and
                }
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                switch (((SubGroupNode) parent).subGroupType) {
                    case SubGroupNode.TYPE_SYSTEM_FONTS:
                        str = getProjectType((ProjectNode) parent.parent.parent) + File.separator + ((ProjectNode) parent.parent.parent).title + File.separator
                                + "system_fonts" + File.separator + str;
                        break;
                    case SubGroupNode.TYPE_SYSTEM_MEDIA:
                        str = getProjectType((ProjectNode) parent.parent.parent) + File.separator + ((ProjectNode) parent.parent.parent).title + File.separator
                                + "system_media" + File.separator + str;
                        break;
                    case SubGroupNode.TYPE_DATA_LOCAL:
                        str = getProjectType((ProjectNode) parent.parent.parent) + File.separator + ((ProjectNode) parent.parent.parent).title + File.separator
                                + "data_local" + File.separator + str;
                        break;
                    case SubGroupNode.TYPE_CUSTOM:
                        str = getProjectType((ProjectNode) parent.parent.parent) + File.separator + ((ProjectNode) parent.parent.parent).title + File.separator
                                + "custom" + File.separator + str;
                        break;
                }
                break;
        }
        str = "customize" + File.separator + str;
        //System.out.println("String after : " + str);
        str = str.replaceAll("\\\\", "/");
        return str;
    }

    public String getDeleteLocation() {
        return ((GroupNode) parent).getLocation() + "/" + title;
    }

    public void setPermissions(String i, String j, String k, String path) {
        this.filePermission = i + ", " + j + ", " + k + ", \"" + path + "\"";
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
