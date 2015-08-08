/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.Operations.UpdaterScriptOperations;

/**
 *
 * @author Nikhil
 */
public class UpdaterScript {

    public static String updaterScript = "";
    public static TreeOperations to;
    public static UpdaterScriptOperations op = new UpdaterScriptOperations();
    public static String updaterScriptPath = "META-INF/com/google/android/updater-script";
    public static String symlinkScriptPath = "META-INF/com/google/android/symlink-script";
    public static String symlinkScript = op.getSymlinkScript();

    public static String build(ProjectItemNode rootNode) {
        updaterScript = "";
        to = new TreeOperations(rootNode);
        updaterScript += op.initiateUpdaterScript();
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
            if (((ProjectNode) project).createZip) {
                switch (((ProjectNode) project).projectType) {
                    case ProjectNode.PROJECT_ROM:
                        updaterScript += buildAdvancedScript((ProjectNode) project);
                        break;
                    case ProjectNode.PROJECT_GAPPS:
                        updaterScript += buildAdvancedScript((ProjectNode) project);
                        break;
                    case ProjectNode.PROJECT_AROMA:
                        updaterScript += buildAromaScript((ProjectNode) project);
                        break;
                    case ProjectNode.PROJECT_NORMAL:
                        updaterScript += buildNormalScript((ProjectNode) project);
                        break;
                    //following is not needed. added just in case.
                    case ProjectNode.PROJECT_ADVANCED:
                        break;
                }
            }
        }
        updaterScript += op.addWipeDalvikCacheString();
        updaterScript += op.addPrintString("@Finished Install");
        return updaterScript;
    }

    public static String buildNormalScript(ProjectNode project) {
        String str = "";
        return str;
    }

    public static String buildAromaScript(ProjectNode project) {
        String str = "";
        str += "if (file_getprop(\"/tmp/aroma/" + project.title + ".prop\", \"selected\")==\"1\") then\n";
        str += op.getMountMethod(1);
        str += op.getExtractDataString();
        str += "set_progress(0);\n";
        for (ProjectItemNode group : to.getNodeList(ProjectItemNode.NODE_GROUP)) {
            if (((ProjectNode) group.parent).projectType == project.projectType
                    && ((ProjectNode) group.parent).title.equals(project.title)) {
                str += op.generateUpdaterScript((GroupNode) group);
                if (((GroupNode) group).groupType == GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP) {
                    str += "package_extract_file(\"" + symlinkScriptPath + "\", \"/tmp/symlink_script\");\n"
                            + "set_perm(0, 0, 0777, \"/tmp/symlink_script\");\n"
                            + "run_program(\"/tmp/symlink_script\");\n";
                }
            }
        }
        str += "set_progress(1);\n";
        str += op.terminateUpdaterScript();
        return str += "endif;\n";
    }

    public static String buildAdvancedScript(ProjectNode project) {
        String str = "";
        switch (project.projectType) {
            case ProjectNode.PROJECT_ROM:
                str += "if (file_getprop(\"/tmp/aroma/" + project.title + ".prop\", \"selected\")==\"1\") then\n";
                str += project.updater_script;
                break;
            case ProjectNode.PROJECT_GAPPS:
                str += "if (file_getprop(\"/tmp/aroma/" + project.title + ".prop\", \"selected\")==\"1\") then\n";
                str += project.updater_script;
                break;
        }
        str += op.getMountMethod(1);
        str += "set_progress(0);\n";
        for (ProjectItemNode group : to.getNodeList(ProjectItemNode.NODE_GROUP)) {
            if (((ProjectNode) group.parent).projectType == project.projectType
                    && ((ProjectNode) group.parent).title.equals(project.title)) {
                str += op.generateUpdaterScript((GroupNode) group);
                if (((GroupNode) group).groupType == GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP) {
                    str += "package_extract_file(\"" + symlinkScriptPath + "\", \"/tmp/symlink_script\");\n"
                            + "set_perm(0, 0, 0777, \"/tmp/symlink_script\");\n"
                            + "run_program(\"/tmp/symlink_script\");\n";
                }
            }
        }
        if (project.projectType == ProjectNode.PROJECT_ROM) {
            str += op.addPrintString("Deleting temporary files..");
            str += op.deleteTempFiles();
        }
        str += "set_progress(1);\n";
        str += op.terminateUpdaterScript();
        return str += "endif;\n";
    }
    
    public static String getDpiScript(String dpi){
        return op.getDpiScript(dpi);
    }
}
