/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.DeleteNode;
import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.UserInterface.Preference;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Types;

/**
 *
 * @author Nikhil
 */
public class AromaScriptOperations {

    String splashPath = "splash/AFZC";
    String fontsPath = "ttf/Roboto-Regular.ttf;";

    public String addSplashString() {
        return "anisplash(\n"
                + "2,\n"
                + "\"" + splashPath + "\", 1200\n"
                + ");\n";
    }

    public String addFontsString() {
        return "fontresload(\"0\", \"" + fontsPath + "\", \"12\");\n"
                + "fontresload(\"1\", \"" + fontsPath + "\", \"14\");\n";
    }

    public String addThemesString(ProjectItemNode rootNode) {
        String str = "theme(\"" + "Nikhil" + "\");\n";
        str += "\nselectbox(\"Themes\",\"Choose your desired theme from following\",\"@customize\",\"theme.prop\",\n";
        for (ProjectItemNode projectNode : rootNode.prop.children) {
            if (((ProjectNode) projectNode).prop.projectType == Types.PROJECT_THEMES) {
                for (ProjectItemNode theme : ((ProjectNode) projectNode).prop.children) {
                    str += "\"" + theme.prop.title + "\", \"\", " + (theme.prop.title.equals("Nikhil") ? 1 : 0) + ",\n";
                }
            }
        }
        str = str.substring(0, str.length() - 2);
        str += ");\n\n";
        int i = 1;
        for (ProjectItemNode projectNode : rootNode.prop.children) {
            if (((ProjectNode) projectNode).prop.projectType == Types.PROJECT_THEMES) {
                for (ProjectItemNode theme : ((ProjectNode) projectNode).prop.children) {
                    str += "if prop(\"theme.prop\", \"selected.0\")==\"" + i++ + "\" then\n"
                            + "theme(\"" + theme.prop.title + "\");\n"
                            + "endif;\n"
                            + "\n";
                }
            }
        }
        return str;
    }

    public String addFontsViewBoxString() {
        String str = "";
        str = "\nviewbox(\"Preview\","
                + "\"\\nThe font \"+getvar(\"fontname\")+\" looks like this\\n\\n\"+"
                + "\"Preview:\\n\\n\"+\"AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz.,1234567890\\n\\n\"+\"Are you sure you want to continue installing this font?\",\n"
                + "\"@update\");\n";
        return str;
    }

    public String addAgreeBox() {
        return "\nagreebox(\"Important notes!\","
                + " \"Terms & Conditions\","
                + " \"@agreement\","
                + "resread(\"Terms and Conditions.txt\"),"
                + " \"I agree with these Terms of Use...\","
                + " \"You need to agree with the Terms of Use...\");\n";
    }

