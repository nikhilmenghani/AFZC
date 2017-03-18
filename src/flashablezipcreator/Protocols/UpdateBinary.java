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
import flashablezipcreator.Operations.UpdateBinaryOperations;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class UpdateBinary {
    public static String updateBinaryInstaller = "";
    public static TreeOperations to;
    public static UpdateBinaryOperations ubo = new UpdateBinaryOperations();
    public static String build(ProjectItemNode rootNode) throws FileNotFoundException, IOException {
        updateBinaryInstaller = "";
        to = new TreeOperations();
        updateBinaryInstaller += ubo.initiateUpdaterScript();
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
            if (((ProjectNode) project).createZip) {
                switch (((ProjectNode) project).projectType) {
                    case ProjectNode.PROJECT_AROMA:
                    case ProjectNode.PROJECT_CUSTOM:
                    case ProjectNode.PROJECT_MOD:
                        updateBinaryInstaller += buildAromaScript((ProjectNode) project);
                        break;
                }
            }
        }
        updateBinaryInstaller += ubo.addWipeDalvikCacheString();
        updateBinaryInstaller += ubo.addPrintString("@Finished Install");
        return updateBinaryInstaller;
    }

    public static String buildNormalScript(ProjectNode project) {
        String str = "";
        return str;
    }

    public static String buildAromaScript(ProjectNode project) {
        String str = "";
        str += "if [ $(file_getprop \"/tmp/aroma/" + project.title + ".prop\" selected) == 1 ]; then\n";
        str += ubo.getMountMethod(1);
        str += "set_progress 0\n";
        for (ProjectItemNode group : to.getNodeList(ProjectItemNode.NODE_GROUP)) {
            if (((ProjectNode) group.parent).projectType == project.projectType && ((ProjectNode) group.parent).title.equals(project.title)) {
                str += ubo.generateUpdaterScript((GroupNode) group);
            }
        }
        str += "set_progress 1\n";
        //str += ubo.terminateUpdaterScript();
        return str += "fi;\n";
    }

    public static String getDpiScript(String dpi) {
        return ubo.getDpiScript(dpi);
    }
}