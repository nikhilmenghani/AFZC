/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.io.File;

/**
 *
 * @author Nikhil
 */
public class SubGroupNode extends ProjectItemNode {

    String parentNodeName;
    String subGroupName;
    public String location = "";
    public String permission = "";
    public int subGroupType;
    public String description = "";
    public String extension = "";
    public boolean isBootAnimationGroup = false;
    public String projectName;
    public String originalGroupType;
    String zipPathPrefix = "SubGroup_";

    public static final int TYPE_SYSTEM_FONTS = GroupNode.GROUP_SYSTEM_FONTS;
    public static final int TYPE_SYSTEM_MEDIA = GroupNode.GROUP_SYSTEM_MEDIA;
    public static final int TYPE_DATA_LOCAL = GroupNode.GROUP_DATA_LOCAL;
    public static final int TYPE_CUSTOM = GroupNode.GROUP_CUSTOM;

    public SubGroupNode(String title, int type, GroupNode parent) {
        super(title, ProjectItemNode.NODE_SUBGROUP, parent);
        super.path = parent.path + File.separator + title;
        subGroupName = title;
        this.subGroupType = type;
        this.projectName = parent.projectName;
        this.originalGroupType = parent.originalGroupType;
        super.location = parent.location;
        //System.out.println("SubGroup Path is : " + path);
        switch (type) {
            case TYPE_SYSTEM_FONTS:
                this.extension = "ttf";
                break;
            case TYPE_SYSTEM_MEDIA:
                this.extension = "zip";
                this.isBootAnimationGroup = true;
                break;
            case TYPE_DATA_LOCAL:
                this.extension = "zip";
                this.isBootAnimationGroup = true;
                break;
//            case TYPE_KERNEL:
            case TYPE_CUSTOM:
                //location and permissions shall remain null and let file node override this property.
                break;
        }
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + title;
        this.permission = parent.permission;
    }

    public String getLocation() {
        return location;
    }

    public String getPermissions() {
        return permission;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
    
    public void updateZipPath(){
        super.zipPath = parent.zipPath + "/" + zipPathPrefix + title;
    }

    public void updateChildrenZipPath(){
        for (ProjectItemNode node : children) {
            switch (node.type) {
                case ProjectItemNode.NODE_FOLDER:
                    ((FolderNode)node).updateZipPath();
                    ((FolderNode)node).updateChildrenZipPath();
                    break;
                case ProjectItemNode.NODE_FILE:
                    ((FileNode) node).updateZipPath();
                    break;
            }
        }
    }
    
    @Override
    public void updateChildrenPath() {
        super.updateChildrenPath();
    }

    public void renameMe(String newName) {
        super.setTitle(newName);
        this.subGroupName = newName;
        super.path = parent.path + File.separator + newName;
        super.zipPath = parent.zipPath + "/" + this.zipPathPrefix + title;
        this.updateChildrenPath();
        this.updateChildrenZipPath();
    }
}
