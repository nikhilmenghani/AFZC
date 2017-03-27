/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Project;
import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class NodeProperties {
    public String title;
    public String path;
    public String zipPath;
    public String location = "";
    public String groupName;
    public String subGroupName;
    public String permission = "";
    public String groupPermission = "";
    public String folderPermission = "";
    public String defaultFolderPerm = "";
    public String propFile;
    public String extension;
    public String projectName;
    public String originalGroupType;
    public String updater_script = "";
    public String aroma_config = "";
    public String androidVersion = Project.androidVersion;
    public String releaseVersion = Project.releaseVersion;
    public String projectZipPathPrefix = "Project_";
    public String groupZipPathPrefix = "Group_";
    public String subGroupZipPathPrefix = "SubGroup_";
    public String folderZipPathPrefix = "Folder_";
    public String typePrefix = "Type_";
    public String folderName;
    public String folderLocation;
    public String description = "";
    public String fileName;
    public String fileSourcePath;
    public String fileInstallLocation;
    public String filePermission;
    public String fileZipPath;
    public String value;
    public int type;
    public int projectType;
    public int groupType;
    public int subGroupType;
    public boolean createZip = true; //when multiple projects will be loaded, this will help in choosing which one to create zip of.
    public boolean isBootAnimationGroup = false;
    public boolean isSelectBox = false;
    public ProjectItemNode parent;
    public ProjectItemNode originalParent;
    public GroupNode groupParent;
    public ProjectItemNode subGroupParent;
    public ProjectItemNode projectParent;
    public ProjectItemNode folderParent;
    public ArrayList<ProjectItemNode> children = new ArrayList<>();
    public byte[] update_binary = null;
    public byte[] update_binary_installer = null;
    
    public String getProp(String str) {
        return str + "_" + groupName.replaceAll(" ", "_") + "_" + parent.prop.title.replaceAll(" ", "_") + ".prop";
    }
    
    public void setPermissions(String parentPermission, String title) {
        filePermission = (parentPermission + "\"" + fileInstallLocation + "/" + title + "\"").replaceAll("\\\\", "/");
    }
}
