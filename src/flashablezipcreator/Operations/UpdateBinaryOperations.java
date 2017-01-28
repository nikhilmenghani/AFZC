/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.UserInterface.Preferences;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.FlashableZipCreator;
import static flashablezipcreator.Protocols.UpdaterScript.symlinkScriptPath;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Rajat
 */
public class UpdateBinaryOperations {

    public static final int installString = 1;
    public static final int deleteString = 2;
    public static final int normalString = 3;
    public static final String universalUpdateBinaryFilePath = "/flashablezipcreator/META-INF/com/google/android/universal-update-binary"; //Add universal binary to this path

    public String addPrintString(String str, int type) {
        switch (type) {
            case installString:
                return "ui_print \"@Installing " + str + "\"\n";
            case deleteString:
                return "ui_print \"@Deleting " + str + "\"\n";
        }
        return "ui_print \"" + str + "\"\n";
    }

    public String addPrintString(String str) {
        return "ui_print \"" + str + "\"\n";
    }

    public String initiateUpdaterScript() throws FileNotFoundException, IOException {
        Read reader = new Read();
        String initShellScript = reader.getStringFromFile(FlashableZipCreator.class.getResourceAsStream(universalUpdateBinaryFilePath));
        return initShellScript + addPrintString("@Starting the install process")
                + addPrintString("Setting up required tools...");
    }

    public String terminateUpdaterScript() {
        return addPrintString("Unmounting Partitions...")
                + "unmount /data\n"
                + "unmount /system\n";
    }

    public String addWipeDalvikCacheString() {
        String str = "";
        str += "mount -o rw,remount,rw /data\n";
        str += "if [ $(file_getprop /tmp/aroma/dalvik_choices.prop true) ==  yes ]; then\n"
                + "ui_print \"@Wiping dalvik-cache\"\n"
                + "delete_recursive /data/dalvik-cache\n"
                + "fi;\n";
        str += "umount /data\n";
        return str;
    }

    public String getMountMethod(int type) {
        switch (type) {
            case 1:
                return addPrintString("@Mounting Partitions...")
                        + "mount /system\n"
                        + "mount /data\n"
                        + "if [ $(is_mounted /data) == 1 ]; then\n"
                        + addPrintString("/data already mounted. Remounting...")
                        + "umount /data\n"
                        + "mount /data\n"
                        + "fi;\n"
                        + "mount -o rw,remount /system\n"
                        + "mount -o rw,remount /system /system\n"
                        + "mount -o rw,remount /\n"
                        + "mount -o rw,remount / /\n"
                        + addPrintString("Extracting zip contents to /tmp")
                        + "cd /tmp\n"
                        + "mkdir zipContent\n"
                        + "cd zipContent\n";
            case 2:
                //future aspect
                break;
        }
        return "";
    }

    public String getFolderScript(String str, ProjectItemNode parent) {
        for (ProjectItemNode child : parent.children) {
            if (child.type == ProjectItemNode.NODE_FOLDER) {
                str = getFolderScript(str, child);
            } else if (child.type == ProjectItemNode.NODE_FILE) {
                FileNode file = (FileNode) child;
                str += getPackageExtractDirString(file);
                if (file.title.endsWith("apk")) {
                    str += addPrintString(file.parent.title, installString);
                } else {
                    str += addPrintString("Copying " + file.title);
                }
                str += "package_extract_file " + file.fileZipPath + " " + file.installLocation + "/" + file.title + "\n";
                str += "set_perm " + file.filePermission + "\n";  //TODO: Inspect filePermission for removal of commas
            }
        }
        return str;
    }

