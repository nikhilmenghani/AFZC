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

    public static void init() {
        gappsList = new ArrayList<>();
        //gappsList.add("etc/g.prop");
        gappsList.add("system/etc/preferred-apps/google.xml");

        romList = new ArrayList<>();
        romList.add("system/build.prop");

        aromaList = new ArrayList<>();
        aromaList.add("customize/aroma/");
    }

    public static int scanCompleteZip(String path) throws IOException {
        fileName = getFileName(path);
        rz = new ReadZip(path);
        int projectType = -1;
        init();
        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
            String entry = e.nextElement().getName();
            if ((zipType = check(entry)) != -1 && projectType == -1) {
                projectType = zipType;
            }
        }
        return projectType;
    }

    public static int scanZip(String path) throws IOException {
        fileName = getFileName(path);
        rz = new ReadZip(path);
        init();
        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
            String entry = e.nextElement().getName();
            if ((zipType = check(entry)) != -1) {
                return zipType;
            }
        }
        return zipType;
    }
    
    public static int check(String name) {
        if (isRom(name)) {
            rom = true;
            return ProjectNode.PROJECT_ROM;
        } else if (rom == false && isGapps(name)) {
            gapps = true;
            return ProjectNode.PROJECT_GAPPS;
        } else if (rom == false && gapps == false && isAroma(name)) {
            aroma = true;
            return ProjectNode.PROJECT_AROMA;
        }
        return -1;
    }

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

    public static boolean hasSubGroup(String path) {
        //System.out.println("Has SubGroup");
        switch (getGroupType(path)) {
            case GroupNode.GROUP_SYSTEM_FONTS:
            case GroupNode.GROUP_SYSTEM_MEDIA:
            case GroupNode.GROUP_AROMA_KERNEL:
            case GroupNode.GROUP_DATA_LOCAL:
                return true;
            case GroupNode.GROUP_CUSTOM:
                if (path.startsWith("customize")) {
                    System.out.println("path before " + path);
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    //path = path.substring(path.indexOf("/", path.indexOf("/", path.indexOf("/") + 1) + 1) + 1, path.length());
                    if (path.contains("/")) {
                        System.out.println("Returning true for " + path);
                        return true;
                    } else {
                        System.out.println("Returning false for " + path);
                        return false;
                    }
                }
        }
        return false;
    }

    public static String getSubGroupName(String groupName, String path) {
        if (path.startsWith("customize")) {
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            //path = path.substring(path.indexOf("/", path.indexOf("/", path.indexOf("/") + 1) + 1) + 1, path.length());
            try {
                path = path.substring(0, path.indexOf("/"));
            } catch (StringIndexOutOfBoundsException er) {
                System.out.println("Group with custom file found..!! " + path);
            }
        }
        return path;
    }

    public static String getGroupName(String path) {
        String fullPath = path;
        if (path.startsWith("customize")) {
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(0, path.indexOf("/"));
            //path = path.substring(path.indexOf("/", path.indexOf("/") + 1) + 1, path.indexOf("/", path.indexOf("/", path.indexOf("/") + 1) + 1));
            return path;
        } else if (path.startsWith("system") || path.startsWith("data") || path.startsWith("preload/symlink/system/app/")) {
            path = path.substring(path.indexOf("/") + 1, path.length());
            if (path.contains("/")) {
                path = path.substring(0, path.indexOf("/"));
            }
            //this will check if folder has subdirectories. -> system/etc/xyz/
            if ((fullPath.substring(fullPath.indexOf("/", fullPath.indexOf(path) + 1) + 1, fullPath.length())).contains("/")) {
                path = (fullPath.substring(fullPath.indexOf("/", fullPath.indexOf(path) + 1) + 1, fullPath.length()));
            }
        }
        switch (path) {
            case "app":
                if (fullPath.startsWith("data/app") && fullPath.endsWith(".apk")) {
                    return "DataApps";
                } else if (fullPath.startsWith("system/app") && fullPath.endsWith(".apk")) {
                    return "SystemApps";
                }
            case "priv-app":
                return "PrivateApps";
            case "local":
                if (fullPath.startsWith("data/local")) {
                    return "BootAnimations";
                } else {
                    break;
                }
//            case "symlink":
//                return "PreloadApps";
            case "custom":
                return "custom";
            default:
                if (fullPath.startsWith("system/media")) {
                    fullPath = fullPath.substring(0, fullPath.lastIndexOf("/"));
                    switch (fullPath) {
                        case "system/media/audio/notifications":
                            return "NotificationsTones";
                        case "system/media/audio/ringtones":
                            return "Ringtones";
                        case "system/media/audio/alarms":
                            return "AlarmTones";
                        case "system/media/audio/ui":
                            return "UITones";
                        default:
                            return "Others";
                    }
                } else if (fullPath.startsWith("preload/symlink/system/app")) {
                    return "PreloadApps";
                } else {
                    return "Others";
                }
        }
        return path;
    }

    public static int getGroupType(String path) {
        String fullPath = path;
        if (path.startsWith("customize")) {
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(path.indexOf("/") + 1, path.length());
            path = path.substring(0, path.indexOf("/"));
            //path = path.substring(path.indexOf("/") + 1, path.indexOf("/", path.indexOf("/") + 1));
        } else if (path.startsWith("system") || path.startsWith("data") || path.startsWith("preload/symlink/system/app/")) {
            path = path.substring(path.indexOf("/") + 1, path.length());
            if (path.contains("/")) {
                path = path.substring(0, path.indexOf("/"));
            }
            //this will check if folder has subdirectories. -> system/etc/xyz/
            if ((fullPath.substring(fullPath.indexOf("/", fullPath.indexOf(path) + 1) + 1, fullPath.length())).contains("/")) {
                path = (fullPath.substring(fullPath.indexOf("/", fullPath.indexOf(path) + 1) + 1, fullPath.length()));// it will return xyz/
            }
        }

        switch (path) {
            case "system_app":
            case "app":
                if (fullPath.startsWith("data")) {
                    return GroupNode.GROUP_DATA_APP;
                } else {
                    return GroupNode.GROUP_SYSTEM_APK;
                }
            case "system_priv_app":
            case "priv-app":
                return GroupNode.GROUP_SYSTEM_PRIV_APK;
            case "system_csc":
                return GroupNode.GROUP_SYSTEM_CSC;
            case "script":
                return GroupNode.GROUP_SCRIPT;
            case "system_etc":
                return GroupNode.GROUP_SYSTEM_ETC;
            case "system_lib":
                return GroupNode.GROUP_SYSTEM_LIB;
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
            case "system_preload":
                return GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP;
            case "system_framework":
                return GroupNode.GROUP_SYSTEM_FRAMEWORK;
            case "system_fonts":
                return GroupNode.GROUP_SYSTEM_FONTS;
            case "system_media":
                return GroupNode.GROUP_SYSTEM_MEDIA;
            case "data_local":
                return GroupNode.GROUP_DATA_LOCAL;
            case "custom":
                return GroupNode.GROUP_CUSTOM;
            case "kernels":
                return GroupNode.GROUP_AROMA_KERNEL;
            case "Other":
                return GroupNode.GROUP_OTHER;
            default:
                if (fullPath.startsWith("system/media")) {
                    fullPath = fullPath.substring(0, fullPath.lastIndexOf("/"));
                    switch (fullPath) {
                        case "system/media":
                            return GroupNode.GROUP_OTHER;//files belonging to system/media but not bootanimation.zip
                        case "system/media/audio/notifications":
                            return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS;
                        case "system/media/audio/ringtones":
                            return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES;
                        case "system/media/audio/alarms":
                            return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS;
                        case "system/media/audio/ui":
                            return GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI;
                        default:
                            return GroupNode.GROUP_OTHER;// like system/media/video in stock touchwiz roms.
                    }
                } else if (fullPath.startsWith("preload/symlink/system/app")) {
                    return GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP;
                } else {
                    return GroupNode.GROUP_OTHER;
                }
        }
    }

    public static String getProjectName(String path) {
        if (path.startsWith("customize")) {
            return path.substring(path.indexOf("/", path.indexOf("/") + 1) + 1, path.indexOf("/", path.indexOf("/", path.indexOf("/") + 1) + 1));
        } else {
            return fileName;
        }
    }

    public static int getProjectType(String path) throws IOException {
        if (path.startsWith("customize")) {
            path = path.substring(path.indexOf("/") + 1, path.indexOf("/", path.indexOf("/") + 1));
        } else {
            return zipType;
        }
        switch (path) {
            case "rom":
                return ProjectNode.PROJECT_ROM;
            case "gapps":
                return ProjectNode.PROJECT_GAPPS;
            case "aroma":
                return ProjectNode.PROJECT_AROMA;
            case "normal":
                return ProjectNode.PROJECT_NORMAL;
        }
        return -1;
    }
}
