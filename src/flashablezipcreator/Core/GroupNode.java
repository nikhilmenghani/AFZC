/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import static flashablezipcreator.AFZC.Protocols.p;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class GroupNode extends ProjectItemNode {

    public String groupName;
    public String permission = "";
    public int groupType;
    public String prop;
    public boolean isSelectBox = false;
    public String extension = "";
    public boolean isBootAnimationGroup = false;
    public String projectName;
    public String originalGroupType;
    String zipPathPrefix = "Group_";
    String typePrefix = "Type_";

    public static final int GROUP_SYSTEM_APK = 1;
    public static final int GROUP_SYSTEM_PRIV_APK = 2;
    public static final int GROUP_SYSTEM_MEDIA_AUDIO_ALARMS = 7;
    public static final int GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS = 8;
    public static final int GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES = 9;
    public static final int GROUP_SYSTEM_MEDIA_AUDIO_UI = 10;
    public static final int GROUP_SYSTEM_MEDIA = 11;
    public static final int GROUP_SYSTEM_FONTS = 12;
    public static final int GROUP_DATA_APP = 14;
    public static final int GROUP_DATA_LOCAL = 15;
    public static final int GROUP_CUSTOM = 16;
    public static final int GROUP_OTHER = 17;
    public static final int GROUP_AROMA_THEMES = 18;
    public static final int GROUP_DELETE_FILES = 20;
    public static final int GROUP_SCRIPT = 21;

    public GroupNode(String title, int type, ProjectNode parent) {
        super(title, ProjectItemNode.NODE_GROUP, parent);
        this.groupType = type;
        this.groupName = title;
        super.path = parent.path + File.separator + title;
        this.projectName = parent.projectName;
        //System.out.println("Group Path is : " + path);

        switch (type) {
            case GROUP_SYSTEM_APK:
                super.location = "/system/app";
                this.prop = getProp("system_app");
                this.extension = "apk";
                this.originalGroupType = this.typePrefix + "system_app";
                setPermissions("0", "0", "0644");
                break;
            case GROUP_SYSTEM_PRIV_APK:
                super.location = "/system/priv-app";
                this.prop = getProp("system_priv");
                this.extension = "apk";
                this.originalGroupType = this.typePrefix + "system_priv_app";
                setPermissions("0", "0", "0644");
                break;
            case GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                super.location = "/system/media/audio/alarms";
                this.prop = getProp("system_media_alarms");
                this.extension = "audio";
                this.originalGroupType = this.typePrefix + "system_media_alarms";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                super.location = "/system/media/audio/notifications";
                this.prop = getProp("system_media_notifications");
                this.extension = "audio";
                this.originalGroupType = this.typePrefix + "system_media_notifications";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                super.location = "/system/media/audio/ringtones";
                this.prop = getProp("system_media_ringtones");
                this.extension = "audio";
                this.originalGroupType = this.typePrefix + "system_media_ringtones";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_SYSTEM_MEDIA_AUDIO_UI:
                super.location = "/system/media/audio/ui";
                this.prop = getProp("system_media_ui");
                this.extension = "audio";
                this.originalGroupType = this.typePrefix + "system_media_ui";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_SYSTEM_MEDIA:
                super.location = "/system/media";
                this.prop = getProp("system_media");
                this.isSelectBox = true;
                this.extension = "zip";
                this.isBootAnimationGroup = true;
                this.originalGroupType = this.typePrefix + "system_media";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_SYSTEM_FONTS:
                super.location = "/system/fonts";
                this.prop = getProp("system_fonts");
                this.isSelectBox = true;
                this.extension = "ttf";
                this.originalGroupType = this.typePrefix + "system_fonts";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_DATA_APP:
                super.location = "/data/app";
                this.prop = getProp("data_app");
                this.extension = "apk";
                this.originalGroupType = this.typePrefix + "data_app";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_DATA_LOCAL:
                super.location = "/data/local";
                this.prop = getProp("data_local");
                this.isSelectBox = true;
                this.extension = "zip";
                this.isBootAnimationGroup = true;
                this.originalGroupType = this.typePrefix + "data_local";
                setPermissions("1000", "1000", "0644");
                break;
            case GROUP_CUSTOM:
//                super.location = "/custom";
//                this.permissions = "";
                this.prop = getProp("custom");
                this.isSelectBox = false;
                this.originalGroupType = this.typePrefix + "custom";
                break;
            case GROUP_OTHER:
//                super.location = "";
//                this.permissions = "";
                //following properties not needed but added.
                this.prop = getProp("other");
                this.isSelectBox = false;
                this.originalGroupType = this.typePrefix + "other";
                break;
            case GROUP_AROMA_THEMES:
//                super.location = "";
//                this.permissions = "";
                this.prop = "themes.prop";
                this.isSelectBox = true;
                super.path = "META-INF/com/google/android/aroma/themes" + File.separator + title;
                this.extension = "themes";
                break;
            case GROUP_DELETE_FILES:
                this.prop = getProp("delete");
                this.isSelectBox = false;
                this.extension = "delete";
                this.originalGroupType = this.typePrefix + "delete";
                break;
            case GROUP_SCRIPT:
                this.prop = getProp("dpi");
                this.isSelectBox = true;
                this.extension = "sh";
                this.originalGroupType = this.typePrefix + "script";
                break;
        }
        super.zipPath = parent.zipPath + "/" + this.originalGroupType + "/" + zipPathPrefix + title;
    }

    public String setPermissions(String i, String j, String k) {
        this.permission = i + ", " + j + ", " + k + ", ";
        return this.permission;
    }

    public boolean isSelectBox() {
        return isSelectBox;
    }

    public boolean isCheckBox() {
        return !isSelectBox;
    }

    public void setLocation(String location) {
        super.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void updateZipPath() {
        super.zipPath = parent.zipPath + "/" + this.originalGroupType + "/" + zipPathPrefix + title;
    }

    public void renameMe(String newName) throws IOException {
        super.setTitle(newName);
        this.groupName = newName;
        super.path = parent.path + File.separator + newName;
        super.zipPath = parent.zipPath + "/" + this.originalGroupType + "/" + zipPathPrefix + newName;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : children) {
            switch (node.type) {
                case ProjectItemNode.NODE_SUBGROUP:
                    ((SubGroupNode) node).updateZipPath();
                    ((SubGroupNode) node).updateChildrenZipPath();
                    break;
                case ProjectItemNode.NODE_FOLDER:
                    ((FolderNode) node).updateZipPath();
                    ((FolderNode) node).updateChildrenZipPath();
                    break;
                case ProjectItemNode.NODE_FILE:
                    ((FileNode) node).updateZipPath();
                    break;
            }
        }
    }

    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
    }
//
//    public String getPermissions() {
//        return permissions;
//    }

    public String getProp(String str) {
        return str + "_" + groupName.replaceAll(" ", "_") + "_" + parent.title.replaceAll(" ", "_") + ".prop";
    }
//
//    public void setPermissions(int i, int j, int k, String path) {
//        this.permissions = i + ", " + j + ", " + k + ", " + path;
//    }

}
