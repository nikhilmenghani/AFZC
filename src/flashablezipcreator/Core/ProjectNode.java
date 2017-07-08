/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preference;
import java.io.File;

/**
 *
 * @author Nikhil
 */
public class ProjectNode extends ProjectItemNode {

    public ProjectNode(NodeProperties properties){
        super(properties);
    }
    
    public ProjectNode(String title, int projectType, int modType, ProjectItemNode parent) {
        super(title, Types.NODE_PROJECT, parent);
        Logs.write("adding project now");
        prop.projectName = title;
        prop.projectType = projectType;
        prop.path = parent + File.separator + title;
        prop.modType = modType;
        switch (projectType) {
            case Types.PROJECT_AROMA:
                prop.zipPath = "customize" + "/" + "aroma_" + modType + "/" + prop.projectZipPathPrefix + title;
                break;
            case Types.PROJECT_CUSTOM:
                prop.zipPath = "customize" + "/" + "custom_" + modType + "/" + prop.projectZipPathPrefix + title;
                break;
            case Types.PROJECT_MOD:
                prop.zipPath = "customize" + "/" + "mod_" + modType + "/" + prop.projectZipPathPrefix + title;
                break;
        }
        prop.androidVersion = "5.x+";
        Logs.write("done adding project");
    }

    public void renameMe(String newName) {
        super.setTitle(newName);
        prop.projectName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        switch (prop.projectType) {
            case Types.PROJECT_AROMA:
                prop.zipPath = "customize" + "/" + "aroma_" + prop.modType + "/" + prop.projectZipPathPrefix + prop.title;
                break;
            case Types.PROJECT_CUSTOM:
                prop.zipPath = "customize" + "/" + "custom_" + prop.modType + "/" + prop.projectZipPathPrefix + prop.title;
                break;
            case Types.PROJECT_MOD:
                prop.zipPath = "customize" + "/" + "mod_" + prop.modType + "/" + prop.projectZipPathPrefix + prop.title;
                break;
        }
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public void updateChildrenPath() {
        super.updateChildrenPath();
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : prop.children) {
            ((GroupNode) node).updateZipPath(); //casting to group node as project node cannot have child of any other type.
            ((GroupNode) node).updateChildrenZipPath();
        }
    }
}
