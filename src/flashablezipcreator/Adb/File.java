/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Protocols.Identify;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Mod;
import flashablezipcreator.Protocols.Types;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class File {

    public String installedPath = "";
    public String systemPath = "";
    public String fileName = "";
    public int belongsToGroup = 0;
    public boolean pullFolder = false;
    //customize/aroma_0/Project_My Project/Type_system_fonts/Group_Fonts/SubGroup_ComicSansMS/DroidSansFallback.ttf

    public String getZipPath(String installedPath) {
        String filePath = "";
        String groupType = "";
        String groupName = "";
        String subGroupName = "";
        java.io.File f = new java.io.File(installedPath);
        String folderPath = f.getParent().replaceAll("\\\\", "/");
        String foldersPath = "";
        boolean isSupported = true;
        if (folderPath.equals("/system")) {
            groupName = "Group_" + "System Files";
            groupType = "Type_" + "system";
        } else if (installedPath.startsWith("/system/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/app/".length())
                    ? installedPath.substring("/system/app/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Apps";
            groupType = "Type_" + "system_app";
        } else if (installedPath.startsWith("/system/priv-app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/priv-app/".length())
                    ? installedPath.substring("/system/priv-app/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Priv Apps";
            groupType = "Type_" + "system_priv_app";
        } else if (installedPath.startsWith("/system/bin")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/bin/".length())
                    ? installedPath.substring("/system/bin/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Bin";
            groupType = "Type_" + "system_bin";
        } else if (installedPath.startsWith("/system/etc")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/etc/".length())
                    ? installedPath.substring("/system/etc/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Etc";
            groupType = "Type_" + "system_etc";
        } else if (installedPath.startsWith("/system/framework")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/framework/".length())
                    ? installedPath.substring("/system/framework/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Framework";
            groupType = "Type_" + "system_framework";
        } else if (installedPath.startsWith("/system/media/audio/alarms")) {
            groupName = "Group_" + "Alarm Tones";
            groupType = "Type_" + "system_media_alarms";
        } else if (installedPath.startsWith("/system/media/audio/notifications")) {
            groupName = "Group_" + "Notifications";
            groupType = "Type_" + "system_media_notifications";
        } else if (installedPath.startsWith("/system/media/audio/ringtones")) {
            groupName = "Group_" + "Ringtones";
            groupType = "Type_" + "system_media_ringtones";
        } else if (installedPath.startsWith("/system/media/audio/ui")) {
            groupName = "Group_" + "UI Tones";
            groupType = "Type_" + "system_media_ui";
        } else if (installedPath.startsWith("/system/fonts")) {
            subGroupName = "SubGroup_" + "System Fonts";
            groupName = "Group_" + "Fonts";
            groupType = "Type_" + "system_fonts";
        } else if (folderPath.equals("/system/media") && f.getName().equals("bootanimation.zip")) {
            subGroupName = "SubGroup_" + "System BootAnimation";
            groupName = "Group_" + "Boot Animations";
            groupType = "Type_" + "system_media";
        } else if (installedPath.startsWith("/data/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/data/app/".length())
                    ? installedPath.substring("/data/app/".length(), installedPath.lastIndexOf("/"))
                    : f.getName().replaceFirst("[.][^.]+$", "");
            foldersPath = foldersPath.contains("-") ? foldersPath : foldersPath + "-1"; //this can/should be improved further.
            groupName = "Group_" + "Data Apps";
            groupType = "Type_" + "data_app";
        } else if (folderPath.equals("/data/local")) {
            subGroupName = "SubGroup_" + "System BootAnimation";
            groupName = "Group_" + "Boot Animations";
            groupType = "Type_" + "data_local";
        } else {
            //yet to be configured
            //Type_custom_Loc_+system+vendor_Perm_1000-1000-0644
            groupName = "Group_" + "Other Files";
            groupType = "Type_" + "custom";
            isSupported = false;
            filePath = "";//returning empty when not supported
        }
        if (isSupported) {
            subGroupName = (!subGroupName.equals("")) ? subGroupName + "/" : "";
            filePath = "customize/aroma_0/Project_My Device/" + groupType + "/" + groupName + "/"
                    + subGroupName;
            String folders = "";
            if (!foldersPath.equals("")) {
                for (String str : foldersPath.split("/")) {
                    folders += "Folder_" + str + "/";
                }
            }
            filePath += folders.equals("") ? "" : folders;
            filePath += f.getName();
        }
        return filePath;
    }

    public String getImportFilePath(String installedPath) {
        String filePath = "AFZC Projects\\My Device\\";
        String groupName = "";
        String subGroupName = "";
        java.io.File f = new java.io.File(installedPath);
        String folderPath = f.getParent().replaceAll("\\\\", "/");
        String foldersPath = "";
        if (folderPath.equals("/system")) {
            groupName = "System Files";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/app")) {
            foldersPath = installedPath.substring("/system/app/".length(), installedPath.length());
            groupName = "System Apps";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/priv-app")) {
            foldersPath = installedPath.substring("/system/priv-app/".length(), installedPath.length());
            groupName = "Priv Apps";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/bin")) {
            foldersPath = installedPath.substring("/system/bin/".length(), installedPath.length());
            groupName = "System Bin";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/etc")) {
            foldersPath = installedPath.substring("/system/etc/".length(), installedPath.length());
            groupName = "System Etc";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/framework")) {
            foldersPath = installedPath.substring("/system/framework/".length(), installedPath.length());
            groupName = "System Framework";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/media/audio/alarms")) {
            groupName = "Alarm Tones";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/notifications")) {
            groupName = "Notifications";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/ringtones")) {
            groupName = "Ringtones";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/ui")) {
            groupName = "UI Tones";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/fonts")) {
            subGroupName = "System Fonts";
            groupName = "Fonts";
            filePath += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else if (folderPath.equals("/system/media") && f.getName().equals("bootanimation.zip")) {
            subGroupName = "System BootAnimation";
            groupName = "Boot Animations";
            filePath += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/data/app")) {
            //make changes
            foldersPath = installedPath.substring("/data/app/".length(), installedPath.length());
            if (!foldersPath.contains("/")) {
                String fName = f.getName();
                String folderName = fName.contains("-") ? fName.substring(0, fName.indexOf("-")) : fName.replaceFirst("[.][^.]+$", "");
                foldersPath = folderName + "-1/" + installedPath.substring("/data/app/".length(), installedPath.length());
            }
            groupName = "Data Apps";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (folderPath.equals("/data/local")) {
            subGroupName = "System BootAnimation";
            groupName = "Boot Animations";
            filePath += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else {
            //Custom Files Not Supported so returning empty
            filePath = "";
        }
        return filePath;
    }

    public String importFilePath(String installedPath) throws IOException {
        String filePath = getZipPath(installedPath);
        String path = "";
        if (!filePath.equals("")) {
            String projectName = Identify.getProjectName(filePath);
            String groupName = Identify.getGroupName(filePath);
            int groupType = Identify.getGroupType(filePath);
            String originalGroupType = "";
            if (groupType == Types.GROUP_CUSTOM) {
                try {
                    originalGroupType = Identify.getOriginalGroupType(filePath);
                } catch (Exception e) {
                    originalGroupType = "";
                }
            }
            ArrayList<String> folderList = Identify.getFolderNames(filePath, Types.PROJECT_AROMA);
            String subGroupName = Identify.getSubGroupName(groupName, filePath);
            int subGroupType = groupType; //Groups that have subGroups have same type.
            String fName = (new java.io.File(filePath)).getName();
            NodeProperties np = new NodeProperties();
            FileNode file = np.Add(fName, subGroupName, subGroupType, groupName, groupType, originalGroupType, folderList, projectName, Types.PROJECT_AROMA, Mod.MOD_LESS);
            file.prop.fileSourcePath = file.prop.path;
            systemPath = file.prop.fileSourcePath;
            Logs.write("Written File: " + fName);
        }
        return path;
    }
}
