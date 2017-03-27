/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preferences;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public final class GroupNode extends ProjectItemNode {

    public GroupNode(String title, int type, ProjectNode parent) {
        super(title, Types.NODE_GROUP, parent);
        prop.groupType = type;
        prop.groupName = title;
        prop.path = parent.prop.path + File.separator + title;
        prop.projectName = parent.prop.projectName;

        switch (type) {
            case Types.GROUP_SYSTEM_APK:
                prop.location = "/system/app";
                prop.propFile = getProp("system_app");
                prop.extension = "apk";
                prop.originalGroupType = prop.typePrefix + "system_app";
                setPermissions("0", "0", "0644");
                break;
            case Types.GROUP_SYSTEM_PRIV_APK:
                prop.location = "/system/priv-app";
                prop.propFile = getProp("system_priv");
                prop.extension = "apk";
                prop.originalGroupType = prop.typePrefix + "system_priv_app";
                setPermissions("0", "0", "0644");
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                prop.location = "/system/media/audio/alarms";
                prop.propFile = getProp("system_media_alarms");
                prop.extension = "audio";
                prop.originalGroupType = prop.typePrefix + "system_media_alarms";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                prop.location = "/system/media/audio/notifications";
                prop.propFile = getProp("system_media_notifications");
                prop.extension = "audio";
                prop.originalGroupType = prop.typePrefix + "system_media_notifications";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                prop.location = "/system/media/audio/ringtones";
                prop.propFile = getProp("system_media_ringtones");
                prop.extension = "audio";
                prop.originalGroupType = prop.typePrefix + "system_media_ringtones";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                prop.location = "/system/media/audio/ui";
                prop.propFile = getProp("system_media_ui");
                prop.extension = "audio";
                prop.originalGroupType = prop.typePrefix + "system_media_ui";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                prop.location = "/system/media";
                prop.propFile = getProp("system_media");
                prop.isSelectBox = true;
                prop.extension = "zip";
                prop.isBootAnimationGroup = true;
                prop.originalGroupType = prop.typePrefix + "system_media";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_SYSTEM_FONTS:
                prop.location = "/system/fonts";
                prop.propFile = getProp("system_fonts");
                prop.isSelectBox = true;
                prop.extension = "ttf";
                prop.originalGroupType = prop.typePrefix + "system_fonts";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_DATA_APP:
                prop.location = "/data/app";
                prop.propFile = getProp("data_app");
                prop.extension = "apk";
                prop.originalGroupType = prop.typePrefix + "data_app";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_DATA_LOCAL:
                prop.location = "/data/local";
                prop.propFile = getProp("data_local");
                prop.isSelectBox = true;
                prop.extension = "zip";
                prop.isBootAnimationGroup = true;
                prop.originalGroupType = prop.typePrefix + "data_local";
                setPermissions("1000", "1000", "0644");
                break;
            case Types.GROUP_CUSTOM:
//                prop.location = "/custom";
//                this.permissions = "";
                prop.propFile = getProp("custom");
                prop.isSelectBox = false;
                prop.originalGroupType = prop.typePrefix + "custom";
                break;
            case Types.GROUP_MOD:
//                prop.location = "";
//                this.permissions = "";
                //following properties not needed but added.
                prop.propFile = getProp("other");
                prop.isSelectBox = false;
                prop.originalGroupType = prop.typePrefix + "other";
                break;
            case Types.GROUP_AROMA_THEMES:
//                prop.location = "";
//                this.permissions = "";
                Logs.write("adding themes");
                prop.propFile = "themes.prop";
                prop.isSelectBox = true;
                prop.path = "META-INF/com/google/android/aroma/themes" + File.separator + title;
                prop.extension = "themes";
                break;
            case Types.GROUP_DELETE_FILES:
                prop.propFile = getProp("delete");
                prop.isSelectBox = false;
                prop.extension = "delete";
                prop.originalGroupType = prop.typePrefix + "delete";
                break;
            case Types.GROUP_SCRIPT:
                prop.propFile = getProp("dpi");
                prop.isSelectBox = true;
                prop.extension = "sh";
                prop.originalGroupType = prop.typePrefix + "script";
                break;
        }
        prop.zipPath = parent.prop.zipPath + "/" + prop.originalGroupType + "/" + prop.groupZipPathPrefix + title;
        Logs.write("group property ready");
    }

    public String setPermissions(String i, String j, String k) {
        if (Preferences.useUniversalBinary) {
            prop.groupPermission = i + " " + j + " " + k + " ";
        } else {
            prop.groupPermission = i + ", " + j + ", " + k + ", ";
        }
        prop.permission = prop.groupPermission;
        return prop.groupPermission;
    }

    public boolean isSelectBox() {
        return prop.isSelectBox;
    }

    public boolean isCheckBox() {
        return !prop.isSelectBox;
    }

    public void setLocation(String location) {
        prop.location = location;
    }

    public String getLocation() {
        return prop.location;
    }

    public void updateZipPath() {
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.originalGroupType + "/" + prop.groupZipPathPrefix + prop.title;
    }

    public void renameMe(String newName) throws IOException {
        prop.title = newName;
        prop.groupName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.originalGroupType + "/" + prop.groupZipPathPrefix + newName;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : prop.children) {
            switch (node.prop.type) {
                case Types.NODE_SUBGROUP:
                    ((SubGroupNode) node).updateZipPath();
                    ((SubGroupNode) node).updateChildrenZipPath();
                    break;
                case Types.NODE_FOLDER:
                    ((FolderNode) node).updateZipPath();
                    ((FolderNode) node).updateChildrenZipPath();
                    break;
                case Types.NODE_FILE:
                    ((FileNode) node).updateZipPath();
                    break;
            }
        }
    }

    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
    }

    public String getProp(String str) {
        return str + "_" + prop.groupName.replaceAll(" ", "_") + "_" + prop.parent.prop.title.replaceAll(" ", "_") + ".prop";
    }
}
