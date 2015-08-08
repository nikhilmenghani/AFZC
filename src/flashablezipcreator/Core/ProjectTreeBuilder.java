/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package flashablezipcreator.Core;

import flashablezipcreator.Operations.TreeOperations;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Nikhil
 */
public class ProjectTreeBuilder {
    
    public static javax.swing.JTree tree;
    public static javax.swing.JScrollPane jScrollPane;
    public static ProjectItemNode rootNode;
    
    public static javax.swing.JTree buildTree() throws IOException{
        rootNode = new ProjectItemNode("AFZC Projects", ProjectItemNode.NODE_ROOT);
        
        ProjectNode projectNode = new ProjectNode("Normal Aroma Zip", ProjectNode.PROJECT_AROMA, rootNode);
        
        
        GroupNode groupNode1 = new GroupNode("Launcher", GroupNode.GROUP_CUSTOM, projectNode);
        
        SubGroupNode subGroupNode1 = new SubGroupNode("S4 Launcher", SubGroupNode.TYPE_CUSTOM, groupNode1);
        
        Vector<GroupNode> groupVec = new Vector<GroupNode>();
        
        //groupVec.add(new GroupNode("Launcher", ProjectItemNode.NODE_GROUP));
        
        Vector<SubGroupNode> subGroupVec = new Vector<SubGroupNode>();
        
        //subGroupVec.add(new SubGroupNode("S4 Launcher", ProjectItemNode.NODE_SUBGROUP));
        
        Vector<FileNode> fileVec = new Vector<FileNode>();
        
        //fileVec.add(new FileNode("home.apk", ProjectItemNode.NODE_FILE));
        //fileVec.add(new FileNode("home.so", ProjectItemNode.NODE_FILE));
        //fileVec.add(new FileNode("Xperia.apk", ProjectItemNode.NODE_FILE));
        
//        for(GroupNode node : groupVec){
//            for(SubGroupNode subNode : subGroupVec){
//                for(FileNode fileNode : fileVec){
//                    subNode.addChild(fileNode);
//                }
//                node.addChild(subNode);
//            }
//            projectNode.addChild(node);
//        }
        
        //rootNode.addChild(projectNode);
        
        //System.out.println(projectNode.getParent().toString() + "Nikhil");
        FileNode file1 = new FileNode("Flash" + File.separator + "Data" + File.separator + "Data Apps" + File.separator + "pl.solidexplorer-2.apk", "0, 0, /system/etc",
                "0, 0, \"/system/etc/pl.solidexplorer-2.apk\"", subGroupNode1);
        FileNode file2 = new FileNode("Flash" + File.separator + "Data" + File.separator + "Data Apps" + File.separator + "pl.solidexplorer.unlocker-1.apk", "/system/etc", "rwrr", subGroupNode1);
        FileNode file3 = new FileNode("Flash" + File.separator + "System" + File.separator + "System App" + File.separator + "MIUIMusic.apk", groupNode1);
        
//        for(ProjectItemNode obj : vec){
//            switch(obj.type){
//                case ProjectItemNode.NODE_FILE:
//                    subGroupNode1.addChild(obj);
//                    break;
//                case ProjectItemNode.NODE_SUBGROUP:
//                    
//            }
//            
//        }
        subGroupNode1.addChild(file1);
        subGroupNode1.addChild(file2);
        
        groupNode1.addChild(subGroupNode1);
        groupNode1.addChild(file3);
        
        projectNode.addChild(groupNode1);
        
        GroupNode groupNode2 = new GroupNode("Google Launcher", GroupNode.GROUP_SYSTEM_APK, projectNode);
        
        FileNode file4 = new FileNode("Flash" + File.separator + "System" + File.separator + "System App" + File.separator + "CMFileManager.apk", "/system/etc", "rwrr", groupNode2);
        
        groupNode2.addChild(file4);
        
        projectNode.addChild(groupNode2);
        
        //rootNode.addChild(projectNode);
        
        //createNode("Normal Aroma Zip", ProjectItemNode.NODE_PROJECT).addChild(createNode("Ringtones", ProjectItemNode.NODE_GROUP));
        
        //rootNode.addChild(createNode("Normal Aroma Zip", ProjectItemNode.NODE_PROJECT));
        
        //System.out.println(rootNode.children);
        
        TreeOperations to = new TreeOperations(rootNode);
        to.buildDirectory(rootNode);
        
        tree = new javax.swing.JTree(rootNode);
        
        
        
        //tree.setCellRenderer(new NodeRenderer());
        tree.setCellRenderer(new DefaultTreeCellRenderer());
        return tree;
    }
    
    public static DefaultTreeModel buildModel(){
        return (DefaultTreeModel) ProjectTreeBuilder.tree.getModel();
    }
    
    public static javax.swing.JScrollPane buildScrollPane(){
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView(ProjectTreeBuilder.tree);
        return jScrollPane;
    }
    
    public static ProjectItemNode createNode(String title, int type){
        return new ProjectItemNode(title, type);
    }
}
