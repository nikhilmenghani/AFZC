/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public final class GroupNode extends ProjectItemNode {

    public GroupNode(NodeProperties properties) {
        super(properties);
    }
    
    public boolean isSelectBox() {
        return prop.isSelectBox;
    }

    public boolean isCheckBox() {
        return !prop.isSelectBox;
    }

    public void setLocation(String location) {
        prop.location = location;
    }

    public String getLocation() {
        return prop.location;
    }

    public void updateZipPath() {
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.originalGroupType + "/" + prop.groupZipPathPrefix + prop.title;
    }

    public void renameMe(String newName) throws IOException {
        prop.title = newName;
        prop.groupName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.originalGroupType + "/" + prop.groupZipPathPrefix + newName;
        prop.reloadZipPath(newName);
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }

    public void updateChildrenZipPath() {
        for (ProjectItemNode node : prop.children) {
            switch (node.prop.type) {
                case Types.NODE_SUBGROUP:
                    ((SubGroupNode) node).updateZipPath();
                    ((SubGroupNode) node).updateChildrenZipPath();
                    break;
                case Types.NODE_FOLDER:
                    ((FolderNode) node).updateZipPath();
                    ((FolderNode) node).updateChildrenZipPath();
                    break;
                case Types.NODE_FILE:
                    ((FileNode) node).prop.updateFileZipPath();
                    break;
            }
        }
    }

    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
    }

    public String getProp(String str) {
        return str + "_" + prop.groupName.replaceAll(" ", "_") + "_" + prop.parent.prop.title.replaceAll(" ", "_") + ".prop";
    }
}
