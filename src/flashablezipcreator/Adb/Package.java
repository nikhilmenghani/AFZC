/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Adb;

import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class Package {

    public String installedPath = "";
    public String systemPath = "";
    public String fileName = "";
    public int belongsToGroup = 0;
    public boolean pullFolder = false;
    public String packageName = "";
    boolean hasDataPath = false;
    public String updatedInstalledPath = "";
    public ArrayList<String> associatedFileList = new ArrayList<>();
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
        } else if (installedPath.startsWith("/system/lib/")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/lib/".length())
                    ? installedPath.substring("/system/lib/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Lib";
            groupType = "Type_" + "system_lib";
        } else if (installedPath.startsWith("/system/lib64")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/system/lib64/".length())
                    ? installedPath.substring("/system/lib64/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "System Lib64";
            groupType = "Type_" + "system_lib64";
        } else if (folderPath.equals("/vendor")) {
            groupName = "Group_" + "Vendor Files";
            groupType = "Type_" + "vendor";
        } else if (installedPath.startsWith("/vendor/app")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/app/".length())
                    ? installedPath.substring("/vendor/app/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Apps";
            groupType = "Type_" + "vendor_app";
        } else if (installedPath.startsWith("/vendor/bin")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/bin/".length())
                    ? installedPath.substring("/vendor/bin/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Bin";
            groupType = "Type_" + "vendor_bin";
        } else if (installedPath.startsWith("/vendor/etc")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/etc/".length())
                    ? installedPath.substring("/vendor/etc/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Etc";
            groupType = "Type_" + "vendor_etc";
        } else if (installedPath.startsWith("/vendor/framework")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/framework/".length())
                    ? installedPath.substring("/vendor/framework/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Framework";
            groupType = "Type_" + "vendor_framework";
        } else if (installedPath.startsWith("/vendor/lib/")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/lib/".length())
                    ? installedPath.substring("/vendor/lib/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Lib";
            groupType = "Type_" + "vendor_lib";
        } else if (installedPath.startsWith("/vendor/lib64")) {
            foldersPath = (installedPath.lastIndexOf("/") > "/vendor/lib64/".length())
                    ? installedPath.substring("/vendor/lib64/".length(), installedPath.lastIndexOf("/")) : "";
            groupName = "Group_" + "Vendor Lib64";
            groupType = "Type_" + "vendor_lib64";
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
        } else if (installedPath.startsWith("/system/lib/")) {
            foldersPath = installedPath.substring("/system/lib/".length(), installedPath.length());
            groupName = "System Lib";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/lib64")) {
            foldersPath = installedPath.substring("/system/lib64/".length(), installedPath.length());
            groupName = "System Lib64";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/system/framework")) {
            foldersPath = installedPath.substring("/system/framework/".length(), installedPath.length());
            groupName = "System Framework";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (folderPath.equals("/vendor")) {
            groupName = "Vendor Files";
            filePath += groupName + "\\" + f.getName();
        } else if (installedPath.startsWith("/vendor/app")) {
            foldersPath = installedPath.substring("/vendor/app/".length(), installedPath.length());
            groupName = "Vendor Apps";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/bin")) {
            foldersPath = installedPath.substring("/vendor/bin/".length(), installedPath.length());
            groupName = "Vendor Bin";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/etc")) {
            foldersPath = installedPath.substring("/vendor/etc/".length(), installedPath.length());
            groupName = "Vendor Etc";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/lib/")) {
            foldersPath = installedPath.substring("/vendor/lib/".length(), installedPath.length());
            groupName = "Vendor Lib";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/lib64")) {
            foldersPath = installedPath.substring("/vendor/lib64/".length(), installedPath.length());
            groupName = "Vendor Lib64";
            filePath += groupName + "\\" + foldersPath.replaceAll("/", "\\\\");
        } else if (installedPath.startsWith("/vendor/framework")) {
            foldersPath = installedPath.substring("/vendor/framework/".length(), installedPath.length());
            groupName = "Vendor Framework";
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

    public String getDataPath() {
        return "/data/app/" + packageName;
    }
}
