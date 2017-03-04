/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Project;
import flashablezipcreator.UserInterface.Preferences;
import java.io.File;

/**
 *
 * @author Nikhil
 */
public class ProjectNode extends ProjectItemNode {

    public int projectType;
    public String projectName = "";
    public boolean createZip = true; //when multiple projects will be loaded, this will help in choosing which one to create zip of.
    public String updater_script = "";
    public String aroma_config = "";
    public byte[] update_binary = null;
    public byte[] update_binary_installer = null;
//    public String romName = Project.romName;
//    public String romVersion = Project.romVersion;
//    public String romAuthor = Project.romAuthor;
//    public String romDevice = Project.romDevice;
//    public String romDate = Project.romDate;
//    public String gappsName = Project.gappsName;
    public String androidVersion = Project.androidVersion;
//    public String gappsType = Project.gappsType;
//    public String gappsDate = Project.gappsDate;
    public String releaseVersion = Project.releaseVersion;
    String zipPathPrefix = "Project_";

    //public static final int PROJECT_NORMAL = 1;
    public static final int PROJECT_AROMA = 2;
    public static final int PROJECT_CUSTOM = 3;
    public static final int PROJECT_MOD = 4;
    public static final int PROJECT_THEMES = 5;
    //public static final int PROJECT_ADVANCED = 6;

    public ProjectNode(String title, int projectType, ProjectItemNode parent) {
        super(title, ProjectItemNode.NODE_PROJECT, parent);
        this.projectName = title;
        this.projectType = projectType;
        super.path = parent + File.separator + title;
        super.zipPath = parent.zipPath + "/" + zipPathPrefix + title;
        this.androidVersion = Preferences.IsFromLollipop ? "5.x+" : "4.x+";
    }

    public void renameMe(String newName) {
        super.setTitle(newName);
        this.projectName = newName;
        super.path = parent.path + File.separator + newName;
        super.zipPath = parent.zipPath + "/" + zipPathPrefix + newName;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public void updateChildrenPath() {
        super.updateChildrenPath();
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : children) {
            ((GroupNode) node).updateZipPath(); //casting to group node as project node cannot have child of any other type.
            ((GroupNode) node).updateChildrenZipPath();
        }
    }
}