    public String predefinedFolderGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            if (Preferences.IsFromLollipop) {
                str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
                for (ProjectItemNode folder : node.children) {
                    str = getFolderScript(str, folder);
                }
                str += "fi;\n";
                for (ProjectItemNode folder : node.children) {
                    str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
                    str = getFolderScript(str, folder);
                    str += "fi;\n";
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
            str += getPackageExtractDirString(node);
            str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
            for (ProjectItemNode fnode : node.children) {
                FileNode file = (FileNode) fnode;
                if (node.groupType == GroupNode.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).title, deleteString);
                    str += "delete /" + ((FileNode) file).getDeleteLocation() + "\n";
                } else {
                    str += addPrintString(file.title, installString);
                    str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                    str += "set_perm " + ((FileNode) file).filePermission + "\n";
                }
            }
            str += "fi;\n";
            for (ProjectItemNode file : node.children) {
                str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
                if (node.groupType == GroupNode.GROUP_DELETE_FILES) {
                    str += addPrintString(((FileNode) file).title, deleteString);
                    str += "delete /" + ((FileNode) file).getDeleteLocation() + "\n";
                } else {
                    str += addPrintString(((FileNode) file).title, installString);
                    str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                    str += "set_perm " + ((FileNode) file).filePermission + "\n";
                }
                str += "fi;\n";
            }
        } else if (node.isSelectBox() && node.groupType == GroupNode.GROUP_SCRIPT) {
            int count = 2;
            for (ProjectItemNode file : node.children) {
                str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " selected.1 ) == " + count++ + " ]; then\n";
                str += addPrintString("@Running script : " + ((FileNode) file).title);
                str += "package_extract_file " + ((FileNode) file).fileZipPath + " /tmp/script\n"
                        + "set_perm 0 0 0777 /tmp/script\n"
                        + "./tmp/script\n"  //TODO: CHECK Here
                        + "delete /tmp/script\n";
                str += "fi;\n";
            }
        } else {
            System.out.println("This Group is not supported");
        }
        return str;
    }

    public String deleteTempFiles() {
        String str = "";
        for (String path : Project.getTempFilesList()) {
            str += "delete /" + path + "\n";
        }
        return str;
    }

    public String predefinedSubGroupsScript(GroupNode node) {
        String str = "";
        if (node.isSelectBox()) {
            int count = 2;
            str += getPackageExtractDirString(node);
            for (ProjectItemNode subGroup : node.children) {
                if (node.groupType == GroupNode.GROUP_SYSTEM_FONTS) {
                    str += "if [ $(file_getprop /tmp/aroma/" + node.prop.replace(".prop", "_temp.prop") + " " + subGroup.toString() + ") == \"" + "yes" + "\" ]; then\n";
                } else {
                    str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " selected.1 ) == " + count++ + " ]; then\n";
                }
                str += addPrintString(((SubGroupNode) subGroup).title, installString);
                for (ProjectItemNode file : subGroup.children) {
                    switch (node.groupType) {
                        case GroupNode.GROUP_SYSTEM_FONTS:
                            str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                            str += "set_perm " + ((FileNode) file).filePermission + "\n";
                            break;
                        case GroupNode.GROUP_DATA_LOCAL:
                        case GroupNode.GROUP_SYSTEM_MEDIA:
                            //this will rename any zip package to bootamination.zip allowing users to add bootanimation.zip with custom names.
                            str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + "bootanimation.zip" + "\n";
                            str += "set_perm " + ((FileNode) file).filePermission + "\n";
                            break;
                    }
                }
                str += "fi;\n";
            }
        }
        return str;
    }

    public String customGroupScript(GroupNode node) {
        String str = "";
        if (node.isCheckBox()) {
            int count = 1;
            str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                            str += "set_perm " + ((FileNode) file).filePermission + "\n";
                        }
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file " + ((FileNode) tempNode).fileZipPath + " " + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\n";
                        str += "set_perm " + ((FileNode) tempNode).filePermission + "\n";
                }
            }
            str += "fi;\n";
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                            str += "set_perm " + ((FileNode) file).filePermission + "\n";
                        }
                        str += "fi;\n";
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " item.1." + count++ + ") == 1 ]; then\n";
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file " + ((FileNode) tempNode).fileZipPath + " " + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\n";
                        str += "set_perm " + ((FileNode) tempNode).filePermission + "\n";
                        str += "fi;\n";
                }
            }
        } else if (node.isSelectBox()) {
            int count = 2;
            for (ProjectItemNode tempNode : node.children) {
                switch (tempNode.type) {
                    case ProjectItemNode.NODE_SUBGROUP:
                        str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " selected.1 ) == " + count++ + " ]; then\n";
                        str += addPrintString(((SubGroupNode) tempNode).title, installString);
                        for (ProjectItemNode file : tempNode.children) {
                            str += "package_extract_file " + ((FileNode) file).fileZipPath + " " + ((FileNode) file).installLocation + "/" + ((FileNode) file).title + "\n";
                            str += "set_perm " + ((FileNode) file).filePermission + "\n";
                        }
                        str += "fi;\n";
                        break;
                    case ProjectItemNode.NODE_FILE:
                        str += "if [ $(file_getprop /tmp/aroma/" + node.prop + " selected.1 ) ==  " + count++ + " ]; then\n";
                        str += addPrintString(((FileNode) tempNode).title, installString);
                        str += "package_extract_file " + ((FileNode) tempNode).fileZipPath + " " + ((FileNode) tempNode).installLocation + "/" + ((FileNode) tempNode).title + "\n";
                        str += "set_perm " + ((FileNode) tempNode).filePermission + "\n";
                        str += "fi;\n";
                }
            }
            str += "fi;\n";
        }
        return str;
    }

    public String generateUpdaterScript(GroupNode node) {
        switch (node.groupType) {
            //Group of predefined locations
            case GroupNode.GROUP_SYSTEM_APK:
            case GroupNode.GROUP_SYSTEM_PRIV_APK:
            case GroupNode.GROUP_DATA_APP:
                return predefinedFolderGroupScript(node);
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
            case GroupNode.GROUP_DELETE_FILES:
            case GroupNode.GROUP_SCRIPT:
                return predefinedGroupScript(node);
            //Group of predefined locations that need subgroups
            case GroupNode.GROUP_SYSTEM_FONTS:
            case GroupNode.GROUP_DATA_LOCAL:
            case GroupNode.GROUP_SYSTEM_MEDIA:
                return predefinedSubGroupsScript(node);
            //Group of custom location.
            case GroupNode.GROUP_CUSTOM:
                return customGroupScript(node);
        }
        return "";
    }

    public String setProgress(int progress) {
        return "\nset_progress " + progress + "\n";
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
        return "package_extract_dir data\\ /data\n"
                + "set_perm 2000 2000 0771 /data/local\n"
                + "set_perm_recursive 1000 1000 0771 0644 /data/app\n";
    }

    public String getPackageExtractDirString(FileNode file) {
        String str = addPrintString("Creating folder in " + file.groupLocation);
        String extractZipPath = file.extractZipPath;
        extractZipPath = extractZipPath.substring(0, extractZipPath.indexOf(file.groupLocation));
        if (file.groupLocation.contains("/system")) {
            return str += "package_extract_dir " + extractZipPath + "/system" + " /system\n";
        } else if (file.groupLocation.contains("/data")) {
            return str += "package_extract_dir " + extractZipPath + "/data" + " /data\n";
        }
        return "";
    }

    public String getPackageExtractDirString(GroupNode group) {
        String str = addPrintString("Creating folder in " + group.location);
        String extractZipPath = (group.extractZipPath + "/" ).replaceAll("\\\\", "/"); // + "afzc_temp"
        extractZipPath = extractZipPath.substring(0, extractZipPath.indexOf(group.location));
        if (group.location.contains("/system")) {
            return str += "package_extract_dir " + extractZipPath + "/system" + " /system\n";
        } else if (group.location.contains("/data")) {
            return str += "package_extract_dir " + extractZipPath + "/data" + " /data\n";
        }
        return "";
    }
}
