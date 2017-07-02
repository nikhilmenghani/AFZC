/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.FlashableZipCreator;
import flashablezipcreator.UserInterface.MyTree;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.UserInterface.Preference;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class Jar {

    //ProjectItemNode rootNode, DefaultTreeModel model 
    public static void addThemesToTree() throws IOException {
        ProjectItemNode rootNode = MyTree.rootNode;
        TreeOperations to = new TreeOperations();
        String projectName = "Themes";
        int projectType = Types.PROJECT_THEMES;
        try {
            ProjectNode themesProject = (ProjectNode) rootNode.addChild(new ProjectNode(projectName, projectType, Mod.MOD_LESS, rootNode), true);
            themesProject.prop.children = new ArrayList<>();
            for (String theme : JarOperations.themesList) {
                Logs.write(theme);
                if (Preference.pp.themes.contains(theme)) {
                    NodeProperties np = new NodeProperties(theme, Types.GROUP_AROMA_THEMES, themesProject);
                    GroupNode themeGroup = (GroupNode) themesProject.addChild(new GroupNode(np), true);
                    String themePath = "META-INF/com/google/android/aroma/themes/" + theme + "/";
                    for (String themesPath : JarOperations.themesFileList) {
                        if (themesPath.contains(themePath)) {
                            themeGroup.addChild(new FileNode(themesPath, themeGroup), true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logs.write("Exception occurred while adding themes to tree");
            Logs.write(Logs.getExceptionTrace(e));
        }
    }

    public static ArrayList<String> getBinaryList() {
        return JarOperations.binaryList;
    }

    public static ArrayList<String> getOtherFileList() {
        return JarOperations.otherList;
    }

    public static ArrayList<String> getThemesList() {
        return JarOperations.themesList;
    }

    public static byte[] getAromaBinary() {
        Logs.write("Aroma Binary Selected: " + Preference.pp.aromaVersion);
        switch (Preference.pp.aromaVersion) {
            case "Version 3.00b1 - MELATI":
                return JarOperations.binary_MELATI;
            case "Version 2.70 RC2 - FLAMBOYAN":
                return JarOperations.binary_FLAMBOYAN;
            case "Version 2.56 - EDELWEIS":
                return JarOperations.binary_EDELWEIS;
        }
        return JarOperations.binary_MELATI;
    }

    public static boolean isExecutingThrough() {
        return FlashableZipCreator.class.getResource("FlashableZipCreator.class").getPath().contains("!");
    }

    public static String getFileName() {
        String path[] = FlashableZipCreator.class.getResource("FlashableZipCreator.class").getPath().split("!");
        String fileName = path[0].substring(path[0].lastIndexOf("/") + 1, path[0].length());
        return fileName;
    }
}
