/*
 * To change prop license header, choose License Headers in Project Properties.
 * To change prop template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;
import java.io.File;

/**
 *
 * @author Nikhil
 */
public class SubGroupNode extends ProjectItemNode {

    public SubGroupNode(NodeProperties properties){
        super(properties);
    }
    
    public SubGroupNode(String title, int type, GroupNode parent) {
        super(title, Types.NODE_SUBGROUP, parent);
        prop.originalParent = parent;
        prop.path = parent.prop.path + File.separator + title;
        prop.subGroupName = title;
        prop.subGroupType = type;
        prop.projectName = parent.prop.projectName;
        prop.originalGroupType = parent.prop.originalGroupType;
        prop.location = parent.prop.location;
        switch (type) {
            case Types.GROUP_SYSTEM_FONTS:
                prop.extension = "ttf";
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                prop.extension = "zip";
                prop.isBootAnimationGroup = true;
                break;
            case Types.GROUP_DATA_LOCAL:
                prop.extension = "zip";
                prop.isBootAnimationGroup = true;
                break;
        }
        prop.zipPath = parent.prop.zipPath + "/" + prop.subGroupZipPathPrefix + title;
        prop.permission = parent.prop.permission;
        prop.setPermissions = parent.prop.setPermissions;
    }

    public String getLocation() {
        return prop.location;
    }

    public String getPermissions() {
        return prop.permission;
    }

    public void setDescription(String desc) {
        prop.description = desc;
    }
    
    public void updateZipPath(){
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.subGroupZipPathPrefix + prop.title;
    }

    public void updateChildrenZipPath(){
        for (ProjectItemNode node : prop.children) {
            switch (node.prop.type) {
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

    public void renameMe(String newName) {
        prop.title = newName;
        prop.subGroupName = newName;
        prop.path = prop.parent.prop.path + File.separator + newName;
        prop.zipPath = prop.parent.prop.zipPath + "/" + prop.subGroupZipPathPrefix + prop.title;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }
}
