/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Identify;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Types;
import static flashablezipcreator.UserInterface.MyTree.rootNode;
import flashablezipcreator.UserInterface.Preferences;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public final class NodeProperties {

    public String title;
    public String path;
    public String zipPath;
    public String location = "";
    public String groupName;
    public String subGroupName;
    public String permission = "";
    public String owner = "";
    public String group = "";
    public String perm = "";
//    public String groupPermission = "";
    public String folderPermission = "";
    public String defaultFolderPerm = "";
    public String propFile;
    public String extension = "";
    public String projectName;
    public String originalGroupType;
    public String updater_script = "";
    public String aroma_config = "";
    public String androidVersion = Project.androidVersion;
    public String releaseVersion = Project.releaseVersion;
    public String projectZipPathPrefix = "Project_";
    public String groupZipPathPrefix = "Group_";
    public String subGroupZipPathPrefix = "SubGroup_";
    public String folderZipPathPrefix = "Folder_";
    public String typePrefix = "Type_";
    public String locSeparator = "Loc_";
    public String permSeparator = "Perm_";
    public String folderName;
    public String folderLocation;
    public String description = "";
    public String fileName;
    public String fileSourcePath;
    public String fileInstallLocation;
    public String filePermission;
    public String fileZipPath;
    public String value;
    public int type;
    public int projectType;
    public int groupType;
    public int subGroupType;
    public int modType;
    public boolean createZip = true; //when multiple projects will be loaded, this will help in choosing which one to create zip of.
    public boolean isBootAnimationGroup = false;
    public boolean isSelectBox = false;
    public boolean setPermissions = true;
    public ProjectItemNode parent;
    public ProjectItemNode originalParent;
    public GroupNode groupParent;
    public SubGroupNode subGroupParent;
    public ProjectNode projectParent;
    public FolderNode folderParent;
    public ArrayList<ProjectItemNode> children = new ArrayList<>();
    public byte[] update_binary = null;
    public byte[] update_binary_installer = null;

    public NodeProperties() {

    }

    public NodeProperties(String title, GroupNode parent) {
        this.title = title;
        this.type = Types.NODE_FOLDER;
        this.parent = parent;
        originalParent = parent;
        groupParent = parent;
        if (!title.endsWith("-1") && parent.prop.groupType == Types.GROUP_DATA_APP) {
            this.title += "-1";
            this.title = title;
        }
        folderName = title;
        path = parent.prop.path + File.separator + title;
        zipPath = parent.prop.zipPath + "/" + folderZipPathPrefix + title;
        location = parent.prop.location;
        folderLocation = parent.prop.location + File.separator + title;
        permission = parent.prop.permission;
        owner = parent.prop.owner;
        group = parent.prop.group;
        perm = parent.prop.perm;
        setPermissions = parent.prop.setPermissions;
        defaultFolderPerm = (Preferences.pp.useUniversalBinary) ? "1000" + " " + "1000" + " " + "0755" + " "
                : "1000" + ", " + "1000" + ", " + "0755" + ", ";
        setPermissions();
        projectName = parent.prop.projectName;
        originalGroupType = parent.prop.originalGroupType;
    }

    public NodeProperties(String title, FolderNode parent) {
        this.title = title;
        this.type = Types.NODE_FOLDER;
        this.parent = parent;
        originalParent = parent.prop.originalParent;
        groupParent = parent.prop.groupParent;
        folderName = title;
        path = parent.prop.path + File.separator + title;
        zipPath = parent.prop.zipPath + "/" + folderZipPathPrefix + title;
        location = parent.prop.location;
        folderLocation = parent.prop.folderLocation + File.separator + title;
        permission = parent.prop.permission;
        owner = parent.prop.owner;
        group = parent.prop.group;
        perm = parent.prop.perm;
        setPermissions = parent.prop.setPermissions;
        defaultFolderPerm = (Preferences.pp.useUniversalBinary) ? "1000" + " " + "1000" + " " + "0755" + " "
                : "1000" + ", " + "1000" + ", " + "0755" + ", ";
        setPermissions();
        projectName = parent.prop.projectName;
        originalGroupType = parent.prop.originalGroupType;
    }

    public NodeProperties(String title, int type, int modType, ProjectItemNode parent) {
        this.title = title;
        this.type = Types.NODE_PROJECT;
        this.parent = parent;
        this.modType = modType;
        projectName = title;
        projectType = type;
        path = parent + File.separator + title;
        switch (projectType) {
            case Types.PROJECT_AROMA:
                zipPath = parent.prop.zipPath + "/" + "aroma_" + modType + "/" + projectZipPathPrefix + title;
                break;
            case Types.PROJECT_CUSTOM:
                zipPath = parent.prop.zipPath + "/" + "custom_" + modType + "/" + projectZipPathPrefix + title;
                break;
            case Types.PROJECT_MOD:
                zipPath = parent.prop.zipPath + "/" + "mod_" + modType + "/" + projectZipPathPrefix + title;
                break;
        }

        androidVersion = Preferences.pp.IsFromLollipop ? "5.x+" : "4.x+";
    }

    public NodeProperties(String title, int type, ProjectNode parent) {
        this.title = title;
        this.type = Types.NODE_GROUP;
        this.parent = parent;
        groupType = type;
        groupName = title;
        path = parent.prop.path + File.separator + title;
        projectName = parent.prop.projectName;
        projectParent = parent;
        switch (type) {
            case Types.GROUP_SYSTEM:
                propFile = getProp("system");
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system";
                break;
            case Types.GROUP_SYSTEM_APK:
                propFile = getProp("system_app");
                extension = "apk";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/app";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_app";
                break;
            case Types.GROUP_SYSTEM_PRIV_APK:
                propFile = getProp("system_priv");
                extension = "apk";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/priv-app";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_priv_app";
                break;
            case Types.GROUP_SYSTEM_BIN:
                propFile = getProp("system_bin");
                owner = "0";
                group = "2000";
                perm = "0755";
                location = "/system/bin";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_bin";
                break;
            case Types.GROUP_SYSTEM_ETC:
                propFile = getProp("system_etc");
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/etc";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_etc";
                break;
            case Types.GROUP_SYSTEM_FRAMEWORK:
                propFile = getProp("system_framework");
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/framework";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_framework";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                propFile = getProp("system_media_alarms");
                extension = "audio";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/media/audio/alarms";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_media_alarms";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                propFile = getProp("system_media_notifications");
                extension = "audio";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/media/audio/notifications";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_media_notifications";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                propFile = getProp("system_media_ringtones");
                extension = "audio";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/media/audio/ringtones";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_media_ringtones";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                propFile = getProp("system_media_ui");
                extension = "audio";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/media/audio/ui";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_media_ui";
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                propFile = getProp("system_media");
                isSelectBox = true;
                extension = "zip";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/media";
                setPermissions(owner, group, perm, "bootanimation.zip");
                isBootAnimationGroup = true;
                originalGroupType = typePrefix + "system_media";
                break;
            case Types.GROUP_SYSTEM_FONTS:
                propFile = getProp("system_fonts");
                isSelectBox = true;
                extension = "ttf";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/system/fonts";
                setPermissions(owner, group, perm);
                originalGroupType = typePrefix + "system_fonts";
                break;
            case Types.GROUP_DATA_APP:
                propFile = getProp("data_app");
                extension = "apk";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/data/app";
                setPermissions(owner, group, perm, "base/apk");
                originalGroupType = typePrefix + "data_app";
                break;
            case Types.GROUP_DATA_LOCAL:
                propFile = getProp("data_local");
                isSelectBox = true;
                extension = "zip";
                owner = "0";
                group = "0";
                perm = "0644";
                location = "/data/local";
                setPermissions(owner, group, perm, "bootanimation.zip");
                isBootAnimationGroup = true;
                originalGroupType = typePrefix + "data_local";
                break;
            case Types.GROUP_CUSTOM:
                propFile = getProp("custom");
                isSelectBox = false;
                break;
            case Types.GROUP_MOD:
                //following properties not needed but added.
                propFile = getProp("other");
                isSelectBox = false;
                originalGroupType = typePrefix + "other";
                break;
            case Types.GROUP_AROMA_THEMES:
                Logs.write("adding themes");
                propFile = "themes.prop";
                isSelectBox = true;
                path = "META-INF/com/google/android/aroma/themes" + File.separator + title;
                extension = "themes";
                break;
            case Types.GROUP_DELETE_FILES:
                propFile = getProp("delete");
                isSelectBox = false;
                extension = "delete";
                originalGroupType = typePrefix + "delete";
                break;
            case Types.GROUP_SCRIPT:
                propFile = getProp("dpi");
                isSelectBox = true;
                extension = "sh";
                originalGroupType = typePrefix + "script";
                break;
        }
        zipPath = parent.prop.zipPath + "/" + originalGroupType + "/" + groupZipPathPrefix + title;
        Logs.write("group property ready");
    }

    public NodeProperties(String title, int type, GroupNode parent) {
        this.title = title;
        this.type = Types.NODE_SUBGROUP;
        this.parent = parent;
        originalParent = parent;
        path = parent.prop.path + File.separator + title;
        subGroupName = title;
        groupType = type;
        subGroupType = type;
        projectName = parent.prop.projectName;
        originalGroupType = parent.prop.originalGroupType;
        location = parent.prop.location;
        switch (type) {
            case Types.GROUP_SYSTEM_FONTS:
                extension = "ttf";
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                extension = "zip";
                isBootAnimationGroup = true;
                break;
            case Types.GROUP_DATA_LOCAL:
                extension = "zip";
                isBootAnimationGroup = true;
                break;
        }
        zipPath = parent.prop.zipPath + "/" + subGroupZipPathPrefix + title;
        permission = parent.prop.permission;
        setPermissions = parent.prop.setPermissions;
    }

    public FileNode Add(String fileName, String subGroupName, int subGroupType, String groupName, int groupType, String originalGroupType,
            ArrayList<String> folders, String projectName, int projectType, int modType) {
        NodeProperties np = new NodeProperties(projectName, projectType, modType, rootNode);
        ProjectNode pNode = (ProjectNode) rootNode.addChild(new ProjectNode(np), false);
        np = new NodeProperties(groupName, groupType, pNode);
        if (groupType == Types.GROUP_CUSTOM && !originalGroupType.equals("")) {
            np.location = Identify.getLocation(originalGroupType);
            String str = Identify.getPermissions(originalGroupType);
            String[] perms = str.split("-");
            if (perms.length > 2) {
                np.owner = perms[0];
                np.group = perms[1];
                np.perm = perms[2];
                np.setPermissions(np.owner, np.group, np.perm);
            } else {
                np.setPermissions = false;
            }
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

    public String getProp(String str) {
        return str + "_" + groupName.replaceAll(" ", "_") + "_" + parent.prop.title.replaceAll(" ", "_") + ".prop";
    }

    public void reloadOriginalStringType() {
        originalGroupType = typePrefix + "custom_" + locSeparator + location.replaceAll("/", "+") + "_" + permSeparator + owner + "-" + group + "-" + perm;
    }

    public void reloadZipPath() {
        zipPath = parent.prop.zipPath + "/" + originalGroupType + "/" + groupZipPathPrefix + title;
    }
    
    public void reloadZipPath(String newTitle) {
        zipPath = parent.prop.zipPath + "/" + originalGroupType + "/" + groupZipPathPrefix + newTitle;
    }

    public void updateFileZipPath() {
        fileZipPath = parent.prop.zipPath + "/" + title;
    }
    
    public void updateFileInstallLocation() {
        fileInstallLocation = parent.prop.location.replaceAll("\\\\", "/");
    }

    public void setPermissions() {
        folderPermission = defaultFolderPerm + "\"" + folderLocation + "\"";
        folderPermission = folderPermission.replaceAll("\\\\", "/");
        if (Preferences.pp.useUniversalBinary) {
            permission = owner + " " + group + " " + perm + " ";
        } else {
            permission = owner + ", " + group + ", " + perm + ", ";
        }
    }

    public void setPermissions(String parentPermission, String title) {
        filePermission = (parentPermission + "\"" + fileInstallLocation + "/" + title + "\"").replaceAll("\\\\", "/");
    }

    public void setPermissions(String o, String g, String p) {
        owner = o;
        group = g;
        perm = p;
        if (Preferences.pp.useUniversalBinary) {
            permission = owner + " " + group + " " + perm + " ";
        } else {
            permission = owner + ", " + group + ", " + perm + ", ";
        }
    }

    public void setPermissions(String o, String g, String p, String title) {
        owner = o;
        group = g;
        perm = p;
        if (Preferences.pp.useUniversalBinary) {
            permission = owner + " " + group + " " + perm + " ";
        } else {
            permission = owner + ", " + group + ", " + perm + ", ";
        }
        filePermission = (permission + "\"" + fileInstallLocation + "/" + title + "\"").replaceAll("\\\\", "/");
    }
}
