/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.FlashableZipCreator;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.TreeOperations;
import static flashablezipcreator.Protocols.Import.to;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class Jar {

    public static void addThemesToTree(ProjectItemNode rootNode, DefaultTreeModel model) throws IOException {
        to = new TreeOperations(rootNode);
        String projectName = "Themes";
        int projectType = ProjectNode.PROJECT_THEMES;
        if (to.getProjectNode(projectName, projectType) == null) {
            to.addChildTo(rootNode, projectName, projectType, model);
        }
        JarOperations.setJarFileList();
        for (String theme : JarOperations.themesList) {
            to.addChildTo(to.getProjectNode(projectName, projectType), theme, GroupNode.GROUP_AROMA_THEMES, model);
            String themePath = "META-INF/com/google/android/aroma/themes/" + theme + "/";
            for (String themesPath : JarOperations.themesFileList) {
                if (themesPath.contains(themePath)) {
                    to.addChildTo(to.getGroupNode(theme, GroupNode.GROUP_AROMA_THEMES, projectName), themesPath, ProjectItemNode.NODE_FILE, model);
                }
            }
        }
    }
    
    public static ArrayList<String> getBinaryList(){
        return JarOperations.binaryList;
    }
    
    public static ArrayList<String> getOtherFileList(){
        return JarOperations.otherList;
    }
    
    public static ArrayList<String> getThemesList(){
        return JarOperations.themesList;
    }
    
    public static byte[] getNeonBinary(){
        return JarOperations.neon_binary;
    }
    
    public static byte[] getNonNeonBinary(){
        return JarOperations.non_neon_binary;
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
