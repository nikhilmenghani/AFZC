/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Operations.AromaScriptOperations;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.UserInterface.Preferences;

/**
 *
 * @author Nikhil
 */
public class AromaConfig {

    public static TreeOperations to;
    public static AromaScriptOperations op = new AromaScriptOperations();
    public static String aromaConfig = "";
    public static String aromaConfigPath = "META-INF/com/google/android/aroma-config";

    public static String build(ProjectItemNode rootNode) {
        aromaConfig = "";
        to = new TreeOperations();
        if (!Preferences.pp.aromaVersion.equals("Version 2.56 - EDELWEIS")) {
            aromaConfig += "ini_set(\"force_colorspace\", \"rgba\");\n\n";
        }
        aromaConfig += op.addSplashString();
        aromaConfig += op.addFontsString();
        aromaConfig += op.addThemesString(rootNode);
        aromaConfig += op.addAgreeBox();
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
            if (((ProjectNode) project).prop.createZip) {
                switch (((ProjectNode) project).prop.projectType) {
                    case Types.PROJECT_AROMA:
                    case Types.PROJECT_CUSTOM:
                    case Types.PROJECT_MOD:
                        aromaConfig += buildAromaScript((ProjectNode) project);
                        break;
                    //following is not needed. added just in case.
//                    case Types.PROJECT_ADVANCED:
//                        break;
                }
            }
        }
        aromaConfig += op.setNextText("Install");
        aromaConfig += op.addCheckViewBox();
        aromaConfig += op.setNextText("Finish");
        aromaConfig += op.addInstallString();
//        aromaConfig += op.setNextText("Finish");
//        aromaConfig += op.addRebootString();
        return aromaConfig;
    }

    public static String buildAromaScript(ProjectNode project) {
        String str = "";
        str += op.addMenuBox(project);
        str += "if prop(\"" + project.prop.title + ".prop\",\"selected\")==\"1\" then\n";
        str += op.addInitString(project);
        str += op.addWelcomeString(project);
        for (ProjectItemNode group : project.prop.children) {
            switch (((ProjectNode) group.prop.parent).prop.projectType) {
                case Types.PROJECT_AROMA:
                case Types.PROJECT_CUSTOM:
                case Types.PROJECT_MOD:
                    str += op.addCheckBox((GroupNode) group);
                    str += op.addSelectBox((GroupNode) group);
                    break;
            }
        }
        str += "endif;\n";
        str += "if prop(\"" + project.prop.title + ".prop\",\"selected\")==\"2\" then\n";
        str += "writetmpfile(\"" + project.prop.title + ".prop\",\"init=no\\n\");\n";
        str += "endif;\n";
        return str;
    }
}
