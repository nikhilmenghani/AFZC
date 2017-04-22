/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
    THIS CLASS IS DEPRECATED DUE TO INTRODUCTION OF UNIVERSAL-UPDATE-BINARY
    If you want to modify script operations, then head over to UpdateBinaryOperations class,
    which is essentially a copy of this class with additional modifications to make it emit shell script rather than Edify
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.SubGroupNode;
import static flashablezipcreator.Operations.UpdateBinaryOperations.copyString;
import flashablezipcreator.UserInterface.Preferences;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Types;

/**
 *
 * @author Nikhil
 */
public class UpdaterScriptOperations {

    public static final int installString = 1;
    public static final int deleteString = 2;
    public static final int normalString = 3;
    public static final int copyString = 4;

    public String addPrintString(String str, int type) {
        switch (type) {
            case installString:
                return "ui_print(\"@Installing " + str + "\");\n";
            case deleteString:
                return "ui_print(\"@Deleting " + str + "\");\n";
            case copyString:
                return "ui_print(\"@Copying " + str + "\");\n";
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
        //str += "run_program(\"/sbin/busybox\",\"mount\", \"/data\");\n";
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
                        + "run_program(\"/sbin/busybox\", \"mount\", \"/system\");\n"
                        //+ "run_program(\"/sbin/busybox\", \"mount\", \"/data\");\n"
                        + createDirectory("/system/app")
                        + createDirectory("/data/app") + "\n";
            case 2:
                //future aspect
                break;
        }
        return "";
    }

    public String getFolderScript(String str, ProjectItemNode parent) {
        str += createDirectory(((FolderNode) parent).prop.folderLocation.replaceAll("\\\\", "/"));
        if (((FolderNode) parent).prop.setPermissions) {
            str += "set_perm(" + ((FolderNode) parent).prop.folderPermission + ");\n";
        }
        for (ProjectItemNode child : parent.prop.children) {
            if (child.prop.type == Types.NODE_FOLDER) {
                str = getFolderScript(str, child);
            } else if (child.prop.type == Types.NODE_FILE) {
                FileNode file = (FileNode) child;
                if (file.prop.title.endsWith("apk")) {
                    str += addPrintString("Copying " + file.prop.title);
                    FolderNode folder = (FolderNode) (file.prop.parent);
                    GroupNode group = (GroupNode) (folder.prop.originalParent);
                    if (group.prop.groupType == Types.GROUP_DATA_APP) {
                        str += "package_extract_file(\"" + file.prop.fileZipPath + "\", \"" + file.prop.fileInstallLocation + "/" + "base.apk" + "\");\n";
                    } else {
                        str += "package_extract_file(\"" + file.prop.fileZipPath + "\", \"" + file.prop.fileInstallLocation + "/" + file.prop.title + "\");\n";
                    }
                } else {
                    str += addPrintString("Copying " + file.prop.title);
                    str += "package_extract_file(\"" + file.prop.fileZipPath + "\", \"" + file.prop.fileInstallLocation + "/" + file.prop.title + "\");\n";
                }
                if (file.prop.setPermissions) {
                    str += "set_perm(" + file.prop.filePermission + ");\n";
                }
            }
        }
        return str;
    }

    public String predefinedFolderGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            if (Preferences.pp.IsFromLollipop) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                for (ProjectItemNode child : node.prop.children) {
                    if (child.prop.type == Types.NODE_FOLDER) {
                        str += addPrintString(child.prop.title, installString);
                        str = getFolderScript(str, child);
                    } else if (child.prop.type == Types.NODE_FILE) {
                        str += addPrintString(child.prop.title, copyString);
                        str += "package_extract_file(\"" + ((FileNode) child).prop.fileZipPath + "\", \"" + ((FileNode) child).prop.fileInstallLocation + "/" + ((FileNode) child).prop.title + "\");\n";
                        if (((FileNode) child).prop.setPermissions) {
                            str += "set_perm(" + ((FileNode) child).prop.filePermission + ");\n";
                        }
                    }
                }
                str += "endif;\n\n";
                for (ProjectItemNode child : node.prop.children) {
                    str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                    if (child.prop.type == Types.NODE_FOLDER) {
                        str += addPrintString(child.prop.title, installString);
                        str = getFolderScript(str, child);
                    } else if (child.prop.type == Types.NODE_FILE) {
                        str += addPrintString(child.prop.title, copyString);
                        str += "package_extract_file(\"" + ((FileNode) child).prop.fileZipPath + "\", \"" + ((FileNode) child).prop.fileInstallLocation + "/" + ((FileNode) child).prop.title + "\");\n";
                        if (((FileNode) child).prop.setPermissions) {
                            str += "set_perm(" + ((FileNode) child).prop.filePermission + ");\n";
                        }
                    }
                    str += "endif;\n\n";
                }
            } else {
                str = predefinedGroupScript(node);
            }
        }
        return str;
    }

    public String predefinedGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