    public String addSelectBox(GroupNode node) {
        String str = "";
        switch (node.prop.groupType) {
            case Types.GROUP_DATA_LOCAL:
            case Types.GROUP_SYSTEM_MEDIA:
//            case Types.GROUP_AROMA_KERNEL:
            case Types.GROUP_SCRIPT:
                str += "\nselectbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@personalize\",\"" + node.prop.propFile + "\",\n"
                        + "\"Select one from the list\", \"\", 2,\n"
                        + "\"Select None\",\"Skip this Group.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {

                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).prop.description + "\", 0";
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
                break;
            case Types.GROUP_CUSTOM:
                if (!node.isSelectBox()) {
                    break;
                }
                str += configCustomGroup(node, node.isSelectBox());
            case Types.GROUP_SYSTEM_FONTS:
                str += configFonts(node);
                break;
        }
        return str;
    }

    public String configCustomGroup(GroupNode node, boolean flag) {
        String str = "";
        if (flag) {//flag is to separate selectbox and checkbox.
            str += "\nselectbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@personalize\",\"" + node.prop.propFile + "\",\n"
                    + "\"Select one from the list\", \"\", 2,\n"
                    + "\"Select None\",\"Skip this Group.\", 1";
            for (int i = 0; i < node.getChildCount(); i++) {
                try {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FolderNode) node.getChildAt(i)).prop.description + "\", 0";
                } catch (ClassCastException cce) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).prop.description + "\", 0";
                }
            }
            str += ");\n";
            str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
        } else {
            str += "\ncheckbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@apps\",\"" + node.prop.propFile + "\",\n"
                    + "\"Select files from the list\", \"\", 2,\n"
                    + "\"Select All\",\"Installs All Files.\", 1";
            for (int i = 0; i < node.getChildCount(); i++) {
                try {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FolderNode) node.getChildAt(i)).prop.description + "\", 0";
                } catch (ClassCastException cce) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).prop.description + "\", 0";
                }
            }
            str += ");\n";
            str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
        }
        return str;
    }

    public String configFonts(GroupNode node) {
        String str = "";
        str += "\nselectbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + " List For Preview\",\"@info\",\"" + node.prop.propFile + "\""
                + ",\n\"Select None\",\"Skip this Group.\", 1";
        for (int i = 0; i < node.getChildCount(); i++) {
            str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).prop.description + "\", " + 0 + "";
        }
        str += ");\n";
        str += "writetmpfile(\"" + node.prop.propFile.replace(".prop", "_" + node.prop.title + ".prop") + "\",\"init=no\\n\");\n";//initialize temp.prop.

        str += "if prop(\"" + node.prop.propFile + "\", \"selected.0\")==\"" + 1 + "\" then\n";
        str += "setvar(\"fontname\",\"" + "default" + "\");\n";
        str += "endif;\n";

        if (node.prop.groupType == Types.GROUP_SYSTEM_FONTS) {
            for (int i = 0; i < node.getChildCount(); i++) {
                SubGroupNode sgnode = (SubGroupNode) node.getChildAt(i);
                str += "if prop(\"" + node.prop.propFile + "\", \"selected.0\")==\"" + (i + 2) + "\" then\n";
                for (int j = 0; j < sgnode.getChildCount(); j++) {
                    FileNode fnode = (FileNode) sgnode.getChildAt(j);
                    if (((FileNode) fnode).prop.title.equals("DroidSans.ttf")
                            || ((FileNode) fnode).prop.title.equals("Roboto-Regular.ttf")) {
                        str += "fontresload(\"0\", \"ttf/" + sgnode.toString() + ".ttf;\", \"15\");\n";
                    }
                }
                str += "setvar(\"fontname\",\"" + sgnode.toString() + "\");\n";
                str += "writetmpfile(\"" + node.prop.propFile.replace(".prop", "_" + node.prop.title + ".prop") + "\",\"" + sgnode + "=yes\\n\");\n";
                str += "endif;\n";
            }
            str += addFontsViewBoxString();
            str += addFontsString();
        }
        return str;
    }

    public String addMenuBox(ProjectNode project) {
        String str = "";
        switch (project.prop.projectType) {
            case Types.PROJECT_AROMA:
            case Types.PROJECT_CUSTOM:
            case Types.PROJECT_MOD:
                str += "\nmenubox(\"" + "Menu" + " List\",\"Select from " + "following" + "\",\"@installmods\",\"" + project.prop.title + ".prop" + "\",\n"
                        + "\"Open " + project.prop.title + "\", \"Install files from " + project.prop.title + "\", \"@install\"";
                str += ",\n\"" + "Skip" + "\", \"Do Not Install files from " + project.prop.title + "\", \"@apps\"";
                str += ");\n";
                str += "writetmpfile(\"" + project.prop.title + ".prop" + "\",readtmpfile(\"" + project.prop.title + ".prop" + "\"));\n";
                break;
        }
        return str;
    }

    public String addInitString(ProjectNode project) {
        String str = "";
        switch (project.prop.projectType) {
            case Types.PROJECT_AROMA:
            case Types.PROJECT_CUSTOM:
            case Types.PROJECT_MOD:
                str += "setvar(\"release_version\",\"" + project.prop.releaseVersion + "\");\n"
                        + "setvar(\"android_version\",\"" + project.prop.androidVersion + "\");\n"
                        + "setvar(\"file_creator\",\"" + Project.zipCreator + "\");\n";
                break;
        }
        return str;
    }

    public String addWelcomeString(ProjectNode project) {
        String str = "";
        switch (project.prop.projectType) {
            case Types.PROJECT_AROMA:
            case Types.PROJECT_CUSTOM:
            case Types.PROJECT_MOD:
                str += "\nviewbox(\n"
                        + "\"Welcome\",\n"
                        + "\"You are about to make additional changes to your device.\\n\\n<b>\"+\n"
                        + "\n"
                        + "\"Android Version\\t:\\t <b><#selectbg_g>\"+getvar(\"android_version\")+\"</#></b>\\n\"+\n"
                        + "\"Release Version\\t:\\t <b><#selectbg_g>\"+getvar(\"release_version\")+\"</#></b>\\n\"+\n"
                        + "\"File Created By\\t:\\t <b><#selectbg_g>\"+getvar(\"file_creator\")+\"</#></b>\\n\\n\\n\"+\n"
                        + "\n"
                        + "\"Press Next to Continue ...\",\n"
                        + "\"@welcome\"\n"
                        + ");\n";
                break;
        }

        return str;
    }

    public String addCheckBox(GroupNode node) {
        String str = "";
        switch (node.prop.groupType) {
            case Types.GROUP_SYSTEM:
            case Types.GROUP_SYSTEM_APK:
            case Types.GROUP_SYSTEM_PRIV_APK:
            case Types.GROUP_DATA_APP:
            case Types.GROUP_SYSTEM_BIN:
            case Types.GROUP_SYSTEM_ETC:
            case Types.GROUP_SYSTEM_FRAMEWORK:
                str += "\ncheckbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@apps\",\"" + node.prop.propFile + "\",\n"
                        + "\"Select files from the list\", \"\", 2,\n"
                        + "\"Select All\",\"Installs All Files.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {
//                    if (Preference.pp.IsFromLollipop) {
                    switch (node.getChildAt(i).prop.type) {
                        case Types.NODE_FOLDER:
                            str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FolderNode) node.getChildAt(i)).prop.description + "\", 0";
                            break;
                        case Types.NODE_FILE:
                            str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).prop.description + "\", 0";
                            break;
                    }
