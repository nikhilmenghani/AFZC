/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Operations.TreeOperations;
import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class Project {

    public static int romCount = 0;
    public static int gappsCount = 0;
    public static int aromaCount = 0;
    public static int normalCount = 0;
    public static String outputPath = "Output.zip";
    public static String romName = "I Don't Know Rom";
    public static String romVersion = "I Don't Know Version";
    public static String romAuthor = "I Don't Know Developer";
    public static String romDevice = "I Don't Know Device";
    public static String romDate = "I Don't Know Date";
    public static String gappsName = "I Don't Know Name";
    public static String androidVersion = "I Don't Know Version";
    public static String gappsType = "I Don't Know Type";
    public static String gappsDate = "I Don't Know Date";
    public static String releaseVersion = "I Don't Know Version";
    public static String zipCreator = "Nikhil";

    static TreeOperations to;

    public static ArrayList<String> getTempFilesList(){
        ArrayList<String> tempArray = new ArrayList<>();
        tempArray.add("system/app/afzc_temp");
        tempArray.add("system/priv-app/afzc_temp");
        tempArray.add("system/media/audio/ui/afzc_temp");
        tempArray.add("system/media/audio/notifications/afzc_temp");
        tempArray.add("system/media/audio/alarms/afzc_temp");
        tempArray.add("system/media/audio/ringtones/afzc_temp");
        tempArray.add("data/local/afzc_temp");
        tempArray.add("data/app/afzc_temp");
        tempArray.add("preload/symlink/system/app/afzc_temp");
        return tempArray;
    }
    
    public static void countProjects(ProjectItemNode rootNode) {
        romCount = 0;
        gappsCount = 0;
        aromaCount = 0;
        normalCount = 0;
        to = new TreeOperations(rootNode);
        for (ProjectItemNode project : to.getNodeList(ProjectItemNode.NODE_PROJECT)) {
            switch (((ProjectNode) project).projectType) {
                case ProjectNode.PROJECT_ROM:
                    romCount++;
                    break;
                case ProjectNode.PROJECT_GAPPS:
                    gappsCount++;
                    break;
                case ProjectNode.PROJECT_AROMA:
                    aromaCount++;
                    break;
                case ProjectNode.PROJECT_NORMAL:
                    normalCount++;
                    break;
            }
        }
    }

    public static int returnMainProject(ProjectItemNode rootNode) {
        countProjects(rootNode);
        if (romCount > 0) {
            return ProjectNode.PROJECT_ROM;
        } else if (gappsCount > 0) {
            return ProjectNode.PROJECT_GAPPS;
        } else if (aromaCount > 0) {
            return ProjectNode.PROJECT_AROMA;
        } else if (normalCount > 0) {
            return ProjectNode.PROJECT_NORMAL;
        }
        return -1;
    }
}
