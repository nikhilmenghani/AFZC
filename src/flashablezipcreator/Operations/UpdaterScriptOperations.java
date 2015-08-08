/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Project;
import static flashablezipcreator.Protocols.UpdaterScript.symlinkScriptPath;

/**
 *
 * @author Nikhil
 */
public class UpdaterScriptOperations {

    public static final int installString = 1;
    public static final int deleteString = 2;
    public static final int normalString = 3;

    public String addPrintString(String str, int type) {
        switch (type) {
            case installString:
                return "ui_print(\"@Installing " + str + "\");\n";
            case deleteString:
                return "ui_print(\"@Deleting " + str + "\");\n";
        }
        return "ui_print(\"" + str + "\");\n";
    }

    public String addPrintString(String str) {
        return "ui_print(\"" + str + "\");\n";
    }

    public String initiateUpdaterScript() {
        return addPrintString("@Starting the install process")
                + addPrintString("Setting up required tools...");
    }

    public String terminateUpdaterScript() {
        return addPrintString("Unmounting Partitions...")
                + "unmount(\"/data\");\n"
                + "unmount(\"/system\");\n";
    }

    public String addWipeDalvikCacheString() {
        String str = "";
        str += "run_program(\"/sbin/busybox\",\"mount\", \"/data\");\n";
        str += "if(file_getprop(\"/tmp/aroma/dalvik_choices.prop\", \"true\")==\"yes\") then\n"
                + "ui_print(\"@Wiping dalvik-cache\");\n"
                + "delete_recursive(\"/data/dalvik-cache\");\n"
                + "endif;\n";
        str += "unmount(\"/data\");\n";
        return str;
    }

    public String getMountMethod(int type) {
        switch (type) {
            case 1:
                return addPrintString("@Mounting Partitions...")
                        + "run_program(\"/sbin/busybox\",\"mount\", \"/system\");\n"
                        + "run_program(\"/sbin/busybox\",\"mount\", \"/data\");\n";
            case 2:
                //future aspect
                break;
        }
        return "";
    }