//                    } else {
//                        str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).prop.description + "\", 0";
//                    }
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                str += "\ncheckbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@apps\",\"" + node.prop.propFile + "\",\n"
                        + "\"Select files from the list\", \"\", 2,\n"
                        + "\"Select All\",\"Installs All Files.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).prop.description + "\", 0";
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
                break;
            case Types.GROUP_DELETE_FILES:
                str += "\ncheckbox(\"" + node.prop.title + " List\",\"Select from " + node.prop.title + "\",\"@apps\",\"" + node.prop.propFile + "\",\n"
                        + "\"Select files from the list\", \"\", 2,\n"
                        + "\"Select All\",\"Delete All.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((DeleteNode) node.getChildAt(i)).prop.description + "\", 0";
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop.propFile + "\",readtmpfile(\"" + node.prop.propFile + "\"));\n";
                break;
            case Types.GROUP_CUSTOM:
                if (!node.isCheckBox()) {
                    break;
                }
                str += configCustomGroup(node, node.isSelectBox());
                break;
        }
        return str;
    }

    public String addCheckViewBox(String type) {
        String str = "";
        switch (type) {
            case "dalvik-cache":
                str = "\ncheckviewbox(\n"
                        + "\"Ready to Install\",\n"
                        + "    \"The wizard is ready to begin installation.\\n\\n\"+\n"
                        + "	\"Press <b>Next</b> to begin the installation.\\n\\n\"+\n"
                        + "	\"If you want to review or change any of your installation settings, press <b>Back</b>. Press Left Hard Button -> Quit Installation to exit the wizard.\\n\\n\\n\\n\\n\\n\\n\",\n"
                        + "    \"@alert\",\n"
                        + "\"<b>Clear Dalvik Cache</b> After Installation.\",\n"
                        + "\"0\",\n"
                        + "\"clear_it\");\n"
                        + "writetmpfile(\"dalvik_choices.prop\",\"init=no\\n\");\n"
                        + "if\n"
                        + "  getvar(\"clear_it\")==\"1\"\n"
                        + "then\n"
                        + "  writetmpfile(\"dalvik_choices.prop\",\"true=yes\");\n"
                        + "endif;\n";
                break;
            case "addon.d":
                str = "\ncheckviewbox(\n"
                        + "\"Backup your choices!\",\n"
                        + "    \"Check this to take backup of all the system files your have selected, "
                        + "checking this will generate addon.d script to backup all your choices.\\n\\n\"+\n"
                        + "	\"Press <b>Next</b> to Proceed ahead.\\n\\n\"+\n"
                        + "	\"If you want to review or change any of your installation settings, press <b>Back</b>. Press Left Hard Button -> Quit Installation to exit the wizard.\\n\\n\\n\\n\\n\\n\\n\",\n"
                        + "    \"@alert\",\n"
                        + "\"<b>Generate Addon.d Script</b> After Installation.\",\n"
                        + "\"" + (Preference.pp.enableAddonDSupport ? "1" : "0") + "\",\n"
                        + "\"addond\");\n"
                        + "writetmpfile(\"addond_choices.prop\",\"init=no\\n\");\n"
                        + "if\n"
                        + "  getvar(\"addond\")==\"1\"\n"
                        + "then\n"
                        + "  writetmpfile(\"addond_choices.prop\",\"true=yes\");\n"
                        + "endif;\n";
                break;
        }

        return str;
    }

    public String setNextText(String text) {
        return "ini_set(\"text_next\", \"" + text + "\");\n";
    }

    public String addInstallString() {
        return "setvar(\"retstatus\",install(\"Installing\", \"Your selected files are being installed. Please Wait...\", \"@install\"));\n";
    }

    public String addRebootString() {
        String str = "";
        str += "\ncheckviewbox(\n"
                + "    \"Process Completed\",\n"
                + "  \n"
                + "    \"<#selectbg_g><b>Congratulations...</b></#>\\n\\n\"+\n"
                + "    \"All options you choose has been installed into your device.\\n\\n\"+\n"
                + "    \"Installer Status: \"+getvar(\"retstatus\")+\"\\n\\n\",\n"
                + "	\n"
                + "    \"@welcome\",\n"
                + "\n"
                + "    \"Reboot your device now.\",\n"
                + "\n"
                + "    \"1\",\n"
                + "\n"
                + "    \"reboot_it\"\n"
                + ");\n"
                + "\n"
                + "if\n"
                + "  getvar(\"reboot_it\")==\"1\"\n"
                + "then\n"
                + "  reboot(\"onfinish\");\n"
                + "endif;";
        return str;
    }
}
