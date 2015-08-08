/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Protocols.Project;
import javax.swing.JOptionPane;

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
        int i = 1;
        for (ProjectItemNode projectNode : rootNode.children) {
            if (((ProjectNode) projectNode).projectType == ProjectNode.PROJECT_THEMES) {
                for (ProjectItemNode theme : ((ProjectNode) projectNode).children) {
                    str += "\"" + theme.title + "\", \"\", " + i + ",\n";
                    i = 0;
                }
            }
        }
        str = str.substring(0, str.length() - 2);
        str += ");\n\n";
        i = 1;
        for (ProjectItemNode projectNode : rootNode.children) {
            if (((ProjectNode) projectNode).projectType == ProjectNode.PROJECT_THEMES) {
                for (ProjectItemNode theme : ((ProjectNode) projectNode).children) {
                    str += "if prop(\"theme.prop\", \"selected.0\")==\"" + i++ + "\" then\n"
                            + "theme(\"" + theme.title + "\");\n"
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
        switch (node.groupType) {
            case GroupNode.GROUP_DATA_LOCAL:
            case GroupNode.GROUP_SYSTEM_MEDIA:
            case GroupNode.GROUP_AROMA_KERNEL:
            case GroupNode.GROUP_SCRIPT:
                str += "\nselectbox(\"" + node.title + " List\",\"Select from " + node.title + "\",\"@personalize\",\"" + node.prop + "\",\n"
                        + "\"Select one from the list\", \"\", 2,\n"
                        + "\"Select None\",\"Skip this Group.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {

                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).description + "\", 0";
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop + "\",readtmpfile(\"" + node.prop + "\"));\n";
                break;
            case GroupNode.GROUP_CUSTOM:
                if (!node.isSelectBox()) {
                    break;
                }
                str += configCustomGroup(node, node.isSelectBox());
            case GroupNode.GROUP_SYSTEM_FONTS:
                str += configFonts(node);
                break;
        }
        return str;
    }

    public String configCustomGroup(GroupNode node, boolean flag) {
        String str = "";
        if (flag) {//flag is to separate selectbox and checkbox.
            str += "\nselectbox(\"" + node.title + " List\",\"Select from " + node.title + "\",\"@personalize\",\"" + node.prop + "\",\n"
                    + "\"Select one from the list\", \"\", 2,\n"
                    + "\"Select None\",\"Skip this Group.\", 1";
            for (int i = 0; i < node.getChildCount(); i++) {
                try {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).description + "\", 0";
                } catch (ClassCastException cce) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).description + "\", 0";
                }
            }
            str += ");\n";
            str += "writetmpfile(\"" + node.prop + "\",readtmpfile(\"" + node.prop + "\"));\n";
        } else {
            str += "\ncheckbox(\"" + node.title + " List\",\"Select from " + node.title + "\",\"@apps\",\"" + node.prop + "\",\n"
                    + "\"Select files from the list\", \"\", 2,\n"
                    + "\"Select All\",\"Installs All Files.\", 1";
            for (int i = 0; i < node.getChildCount(); i++) {
                try {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).description + "\", 0";
                } catch (ClassCastException cce) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).description + "\", 0";
                }
            }
            str += ");\n";
            str += "writetmpfile(\"" + node.prop + "\",readtmpfile(\"" + node.prop + "\"));\n";
        }
        return str;
    }

    public String configFonts(GroupNode node) {
        String str = "";
        str += "\nselectbox(\"" + node.title + " List\",\"Select from " + node.title + " List For Preview\",\"@info\",\"" + node.prop + "\""
                + ",\n\"Select None\",\"Skip this Group.\", 1";
        for (int i = 0; i < node.getChildCount(); i++) {
            str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((SubGroupNode) node.getChildAt(i)).description + "\", " + 0 + "";
        }
        str += ");\n";
        str += "writetmpfile(\"" + node.prop.replace(".prop", "_temp.prop") + "\",\"init=no\\n\");\n";//initialize temp.prop.
        
        str += "if prop(\"" + node.prop + "\", \"selected.0\")==\"" + 1 + "\" then\n";
        str += "setvar(\"fontname\",\"" + "default" + "\");\n";
        str += "endif;\n";
        
        if (node.groupType == GroupNode.GROUP_SYSTEM_FONTS) {
            for (int i = 0; i < node.getChildCount(); i++) {
                SubGroupNode sgnode = (SubGroupNode) node.getChildAt(i);
                str += "if prop(\"" + node.prop + "\", \"selected.0\")==\"" + (i + 2) + "\" then\n";
                for (int j = 0; j < sgnode.getChildCount(); j++) {
                    FileNode fnode = (FileNode) sgnode.getChildAt(j);
                    if (((FileNode) fnode).title.equals("DroidSans.ttf")) {
                        str += "fontresload(\"0\", \"ttf/" + sgnode.toString() + ".ttf;\", \"15\");\n";
                    }
                }
                str += "setvar(\"fontname\",\"" + sgnode.toString() + "\");\n";
                str += "writetmpfile(\"" + node.prop.replace(".prop", "_temp.prop") + "\",\"" + sgnode + "=yes\\n\");\n";
                str += "endif;\n";
            }
            str += addFontsViewBoxString();
            str += addFontsString();
        }
        return str;
    }

    public String addMenuBox(ProjectNode project) {
        String str = "";
        switch (project.projectType) {
            case ProjectNode.PROJECT_GAPPS:
                str += "\nmenubox(\"" + "Menu" + " List\",\"Select from " + "following" + "\",\"@installgapps\",\"" + project.title + ".prop" + "\",\n"
                        + "\"Install\", \"Install Gapps\", \"@install\"";
                str += ",\n\"" + "Skip" + "\", \"Do Not Install Gapps\", \"@exit\"";
                str += ");\n";
                str += "writetmpfile(\"" + project.title + ".prop" + "\",readtmpfile(\"" + project.title + ".prop" + "\"));\n";
                //str += "writetmpfile(\"" + project.title + ".prop" + "\",\"true=yes\");\n";
                break;
            case ProjectNode.PROJECT_ROM:
                str += "\nmenubox(\"" + "Menu" + " List\",\"Select from " + "following" + "\",\"@installrom\",\"" + project.title + ".prop" + "\",\n"
                        + "\"Install\", \"Install Rom\", \"@install\"";
                str += ",\n\"" + "Skip" + "\", \"Do Not Install Rom\", \"@apps\"";
                str += ");\n";
                str += "writetmpfile(\"" + project.title + ".prop" + "\",readtmpfile(\"" + project.title + ".prop" + "\"));\n";
                break;
            case ProjectNode.PROJECT_AROMA:
                str += "\nmenubox(\"" + "Menu" + " List\",\"Select from " + "following" + "\",\"@installmods\",\"" + project.title + ".prop" + "\",\n"
                        + "\"Install\", \"Install Mods\", \"@install\"";
                str += ",\n\"" + "Skip" + "\", \"Do Not Install Mods\", \"@apps\"";
                str += ");\n";
                str += "writetmpfile(\"" + project.title + ".prop" + "\",readtmpfile(\"" + project.title + ".prop" + "\"));\n";
                break;
        }
        return str;
    }

    public String addInitString(ProjectNode project) {
        String str = "";
        switch (project.projectType) {
            case ProjectNode.PROJECT_ROM:
                str += "ini_set(\"rom_name\",\"" + project.romName + "\");\n"
                        + "ini_set(\"rom_version\",\"" + project.romVersion + "\");\n"
                        + "ini_set(\"rom_author\",\"" + project.romAuthor + "\");\n"
                        + "ini_set(\"rom_device\",\"" + project.romDevice + "\");\n"
                        + "ini_set(\"rom_date\",\"" + project.romDate + "\");\n"
                        + "setvar(\"file_creator\",\"" + Project.zipCreator + "\");\n";
                break;
            case ProjectNode.PROJECT_GAPPS:
                str += "setvar(\"gapps_name\",\"" + project.gappsName + "\");\n"
                        + "setvar(\"android_version\",\"" + project.androidVersion + "\");\n"
                        + "setvar(\"gapps_type\",\"" + project.gappsType + "\");\n"
                        + "setvar(\"gapps_date\",\"" + project.gappsDate + "\");\n"
                        + "setvar(\"file_creator\",\"" + Project.zipCreator + "\");\n";
                break;
            case ProjectNode.PROJECT_AROMA:
                str += "setvar(\"release_version\",\"" + project.releaseVersion + "\");\n"
                        + "setvar(\"android_version\",\"" + project.androidVersion + "\");\n"
                        + "setvar(\"file_creator\",\"" + Project.zipCreator + "\");\n";
                break;
        }
        return str;
    }

    public String addWelcomeString(ProjectNode project) {
        String str = "";
        switch (project.projectType) {
            case ProjectNode.PROJECT_ROM:
                str += "\nviewbox(\n"
                        + "\"Welcome\",\n"
                        + "\"You are about to install <b>\"+\n"
                        + "ini_get(\"rom_name\")+\n"
                        + "\"</b> for <b>\"+ini_get(\"rom_device\")+\"</b>.\\n\\n\"+\n"
                        + "\n"
                        + "\"Version\\t        :\\t <b><#selectbg_g>\"+ini_get(\"rom_version\")+\"</#></b>\\n\"+\n"
                        + "\"Developed By\\t   :\\t <b><#selectbg_g>\"+ini_get(\"rom_author\")+\"</#></b>\\n\"+\n"
                        + "\"Update Date\\t    :\\t <b><#selectbg_g>\"+ini_get(\"rom_date\")+\"</#></b>\\n\\n\\n\"+\n"
                        + "\"File Created By\\t:\\t <b><#selectbg_g>\"+getvar(\"file_creator\")+\"</#></b>\\n\\n\\n\"+\n"
                        + "\n"
                        + "\"Press Next to Continue\",\n"
                        + "\"@welcome\"\n"
                        + ");\n";
                break;
            case ProjectNode.PROJECT_GAPPS:
                str += "\nviewbox(\n"
                        + "\"Welcome\",\n"
                        + "\"You are about to install <b>\"+\n"
                        + "getvar(\"gapps_name\")+\n"
                        + "\"</b> for <b>Your Device</b>.\\n\\n\"+"
                        + "\n"
                        + "\"Android Version\\t:\\t <b><#selectbg_g>\"+getvar(\"android_version\")+\"</#></b>\\n\"+\n"
                        + "\"Gapps Type\\t        :\\t <b><#selectbg_g>\"+getvar(\"gapps_type\")+\"</#></b>\\n\"+\n"
                        + "\"Gapps Date\\t        :\\t <b><#selectbg_g>\"+getvar(\"gapps_date\")+\"</#></b>\\n\\n\\n\"+\n"
                        + "\"File Created By\\t:\\t <b><#selectbg_g>\"+getvar(\"file_creator\")+\"</#></b>\\n\\n\\n\"+\n"
                        + "\n"
                        + "\"Press Next to Continue\",\n"
                        + "\"@welcome\"\n"
                        + ");\n";
                break;
            case ProjectNode.PROJECT_AROMA:
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
        switch (node.groupType) {
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
                str += "\ncheckbox(\"" + node.title + " List\",\"Select from " + node.title + "\",\"@apps\",\"" + node.prop + "\",\n"
                        + "\"Select files from the list\", \"\", 2,\n"
                        + "\"Select All\",\"Installs All Files.\", 1";
                for (int i = 0; i < node.getChildCount(); i++) {
                    str += ",\n\"" + node.getChildAt(i).toString() + "\", \"" + ((FileNode) node.getChildAt(i)).description + "\", 0";
                }
                str += ");\n";
                str += "writetmpfile(\"" + node.prop + "\",readtmpfile(\"" + node.prop + "\"));\n";
                break;
            case GroupNode.GROUP_CUSTOM:
                if (!node.isCheckBox()) {
                    break;
                }
                str += configCustomGroup(node, node.isSelectBox());
                break;
        }
        return str;
    }

    public String addCheckViewBox() {
        String str = "\ncheckviewbox(\n"
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
