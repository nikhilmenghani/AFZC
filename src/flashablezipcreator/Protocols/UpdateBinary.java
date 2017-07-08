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
import flashablezipcreator.UserInterface.Preference;
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
        Logs.write("Building update-binary-installer");
        updateBinaryInstaller = "";
        to = new TreeOperations();
        updateBinaryInstaller += ubo.initiateUpdaterScript();
        if (Preference.pp.enableAddonDSupport || Preference.pp.displayAddonDSupport) {
            updateBinaryInstaller += ubo.deleteAddonBackupData();
            updateBinaryInstaller += ubo.getAfzcBinaryString();
        }
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
            if (((ProjectNode) project).prop.createZip) {
                switch (((ProjectNode) project).prop.projectType) {
                    case Types.PROJECT_AROMA:
                    case Types.PROJECT_CUSTOM:
                    case Types.PROJECT_MOD:
                        if (Preference.pp.createZipType.equals("Aroma")) {
                            updateBinaryInstaller += buildAromaScript((ProjectNode) project);
                        } else if (Preference.pp.createZipType.equals("Normal")) {
                            updateBinaryInstaller += buildNormalScript((ProjectNode) project);
                        }
                        break;
                }
            }
        }
        if (Preference.pp.enableAddonDSupport || Preference.pp.displayAddonDSupport) {
            if (Preference.pp.createZipType.equals("Aroma")) {
                updateBinaryInstaller += ubo.addAromaAddonDString();
            } else if (Preference.pp.createZipType.equals("Normal")) {
                updateBinaryInstaller += ubo.addNormalAddonDString();
            }
        }
        if (Preference.pp.createZipType.equals("Aroma")) {
            updateBinaryInstaller += ubo.addWipeDalvikCacheString();
        }
        updateBinaryInstaller += ubo.addPrintString("" + (Preference.pp.createZipType.equals("Normal") ? "" : "@") + "Finished Install");
        return updateBinaryInstaller;
    }

    public static String buildNormalScript(ProjectNode project) {
        String str = "";
        str += ubo.getMountMethod(1);
        str += "set_progress 0\n";
        for (ProjectItemNode group : to.getNodeList(Types.NODE_GROUP)) {
            if (((ProjectNode) group.prop.parent).prop.projectType == project.prop.projectType && ((ProjectNode) group.prop.parent).prop.title.equals(project.prop.title)) {
                str += ubo.generateUpdaterScript((GroupNode) group);
            }
        }
        str += "set_progress 1\n";
        return str;
    }

    public static String buildAromaScript(ProjectNode project) {
        String str = "";
        str += "if [ $(file_getprop \"/tmp/aroma/" + project.prop.title + ".prop\" selected) == 1 ]; then\n";
        str += ubo.getMountMethod(1);
        str += "set_progress 0\n";
        for (ProjectItemNode group : to.getNodeList(Types.NODE_GROUP)) {
            if (((ProjectNode) group.prop.parent).prop.projectType == project.prop.projectType && ((ProjectNode) group.prop.parent).prop.title.equals(project.prop.title)) {
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