//            str += getPackageExtractDirString(node);
            str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
            for (ProjectItemNode fnode : node.prop.children) {
                FileNode file = (FileNode) fnode;
                if (node.prop.groupType == Types.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).prop.title, deleteString);
                    str += "delete(\"/" + ((FileNode) file).getDeleteLocation() + "\");\n";
                } else {
                    str += addPrintString(file.prop.title, copyString);
                    str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                    if (((FileNode) file).prop.setPermissions) {
                        str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                    }
                }
            }
            str += "endif;\n";
            for (ProjectItemNode file : node.prop.children) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                if (node.prop.groupType == Types.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).prop.title, deleteString);
                    str += "delete(\"/" + ((FileNode) file).getDeleteLocation() + "\");\n";
                } else {
                    str += addPrintString(((FileNode) file).prop.title, copyString);
                    str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                    if (((FileNode) file).prop.setPermissions) {
                        str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                    }
                }
                str += "endif;\n";
            }
        } else if (node.isSelectBox() && node.prop.groupType == Types.GROUP_SCRIPT) {
            int count = 2;
            for (ProjectItemNode file : node.prop.children) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                str += addPrintString("@Running script : " + ((FileNode) file).prop.title);
                str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"/tmp/script\");\n"
                        + "set_perm(0, 0, 0777, \"/tmp/script\");\n"
                        + "run_program(\"/tmp/script\");\n"
                        + "delete(\"/tmp/script\");\n";
                str += "endif;\n";
            }
        } else {
            System.out.println("This Group is not supported");
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
//            str += getPackageExtractDirString(node);
            for (ProjectItemNode subGroup : node.prop.children) {
                if (node.prop.groupType == Types.GROUP_SYSTEM_FONTS) {
                    str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile.replace(".prop", "_" + node.prop.title + ".prop") + "\", \"" + subGroup.toString() + "\")==\"" + "yes" + "\") then \n";
                } else {
                    str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                }
                str += addPrintString(((SubGroupNode) subGroup).prop.title, installString);
                for (ProjectItemNode file : subGroup.prop.children) {
                    switch (node.prop.groupType) {
                        case Types.GROUP_SYSTEM_FONTS:
                            str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                            if (((FileNode) file).prop.setPermissions) {
                                str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n\n";
                            }
                            break;
                        case Types.GROUP_DATA_LOCAL:
                        case Types.GROUP_SYSTEM_MEDIA:
                            //this will rename any zip package to bootamination.zip allowing users to add bootanimation.zip with custom names.
                            str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + "bootanimation.zip" + "\");\n";
                            if (((FileNode) file).prop.setPermissions) {
                                str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n\n";
                            }
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
            str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
            for (ProjectItemNode tempNode : node.prop.children) {
                switch (tempNode.prop.type) {
                    case Types.NODE_FOLDER:
                        str += addPrintString(((FolderNode) tempNode).prop.title, installString);
                        str = getFolderScript(str, (FolderNode) tempNode);
                        break;
                    case Types.NODE_FILE:
                        str += addPrintString(((FileNode) tempNode).prop.title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).prop.fileZipPath + "\", \"" + ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title + "\");\n";
                        if (((FileNode) tempNode).prop.setPermissions) {
                            str += "set_perm(" + ((FileNode) tempNode).prop.filePermission + ");\n";
                        }
                }
            }
            str += "endif;\n";
            for (ProjectItemNode tempNode : node.prop.children) {
                switch (tempNode.prop.type) {
                    case Types.NODE_FOLDER:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                        str += addPrintString(((FolderNode) tempNode).prop.title, installString);
                        str = getFolderScript(str, (FolderNode) tempNode);
                        str += "endif;\n";
                        break;
                    case Types.NODE_FILE:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                        str += addPrintString(((FileNode) tempNode).prop.title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).prop.fileZipPath + "\", \"" + ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title + "\");\n";
                        if (((FileNode) tempNode).prop.setPermissions) {
                            str += "set_perm(" + ((FileNode) tempNode).prop.filePermission + ");\n";
                        }
                        str += "endif;\n";
                }
            }
        } else if (node.isSelectBox()) {
            int count = 2;
            for (ProjectItemNode tempNode : node.prop.children) {
                switch (tempNode.prop.type) {
                    case Types.NODE_SUBGROUP:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                        str += addPrintString(((FolderNode) tempNode).prop.title, installString);
                        str = getFolderScript(str, (FolderNode) tempNode);
                        str += "endif;\n";
                        break;
                    case Types.NODE_FILE:
                        str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"selected.1\")==\"" + count++ + "\") then \n";
                        str += addPrintString(((FileNode) tempNode).prop.title, installString);
                        str += "package_extract_file(\"" + ((FileNode) tempNode).prop.fileZipPath + "\", \"" + ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title + "\");\n";
                        if (((FileNode) tempNode).prop.setPermissions) {
                            str += "set_perm(" + ((FileNode) tempNode).prop.filePermission + ");\n";
                        }
                        str += "endif;\n";
                }
            }
            str += "endif;\n";
        }
        return str;
    }

    public String generateUpdaterScript(GroupNode node) {
        switch (node.prop.groupType) {
            //Group of predefined locations
            case Types.GROUP_SYSTEM:
            case Types.GROUP_SYSTEM_APK:
            case Types.GROUP_SYSTEM_PRIV_APK:
            case Types.GROUP_DATA_APP:
            case Types.GROUP_SYSTEM_BIN:
            case Types.GROUP_SYSTEM_ETC:
            case Types.GROUP_SYSTEM_FRAMEWORK:
                return predefinedFolderGroupScript(node);
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case Types.GROUP_DELETE_FILES:
            case Types.GROUP_SCRIPT:
                return predefinedGroupScript(node);
            //Group of predefined locations that need subgroups
            case Types.GROUP_SYSTEM_FONTS:
            case Types.GROUP_DATA_LOCAL:
            case Types.GROUP_SYSTEM_MEDIA:
                return predefinedSubGroupsScript(node);
            //Group of custom location.
            case Types.GROUP_CUSTOM:
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

    public String createDirectory(String dir) {
        return "run_program(\"/sbin/busybox\", \"mkdir\", \"-p\", \"" + dir + "\");\n";
    }
}
