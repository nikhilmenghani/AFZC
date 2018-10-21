/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Protocols.Identify;
import flashablezipcreator.Protocols.Types;
import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class Package {

    public String installedPath = "";
    public String systemPath = "";
    public String fileName = "";
    public boolean isOptional = false;
    public int belongsToGroup = 0;
    public boolean pullFolder = false;
    public String packageName = "";
    boolean hasDataPath = false;
    public String updatedInstalledPath = "";
    public ArrayList<String> associatedFileList = new ArrayList<>();
    //customize/aroma_0/Project_My Project/Type_system_fonts/Group_Fonts/SubGroup_ComicSansMS/DroidSansFallback.ttf

    public String getZipPath(String installedPath, ProjectItemNode parent) {
        String filePath = "";
        String groupType = "";
        String groupName = "";
        String subGroupName = "";
        String projectName = "My Device";
        java.io.File f = new java.io.File(installedPath);
        String folderPath = f.getParent().replaceAll("\\\\", "/");
        String foldersPath = "";
        boolean isSupported = true;
        if (folderPath.equals("/system")) {
            groupType = "Type_" + "system";
        } else if (installedPath.startsWith("/system/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/app/".length())
                    ? installedPath.substring("/system/app/".length(), installedPath.lastIndexOf("/")) : "";
            //sometimes apk files are stored at root folder which is not the expected behavior in Android 5+ Roms
            //forcing the tool to create an additional folder
            foldersPath = (foldersPath.equals("") && installedPath.endsWith(".apk"))
                    ? f.getName().replaceFirst("[.][^.]+$", "") : foldersPath;
            groupType = "Type_" + "system_app";
            groupName = "System Apps";
        } else if (installedPath.startsWith("/system/priv-app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/priv-app/".length())
                    ? installedPath.substring("/system/priv-app/".length(), installedPath.lastIndexOf("/")) : "";
            //sometimes apk files are stored at root folder which is not the expected behavior in Android 5+ Roms
            //forcing the tool to create an additional folder
            foldersPath = (foldersPath.equals("") && installedPath.endsWith(".apk"))
                    ? f.getName().replaceFirst("[.][^.]+$", "") : foldersPath;
            groupType = "Type_" + "system_priv_app";
            groupName = "Priv Apps";
        } else if (installedPath.startsWith("/system/bin")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/bin/".length())
                    ? installedPath.substring("/system/bin/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_bin";
            groupName = "System Bin";
        } else if (installedPath.startsWith("/system/xbin")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/xbin/".length())
                    ? installedPath.substring("/system/xbin/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_xbin";
            groupName = "System xBin";
        } else if (installedPath.startsWith("/system/etc")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/etc/".length())
                    ? installedPath.substring("/system/etc/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_etc";
            groupName = "System Etc";
        } else if (installedPath.startsWith("/system/framework")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/framework/".length())
                    ? installedPath.substring("/system/framework/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_framework";
            groupName = "System Framework";
        } else if (installedPath.startsWith("/system/lib/")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/lib/".length())
                    ? installedPath.substring("/system/lib/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_lib";
            groupName = "System Lib";
        } else if (installedPath.startsWith("/system/lib64")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/lib64/".length())
                    ? installedPath.substring("/system/lib64/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "system_lib64";
            groupName = "System Lib64";
        } else if (folderPath.equals("/vendor")) {
            groupType = "Type_" + "vendor";
        } else if (installedPath.startsWith("/vendor/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/app/".length())
                    ? installedPath.substring("/vendor/app/".length(), installedPath.lastIndexOf("/")) : "";
            foldersPath = (foldersPath.equals("") && installedPath.endsWith(".apk"))
                    ? f.getName().replaceFirst("[.][^.]+$", "") : foldersPath;
            groupType = "Type_" + "vendor_app";
            groupName = "Vendor Apps";
        } else if (installedPath.startsWith("/vendor/bin")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/bin/".length())
                    ? installedPath.substring("/vendor/bin/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "vendor_bin";
            groupName = "Vendor Bin";
        } else if (installedPath.startsWith("/vendor/etc")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/etc/".length())
                    ? installedPath.substring("/vendor/etc/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "vendor_etc";
            groupName = "Vendor Etc";
        } else if (installedPath.startsWith("/vendor/framework")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/framework/".length())
                    ? installedPath.substring("/vendor/framework/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "vendor_framework";
            groupName = "Vendor Framework";
        } else if (installedPath.startsWith("/vendor/lib/")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/lib/".length())
                    ? installedPath.substring("/vendor/lib/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "vendor_lib";
            groupName = "Vendor Lib";
        } else if (installedPath.startsWith("/vendor/lib64")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/lib64/".length())
                    ? installedPath.substring("/vendor/lib64/".length(), installedPath.lastIndexOf("/")) : "";
            groupType = "Type_" + "vendor_lib64";
            groupName = "Vendor Lib64";
        } else if (installedPath.startsWith("/system/media/audio/alarms")) {
            groupType = "Type_" + "system_media_alarms";
        } else if (installedPath.startsWith("/system/media/audio/notifications")) {
            groupType = "Type_" + "system_media_notifications";
        } else if (installedPath.startsWith("/system/media/audio/ringtones")) {
            groupType = "Type_" + "system_media_ringtones";
        } else if (installedPath.startsWith("/system/media/audio/ui")) {
            groupType = "Type_" + "system_media_ui";
        } else if (installedPath.startsWith("/system/fonts")) {
            subGroupName = "System Fonts";
            groupType = "Type_" + "system_fonts";
        } else if (folderPath.equals("/system/media") && f.getName().equals("bootanimation.zip")) {
            subGroupName = "System BootAnimation";
            groupType = "Type_" + "system_media";
        } else if (installedPath.startsWith("/data/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/data/app/".length())
                    ? installedPath.substring("/data/app/".length(), installedPath.lastIndexOf("/"))
                    : f.getName().replaceFirst("[.][^.]+$", "");
            foldersPath = foldersPath.contains("-") ? foldersPath : foldersPath + "-1"; //this can/should be improved further.
            groupType = "Type_" + "data_app";
        } else if (folderPath.equals("/data/local")) {
            subGroupName = "System BootAnimation";
            groupType = "Type_" + "data_local";
        } else {
            //yet to be configured
            //Type_custom_Loc_+system+vendor_Perm_1000-1000-0644
            groupName = "Other Files";
            groupType = "Type_" + "custom";
            isSupported = false;
            filePath = "";//returning empty when not supported
        }
        if (isSupported) {
            switch (parent.prop.type) {
                case Types.NODE_PROJECT:
                    projectName = parent.prop.projectName;
                    break;
                case Types.NODE_GROUP:
                    projectName = parent.prop.projectName;
                    groupName = parent.prop.title;
                    break;
                case Types.NODE_SUBGROUP:
                    projectName = parent.prop.projectName;
                    groupName = parent.prop.groupName;
                    subGroupName = parent.prop.title;
                    break;
                case Types.NODE_FOLDER:
                    projectName = parent.prop.projectName;
                    groupName = parent.prop.groupName;
                    break;
            }
            subGroupName = (!subGroupName.equals("")) ? "SubGroup_" + subGroupName + "/" : "";
            groupName = "Group_" + groupName;
            filePath = "customize/aroma_0/Project_" + projectName + "/" + groupType + "/" + groupName + "/"
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

    public String[] getImportFilePath(String installedPath, ProjectItemNode parent) {
        String filePath[] = new String[]{"", ""};
        String zipPath = getZipPath(installedPath, parent);
        if (zipPath.equals("")) {
            return filePath;
        }
        String projectName = Identify.getProjectName(zipPath);
        String groupName = Identify.getGroupName(zipPath);
        int groupType = Identify.getGroupType(zipPath);
        String originalGroupType = "";
        if (groupType == Types.GROUP_CUSTOM) {
            try {
                originalGroupType = Identify.getOriginalGroupType(zipPath);
            } catch (Exception e) {
                originalGroupType = "";
            }
        }
        //ArrayList<String> folderList = Identify.getFolderNames(zipPath, Types.PROJECT_AROMA);
        String subGroupName = Identify.getSubGroupName(groupName, zipPath);
        switch (parent.prop.type) {
            case Types.NODE_PROJECT:
                projectName = parent.prop.title;
                break;
            case Types.NODE_GROUP:
                projectName = parent.prop.projectName;
                groupName = parent.prop.title;
                break;
            case Types.NODE_SUBGROUP:
                projectName = parent.prop.projectName;
                groupName = parent.prop.groupName;
                subGroupName = parent.prop.title;
                break;
            case Types.NODE_FOLDER:
                projectName = parent.prop.projectName;
                groupName = parent.prop.groupName;
                break;
        }
        java.io.File f = new java.io.File(installedPath);
        String folderPath = f.getParent().replaceAll("\\\\", "/");
        String foldersPath = "";
        filePath[0] = "AFZC Projects\\" + projectName + "\\";
        if (folderPath.equals("/system")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/app")) {
            foldersPath = installedPath.substring("/system/app/".length(), installedPath.length());
            foldersPath = foldersPath.contains("/") ? foldersPath : foldersPath.replaceFirst("[.][^.]+$", "") + "/" + foldersPath;
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/priv-app")) {
            foldersPath = installedPath.substring("/system/priv-app/".length(), installedPath.length());
            foldersPath = foldersPath.contains("/") ? foldersPath : foldersPath.replaceFirst("[.][^.]+$", "") + "/" + foldersPath;
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/bin")) {
            foldersPath = installedPath.substring("/system/bin/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/xbin")) {
            foldersPath = installedPath.substring("/system/xbin/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/etc")) {
            foldersPath = installedPath.substring("/system/etc/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/lib/")) {
            foldersPath = installedPath.substring("/system/lib/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/lib64")) {
            foldersPath = installedPath.substring("/system/lib64/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/framework")) {
            foldersPath = installedPath.substring("/system/framework/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (folderPath.equals("/vendor")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/vendor/app")) {
            foldersPath = installedPath.substring("/vendor/app/".length(), installedPath.length());
            foldersPath = foldersPath.contains("/") ? foldersPath : foldersPath.replaceFirst("[.][^.]+$", "") + "/" + foldersPath;
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/bin")) {
            foldersPath = installedPath.substring("/vendor/bin/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/etc")) {
            foldersPath = installedPath.substring("/vendor/etc/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/lib/")) {
            foldersPath = installedPath.substring("/vendor/lib/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/lib64")) {
            foldersPath = installedPath.substring("/vendor/lib64/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/framework")) {
            foldersPath = installedPath.substring("/vendor/framework/".length(), installedPath.length());
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/media/audio/alarms")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/notifications")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/ringtones")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/media/audio/ui")) {
            filePath[0] += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/system/fonts")) {
            filePath[0] += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else if (folderPath.equals("/system/media") && f.getName().equals("bootanimation.zip")) {
            filePath[0] += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/data/app")) {
            //make changes
            foldersPath = installedPath.substring("/data/app/".length(), installedPath.length());
            if (!foldersPath.contains("/")) {
                String fileName = f.getName();
                String folderName = fileName.contains("-") ? fileName.substring(0, fileName.indexOf("-")) : fileName.replaceFirst("[.][^.]+$", "");
                foldersPath = folderName + "-1/" + installedPath.substring("/data/app/".length(), installedPath.length());
            }
            filePath[0] += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (folderPath.equals("/data/local")) {
            filePath[0] += groupName + "\\" + subGroupName + "\\" + f.getName();
        } else {
            //Custom Files Not Supported so returning empty
            filePath[0] = "";
        }
        filePath[1] = zipPath;
        return filePath;
    }

    public String getDataPath() {
        return "/data/app/" + packageName;
    }
}