    public String predefinedGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"item.1." + count++ + "\")==\"1\") then \n";
            for (ProjectItemNode file : node.children) {
                if (node.groupType == GroupNode.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).title, deleteString);
                    str += "delete(\"/" + ((FileNode) file).getDeleteLocation() + "\");\n";
                } else {
                    str += addPrintString(((FileNode) file).title, installString);
                    str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                    str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                }
            }
            str += "endif;\n";
            for (ProjectItemNode file : node.children) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                if (node.groupType == GroupNode.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).title, deleteString);
                    str += "delete(\"/" + ((FileNode) file).getDeleteLocation() + "\");\n";
                } else {
                    str += addPrintString(((FileNode) file).title, installString);
                    str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                    str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                }
                str += "endif;\n";
            }
        } else if (node.isSelectBox() && node.groupType == GroupNode.GROUP_SCRIPT) {
            int count = 2;
            for (ProjectItemNode file : node.children) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                str += addPrintString("@Running script : " + ((FileNode) file).title);
                str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"/tmp/script\");\n"
                        + "set_perm(0, 0, 0777, \"/tmp/script\");\n"
                        + "run_program(\"/tmp/script\");\n"
                        + "delete(\"/tmp/script\");\n";
                str += "endif;\n";
            }
        } else {
            System.out.println("This Group not support");
        }
        return str;
    }

    public String deleteTempFiles() {
        String str = "";
        for (String path : Project.getTempFilesList()) {
            str += "delete(\"/" + path + "\");\n";
        }
        return str;
    }

    public String predefinedSubGroupsScript(GroupNode node) {
        String str = "";
        if (node.isSelectBox()) {
            int count = 2;
            for (ProjectItemNode subGroup : node.children) {
                if (node.groupType == GroupNode.GROUP_SYSTEM_FONTS) {
                    str += "if (file_getprop(\"/tmp/aroma/" + node.prop.replace(".prop", "_temp.prop") + "\", \"" + subGroup.toString() + "\")==\"" + "yes" + "\") then \n";
                } else {
                    str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                }
                str += addPrintString(((SubGroupNode) subGroup).title, installString);
                for (ProjectItemNode file : subGroup.children) {
                    switch (node.groupType) {
                        case GroupNode.GROUP_SYSTEM_FONTS:
                            str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                            str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                            break;
                        case GroupNode.GROUP_DATA_LOCAL:
                        case GroupNode.GROUP_SYSTEM_MEDIA:
                            //this will rename any zip package to bootamination.zip allowing users to add bootanimation.zip with custom names.
                            str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + "bootanimation.zip" + "\");\n";
                            str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                            break;
                        case GroupNode.GROUP_AROMA_KERNEL:
                            str += "assert(package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + Device.getMountPoint() + "\"));\n";
                            break;
                    }
                }
                str += "endif;\n";
            }
        }
        return str;
    }

    public String customGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"item.1." + count++ + "\")==\"1\") then \n";
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                            str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                        }
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).getZipPath() + "\", \"" + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\");\n";
                        str += "set_perm(" + ((FileNode) tempNode).filePermission + ");\n";
                }
            }
            str += "endif;\n";
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                            str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                        }
                        str += "endif;\n";
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).getZipPath() + "\", \"" + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\");\n";
                        str += "set_perm(" + ((FileNode) tempNode).filePermission + ");\n";
                        str += "endif;\n";
                }
            }
        } else if (node.isSelectBox()) {
            int count = 2;
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file(\"" + ((FileNode) file).getZipPath() + "\", \"" + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\");\n";
                            str += "set_perm(" + ((FileNode) file).filePermission + ");\n";
                        }
                        str += "endif;\n";
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).getZipPath() + "\", \"" + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\");\n";
                        str += "set_perm(" + ((FileNode) tempNode).filePermission + ");\n";
                        str += "endif;\n";
                }
            }
            str += "endif;\n";
        }
        return str;
    }

    public String generateUpdaterScript(GroupNode node) {
        switch (node.groupType) {
            //Group of predefined locations
            case GroupNode.GROUP_SYSTEM_APK:
            case GroupNode.GROUP_SYSTEM_PRIV_APK:
            case GroupNode.GROUP_SYSTEM_CSC:
            case GroupNode.GROUP_SYSTEM_ETC:
            case GroupNode.GROUP_SYSTEM_LIB:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case GroupNode.GROUP_DATA_APP:
            case GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP:
            case GroupNode.GROUP_SYSTEM_FRAMEWORK:
            case GroupNode.GROUP_DELETE_FILES:
            case GroupNode.GROUP_SCRIPT:
                return predefinedGroupScript(node);
            //Group of predefined locations that need subgroups
            case GroupNode.GROUP_SYSTEM_FONTS:
            case GroupNode.GROUP_DATA_LOCAL:
            case GroupNode.GROUP_SYSTEM_MEDIA:
            case GroupNode.GROUP_AROMA_KERNEL:
                return predefinedSubGroupsScript(node);
            //Group of custom location.
            case GroupNode.GROUP_CUSTOM:
                return customGroupScript(node);
        }
        return "";
    }

    public String setProgress(int progress) {
        return "\nset_progress(" + progress + ");\n";
    }

    public String getSymlinkScript() {
        return "#!/system/bin/mksh\n"
                + "\n"
                + "mount -o remount rw /system\n"
                + "cd /preload/symlink/system/app\n"
                + "\n"
                + "# Can't create array with /sbin/sh, hence we use mksh\n"
                + "apk_list=( `ls | grep .apk` )\n"
                + "odex_list=( `ls | grep .odex` )\n"
                + "items=${apk_list[*]}\" \"${odex_list[*]}\n"
                + "\n"
                + "for item in ${items[@]}\n"
                + "do\n"
                + "  ln -s /preload/symlink/system/app/$item /system/app/$item \n"
                + "done";
    }

    public String getDpiScript(String dpi) {
        return "#!/sbin/sh\n"
                + "sed -i '/ro.sf.lcd_density=/s/240/" + dpi + "/g' /system/build.prop";
    }

    public String getExtractDataString() {
        return "package_extract_dir(\"data\", \"/data\");\n"
                + "set_perm(2000, 2000, 0771, \"/data/local\");\n"
                + "set_perm_recursive(1000, 1000, 0771, 0644, \"/data/app\");\n";
    }
}
