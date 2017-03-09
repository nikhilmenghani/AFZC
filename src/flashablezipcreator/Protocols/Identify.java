/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.DiskOperations.ReadZip;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Identify {

    static ReadZip rz;
    static int zipType = -1;
    static boolean rom = false;
    static boolean gapps = false;
    static boolean aroma = false;
    static boolean advanced = false;
    static boolean normal = false;
    static boolean other = false;
    static String fileName = "";
    static ArrayList<String> gappsList = new ArrayList<>();
    static ArrayList<String> romList = new ArrayList<>();
    static ArrayList<String> aromaList = new ArrayList<>();
    static String projectSeparator = "Project_";
    static String groupSeparator = "Group_";
    static String subGroupSeparator = "SubGroup_";
    static String folderSeparator = "Folder_";
    static String typeSeparator = "Type_";

    public static void init() {
        rom = false;
        gapps = false;
        aroma = false;
        advanced = false;
        normal = false;
        other = false;
        gappsList = new ArrayList<>();
        //gappsList.add("etc/g.prop");
        gappsList.add("system/etc/preferred-apps/google.xml");
        romList = new ArrayList<>();
        romList.add("system/build.prop");
        aromaList = new ArrayList<>();
        aromaList.add("customize/aroma/");
    }

//    public static int scanCompleteZip(String path) throws IOException {
//        fileName = getFileName(path);
//        rz = new ReadZip(path);
//        int projectType = -1;
//        init();
//        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
//            String entry = e.nextElement().getName();
//            if ((zipType = check(entry)) != -1 && projectType == -1) {
//                projectType = zipType;
//            }
//        }
//        return projectType;
//    }
//    public static int scanZip(String path) throws IOException {
//        fileName = getFileName(path);
//        rz = new ReadZip(path);
//        init();
//        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
//            String entry = e.nextElement().getName();
//            if ((zipType = check(entry)) != -1) {
//                return zipType;
//            }
//        }
//        JOptionPane.showMessageDialog(null, zipType + ".");
//        return zipType;
//    }
//    public static int check(String name) {
//        if (isRom(name)) {
//            rom = true;
//            return ProjectNode.PROJECT_ROM;
//        } else if (rom == false && isGapps(name)) {
//            gapps = true;
//            return ProjectNode.PROJECT_GAPPS;
//        } else if (rom == false && gapps == false && isAroma(name)) {
//            aroma = true;
//            return ProjectNode.PROJECT_AROMA;
//        }
//        return -1;
//    }
    public static boolean isRom(String name) {
        for (String list : romList) {
            if (list.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGapps(String name) {
        for (String list : gappsList) {
            if (list.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAroma(String name) {
        for (String list : aromaList) {
            if (name.startsWith(list)) {
                return true;
            }
        }
        return false;
    }

    //this removes the extension and returns file name.
    public static String getFileName(String path) {
        String fileName = (new File(path)).getName();
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        return fileName;
    }

    public static boolean hasFolderGroup(String path) {
        if (path.contains(folderSeparator)) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> getFolderNames(String path, int projectType) {
        ArrayList<String> fList = new ArrayList<>();
        switch (projectType) {
            case ProjectNode.PROJECT_AROMA:
            case ProjectNode.PROJECT_CUSTOM:
                while (path.contains(folderSeparator)) {
                    path = path.substring(path.indexOf(folderSeparator) + folderSeparator.length(), path.length());
                    String folderName = path.substring(0, path.indexOf("/"));
                    fList.add(folderName);
                    path = path.substring(path.indexOf("/") + 1, path.length());
                }
                return fList;
            case ProjectNode.PROJECT_MOD:
                while(path.contains("/")){
                    String folderName = path.substring(0, path.indexOf("/"));
                    fList.add(folderName);
                    path = path.substring(path.indexOf("/") + 1, path.length());
                }
                return fList;
        }
        return fList;
    }

    public static boolean hasSubGroup(String path) {
        if (path.contains(subGroupSeparator)) {
            return true;
        }
        return false;
    }

    public static String getSubGroupName(String groupName, String path) {
        String str = "";
        if (path.contains(subGroupSeparator)) {
            str = path.substring(path.indexOf(subGroupSeparator) + subGroupSeparator.length(), path.length());
            str = str.substring(0, str.indexOf("/"));
        }
        return str;
    }

    public static String getGroupName(String path) {
        String str = path.substring(path.indexOf(groupSeparator) + groupSeparator.length(), path.length());
        return str.substring(0, str.indexOf("/"));
    }

    public static int getGroupType(String path) {
        String str = path.substring(path.indexOf(typeSeparator) + typeSeparator.length(), path.length());
        str = str.substring(0, str.indexOf("/"));
        switch (str) {
            case "system_app":
                return GroupNode.GROUP_SYSTEM_APK;
            case "system_priv_app":
                return GroupNode.GROUP_SYSTEM_PRIV_APK;
            case "script":
                return GroupNode.GROUP_SCRIPT;
            case "system_media_alarms":
                return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS;
            case "system_media_notifications":
                return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS;
            case "system_media_ringtones":
                return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES;
            case "system_media_ui":
                return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI;
            case "data_app":
                return GroupNode.GROUP_DATA_APP;
            case "system_fonts":
                return GroupNode.GROUP_SYSTEM_FONTS;
            case "system_media":
                return GroupNode.GROUP_SYSTEM_MEDIA;
            case "data_local":
                return GroupNode.GROUP_DATA_LOCAL;
            case "custom":
                return GroupNode.GROUP_CUSTOM;
            case "mod":
                return GroupNode.GROUP_MOD;
            default:
                return GroupNode.GROUP_CUSTOM;
        }
    }

    public static String getProjectName(String path) {
        if (path.startsWith("customize")) {
            String str = path.substring(path.indexOf(projectSeparator) + projectSeparator.length(), path.length());
            return str.substring(0, str.indexOf("/"));
        } else {
            return fileName;
        }
    }

    public static int getProjectType(String path) throws IOException {
        if (path.startsWith("customize")) {
            //path = path.substring(path.indexOf("/") + 1, path.indexOf("/", path.indexOf("/") + 1));
            path = path.substring(path.indexOf("/") + 1, path.indexOf("_"));
        }
        switch (path) {
            case "aroma":
                return ProjectNode.PROJECT_AROMA;
            case "custom":
                return ProjectNode.PROJECT_CUSTOM;
            case "mod":
                return ProjectNode.PROJECT_MOD;
        }
        return ProjectNode.PROJECT_MOD;//This might get changed in future and set to Custom
    }
}
