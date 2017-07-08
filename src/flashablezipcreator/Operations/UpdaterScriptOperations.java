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

import flashablezipcreator.Core.DeleteNode;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.UserInterface.Preference;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Script;
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
                return "ui_print(\"" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Installing " + str + "\");\n";
            case deleteString:
                return "ui_print(\"" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Deleting " + str + "\");\n";
            case copyString:
                return "ui_print(\"" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Copying " + str + "\");\n";
        }
        return "ui_print(\"" + str + "\");\n";
    }

    public String addPrintString(String str) {
        return "ui_print(\"" + str + "\");\n";
    }

    public String initiateUpdaterScript() {
        return addPrintString("" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Starting the install process")
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

    public String addNormalAddonDString() {
        String str = "";
        str += "ui_print(\"Generating addon.d script\");\n"
                + getAddonBinaryString()
                + "ui_print(\"Executing addon.d script\");\n"
                + getExecuteScriptString(Script.addonScriptTempPath, "-di", Script.logDataPath)
                + "ui_print(\"Done!\");\n";
        return str;
    }

    public String addAromaAddonDString() {
        String str = "";
        str += "if(file_getprop(\"/tmp/aroma/addond_choices.prop\", \"true\")==\"yes\") then\n"
                + "ui_print(\"@Generating addon.d script\");\n"
                + getAddonBinaryString()
                + "ui_print(\"@Executing addon.d script\");\n"
                + getExecuteScriptString(Script.addonScriptTempPath, "-di", Script.logDataPath)
                + "ui_print(\"@Done!\");\n"
                + "endif;\n";
        return str;
    }

    public String getMountMethod(int type) {
        switch (type) {
            case 1:
                return addPrintString("" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Mounting Partitions...")
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
                str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", file.prop.fileInstallLocation + "/" + file.prop.title);
            }
        }
        return str;
    }

    public String predefinedNormalFolderGroupScript(GroupNode node) {
        String str = "";
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
                str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) child).prop.fileInstallLocation + "/" + ((FileNode) child).prop.title);
            }
        }
        return str;
    }

    public String predefinedAromaFolderGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
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
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) child).prop.fileInstallLocation + "/" + ((FileNode) child).prop.title);
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
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) child).prop.fileInstallLocation + "/" + ((FileNode) child).prop.title);
                }
                str += "endif;\n\n";
            }
        }
        return str;
    }

    public String predefinedNormalGroupScript(GroupNode node) {
        String str = "";
        for (ProjectItemNode cnode : node.prop.children) {
            if (node.prop.groupType == Types.GROUP_DELETE_FILES) {
                DeleteNode file = (DeleteNode) cnode;
                str += addPrintString(file.prop.title, deleteString);
                str += "delete_recursive(\"" + file.getDeleteLocation() + "\");\n";
                str += getExecuteScriptString(Script.afzcScriptTempPath, "-di", file.getDeleteLocation());
            } else {
                FileNode file = (FileNode) cnode;
                str += addPrintString(file.prop.title, copyString);
                str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                if (((FileNode) file).prop.setPermissions) {
                    str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                }
                str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title);
            }
        }
        return str;
    }

    public String predefinedAromaGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
            for (ProjectItemNode cnode : node.prop.children) {
                if (node.prop.groupType == Types.GROUP_DELETE_FILES) {
                    DeleteNode file = (DeleteNode) cnode;
                    str += addPrintString(file.prop.title, deleteString);
                    str += "delete_recursive(\"" + file.getDeleteLocation() + "\");\n";
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-di", file.getDeleteLocation());
                } else {
                    FileNode file = (FileNode) cnode;
                    str += addPrintString(file.prop.title, copyString);
                    str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                    if (((FileNode) file).prop.setPermissions) {
                        str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                    }
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title);
                }
            }
            str += "endif;\n";
            for (ProjectItemNode file : node.prop.children) {
                str += "if (file_getprop(\"/tmp/aroma/" + node.prop.propFile + "\", \"item.1." + count++ + "\")==\"1\") then \n";
                if (node.prop.groupType == Types.GROUP_DELETE_FILES) {
                    str += addPrintString(((DeleteNode) file).prop.title, deleteString);
                    str += "delete_recursive(\"" + ((DeleteNode) file).getDeleteLocation() + "\");\n";
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-di", ((DeleteNode) file).getDeleteLocation());
                } else {
                    str += addPrintString(((FileNode) file).prop.title, copyString);
                    str += "package_extract_file(\"" + ((FileNode) file).prop.fileZipPath + "\", \"" + ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title + "\");\n";
                    if (((FileNode) file).prop.setPermissions) {
                        str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                    }
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title);
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

    public String predefinedNormalSubGroupsScript(GroupNode node) {
        String str = "";
        for (ProjectItemNode subGroup : node.prop.children) {
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
                            str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                        }
                        break;
                }
                str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title);
            }
        }
        return str;
    }

    public String predefinedAromaSubGroupsScript(GroupNode node) {
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
                                str += "set_perm(" + ((FileNode) file).prop.filePermission + ");\n";
                            }
                            break;
                    }
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) file).prop.fileInstallLocation + "/" + ((FileNode) file).prop.title);
                }
                str += "endif;\n";
            }
        }
        return str;
    }

    public String customNormalGroupScript(GroupNode node) {
        String str = "";
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
                    str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title);
            }
        }
        return str;
    }

    public String customAromaGroupScript(GroupNode node) {
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
                        str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title);
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
                        str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title);
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
                        str += getExecuteScriptString(Script.afzcScriptTempPath, "-ei", ((FileNode) tempNode).prop.fileInstallLocation + "/" + ((FileNode) tempNode).prop.title);
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
                if (Preference.pp.createZipType.equals("Aroma")) {
                    return predefinedAromaFolderGroupScript(node);
                } else if (Preference.pp.createZipType.equals("Normal")) {
                    return predefinedNormalFolderGroupScript(node);
                }
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case Types.GROUP_DELETE_FILES:
            case Types.GROUP_SCRIPT:
                if (Preference.pp.createZipType.equals("Aroma")) {
                    return predefinedAromaGroupScript(node);
                } else if (Preference.pp.createZipType.equals("Normal")) {
                    return predefinedNormalGroupScript(node);
                }
            //Group of predefined locations that need subgroups
            case Types.GROUP_SYSTEM_FONTS:
            case Types.GROUP_DATA_LOCAL:
            case Types.GROUP_SYSTEM_MEDIA:
                if (Preference.pp.createZipType.equals("Aroma")) {
                    return predefinedAromaSubGroupsScript(node);
                } else if (Preference.pp.createZipType.equals("Normal")) {
                    return predefinedNormalSubGroupsScript(node);
                }
            //Group of custom location.
            case Types.GROUP_CUSTOM:
                if (Preference.pp.createZipType.equals("Aroma")) {
                    return customAromaGroupScript(node);
                } else if (Preference.pp.createZipType.equals("Normal")) {
                    return customNormalGroupScript(node);
                }
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

    public String getExecuteScriptString(String scriptPath, String command, String data) {
        String str = "";
        switch (command) {
            case "-ei":
                if (data.startsWith("/system/")) {
                    data = data.substring(8, data.length());
                    str = "run_program(\"" + scriptPath + "\", \"$OUTFD\", \"" + command + "\", \"" + data + "\");\n";
                }
                break;
            case "-di":
                str = "run_program(\"" + scriptPath + "\", \"$OUTFD\", \"" + command + "\", \"" + data + "\");\n";
                break;
        }
        return str;
    }

    public String delete_recursive(String location) {
        return "delete_recursive(\"" + location + "\");\n";
    }

    public String package_extract_file(String source, String dest) {
        return "package_extract_file(\"" + source + "\", \"" + dest + "\");\n";
    }

    public String deleteAddonBackupData() {
        return delete_recursive(Script.logDataPath);
    }

    public String getAfzcBinaryString() {
        String str = "\n";
        str += createDirectory(Script.afzcScriptTempPath.substring(0, Script.afzcScriptTempPath.lastIndexOf("/")));
        str += package_extract_file(Script.afzcScriptZipPath, Script.afzcScriptTempPath);
        str += "set_perm(0, 0, 0755, \"" + Script.afzcScriptTempPath + "\");\n";
        return str;
    }

    public String getAddonBinaryString() {
        String str = "\n";
        str += createDirectory(Script.addonScriptTempPath.substring(0, Script.addonScriptTempPath.lastIndexOf("/")));
        str += package_extract_file(Script.addonScriptZipPath, Script.addonScriptTempPath);
        str += "set_perm(0, 0, 0755, \"" + Script.addonScriptTempPath + "\");\n";
        return str;
    }

    //following function is for future reference
    public String getModString() {
        String str = "";
        str = "package_extract_dir(\"Install\", \"/tmp/Install\");\n";
        str += "run_program(\"/sbin/busybox\", \"unzip\", \"/tmp/Install/busybox.zip\", \"META-INF/com/google/android/*\", \"-d\", \"/tmp/Install\");\n";
        str += "run_program(\"/sbin/sh\", \"/tmp/Install/META-INF/com/google/android/update-binary\", \"dummy\", \"1\", \"/tmp/Install/busybox.zip\");";
        return str;
    }
}
