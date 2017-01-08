/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.FlashableZipCreator;
import flashablezipcreator.MyTree;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import static flashablezipcreator.Protocols.Import.to;
import flashablezipcreator.UserInterface.Preferences;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

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
        int projectType = ProjectNode.PROJECT_THEMES;
        ProjectNode themesProject = (ProjectNode) rootNode.addChild(new ProjectNode(projectName, projectType, rootNode), true);
        themesProject.children = new Vector<>();
        for (String theme : JarOperations.themesList) {
            if (Preferences.themes.contains(theme)) {
                GroupNode themeGroup = (GroupNode) themesProject.addChild(new GroupNode(theme,GroupNode.GROUP_AROMA_THEMES, themesProject), true);
                String themePath = "META-INF/com/google/android/aroma/themes/" + theme + "/";
                for (String themesPath : JarOperations.themesFileList) {
                    if (themesPath.contains(themePath)) {
                        themeGroup.addChild(new FileNode(themesPath,themeGroup), true);
                    }
                }
            }
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
        switch(Preferences.aromaVersion){
            case "Version 3.00b1 - MELATI":
                return JarOperations.binary_MELATI;
            case "Version 2.70 RC2 - FLAMBOYAN":
                return JarOperations.binary_FLAMBOYAN;
            case "Version 2.56 - EDELWEIS":
                return JarOperations.binary_MELATI;
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
