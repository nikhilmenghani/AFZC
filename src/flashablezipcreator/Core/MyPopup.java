/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import flashablezipcreator.Operations.MyFileFilter;
import flashablezipcreator.Protocols.Mod;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.AddGroup;
import flashablezipcreator.UserInterface.Preferences;
import flashablezipcreator.UserInterface.MyTree;
import static flashablezipcreator.UserInterface.MyTree.model;
import static flashablezipcreator.UserInterface.MyTree.rootNode;
import static flashablezipcreator.UserInterface.MyTree.tree;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Nikhil
 */
public class MyPopup {

    static JPopupMenu popup;
    static AddGroup addGroup;

    public static JPopupMenu getPopup(TreePath[] paths, JTree myTree) {
        popup = new JPopupMenu();
        ArrayList<ProjectItemNode> nodeList = new ArrayList<>();
        Set<Integer> typeList = new HashSet<>();
        for (TreePath path : paths) {
            nodeList.add((ProjectItemNode) path.getLastPathComponent());
            typeList.add(((ProjectItemNode) path.getLastPathComponent()).prop.type);
        }
        ProjectItemNode node = nodeList.get(0);
        if (typeList.size() > 1) {
            JOptionPane.showMessageDialog(null, "You cannot select nodes of different types.");
        } else {
            switch (node.prop.type) {
                case Types.NODE_ROOT:
                    popup = getRootMenu();
                    break;
                case Types.NODE_PROJECT:
                    popup = getProjectMenu(nodeList);
                    break;
                case Types.NODE_GROUP:
                    popup = getGroupMenu(nodeList, myTree);
                    break;
                case Types.NODE_SUBGROUP:
                    popup = getSubGroupMenu(nodeList);
                    break;
                case Types.NODE_FOLDER:
                    popup = getFolderMenu(nodeList);
                    break;
                case Types.NODE_FILE:
                    popup = getFileMenu(nodeList);
                    break;
            }
        }
        return popup;
    }

    public static JPopupMenu getRootMenu() {
        JMenuItem mitemAddProject = new JMenuItem("Add Project");
        mitemAddProject.addActionListener((ActionEvent ae) -> {
            String defaultProjName = "My Project";
            if (rootNode.contains(defaultProjName)) {
                for (int i = 1;; i++) {
                    if (rootNode.contains(defaultProjName + "(" + i + ")")) {
                        continue;
                    }
                    defaultProjName = defaultProjName + "(" + i + ")";
                    break;
                }
            }
            ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
            if (selNode != null) {
                ProjectNode newNode = new ProjectNode(defaultProjName, Types.PROJECT_AROMA, Mod.MOD_LESS, rootNode);
                rootNode.addChild(newNode, false);
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
                tree.startEditingAtPath(path);
            } else {
                JOptionPane.showMessageDialog(popup, "Select the AFZC Projects node first");
            }
        });
        popup = new JPopupMenu();
        popup.add(mitemAddProject);
        return popup;
    }

    public static JPopupMenu getProjectMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectNode node = (ProjectNode) nodeList.get(0);
        JMenuItem mitemSystemApp = new JMenuItem("system/app");
        mitemSystemApp.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_APK, node);
        });
        JMenuItem mitemPrivApp = new JMenuItem("system/priv-app");
        mitemPrivApp.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_PRIV_APK, node);
        });
        JMenuItem mitemDataApp = new JMenuItem("data/app");
        mitemDataApp.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_DATA_APP, node);
        });
        JMenuItem mitemAlarms = new JMenuItem("system/media/audio/alarms");
        mitemAlarms.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS, node);
        });
        JMenuItem mitemNotifications = new JMenuItem("system/media/audio/notifications");
        mitemNotifications.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS, node);
        });
        JMenuItem mitemRingtones = new JMenuItem("system/media/audio/ringtones");
        mitemRingtones.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES, node);
        });
        JMenuItem mitemUI = new JMenuItem("system/media/audio/ui");
        mitemUI.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_MEDIA_AUDIO_UI, node);
        });
        JMenuItem mitemFonts = new JMenuItem("system/fonts");
        mitemFonts.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_FONTS, node);
        });
        JMenuItem mitemBootAnimSystem = new JMenuItem("system/media");
        mitemBootAnimSystem.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_SYSTEM_MEDIA, node);
        });
        JMenuItem mitemBootAnimLocal = new JMenuItem("data/local");
        mitemBootAnimLocal.addActionListener((ActionEvent ae) -> {
            addName(Types.GROUP_DATA_LOCAL, node);
        });
        JMenuItem mitemDeleteProject = new JMenuItem("Delete Project");
        mitemDeleteProject.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });

        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            if (((ProjectNode) node).prop.projectType == Types.PROJECT_THEMES) {
                JMenuItem mitemAddThemes = new JMenuItem("Add Theme");
                mitemAddThemes.addActionListener((ActionEvent ae) -> {
                    addName(Types.GROUP_AROMA_THEMES, node);
                });
                popup.add(mitemAddThemes);
            } else {
                JMenu addGroupMenu = new JMenu("Add Group");
                JMenu addSystemGroupMenu = new JMenu("System Group");
                addSystemGroupMenu.add(mitemSystemApp);
                addSystemGroupMenu.add(mitemPrivApp);
                JMenu addDataGroupMenu = new JMenu("Data Group");
                addDataGroupMenu.add(mitemDataApp);
                JMenu addFontsGroupMenu = new JMenu("Fonts Group");
                addFontsGroupMenu.add(mitemFonts);
                JMenu addTonesGroupMenu = new JMenu("Tones Group");
                addTonesGroupMenu.add(mitemAlarms);
                addTonesGroupMenu.add(mitemNotifications);
                addTonesGroupMenu.add(mitemRingtones);
                addTonesGroupMenu.add(mitemUI);
                JMenu addBootAnimationGroupMenu = new JMenu("Boot Animation Group");
                addBootAnimationGroupMenu.add(mitemBootAnimSystem);
                addBootAnimationGroupMenu.add(mitemBootAnimLocal);
                JMenuItem mitemCustomGroup = new JMenuItem("Custom Group");
                mitemCustomGroup.addActionListener((ActionEvent ae) -> {
                    addName(Types.GROUP_CUSTOM, node);
                });
                addGroupMenu.add(addSystemGroupMenu);
                addGroupMenu.add(addDataGroupMenu);
                addGroupMenu.add(addFontsGroupMenu);
                addGroupMenu.add(addTonesGroupMenu);
                addGroupMenu.add(addBootAnimationGroupMenu);
                addGroupMenu.add(mitemCustomGroup);
                popup.add(addGroupMenu);
            }
        }
        popup.add(mitemDeleteProject);
        return popup;
    }

    public static JPopupMenu getGroupMenu(ArrayList<ProjectItemNode> nodeList, JTree myTree) {
        ProjectItemNode node = nodeList.get(0);
        JMenuItem mitemAddSubGroup = null;
        int groupType = ((GroupNode) node).prop.groupType;
        switch (groupType) {
            case Types.GROUP_SYSTEM_FONTS:
                mitemAddSubGroup = new JMenuItem("Add Fonts");
                break;
            case Types.GROUP_DATA_LOCAL:
                mitemAddSubGroup = new JMenuItem("Add Boot Animations");
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                mitemAddSubGroup = new JMenuItem("Add Boot Animations");
                break;
            default:
                mitemAddSubGroup = new JMenuItem("Add SubGroup");
        }
        mitemAddSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    switch (groupType) {
                        case Types.GROUP_SYSTEM_FONTS:
                            addName(Types.GROUP_SYSTEM_FONTS, node);
                            break;
                        case Types.GROUP_DATA_LOCAL:
                            addName(Types.GROUP_DATA_LOCAL, node);
                            break;
                        case Types.GROUP_SYSTEM_MEDIA:
                            addName(Types.GROUP_SYSTEM_MEDIA, node);
                            break;
                    }
                }
        );
        JMenuItem mitemAddFolder = new JMenuItem("Add Folder");
        mitemAddFolder.addActionListener((ActionEvent ae) -> {
            String defaultFolderName = "NewFolder";
            if (node.contains(defaultFolderName)) {
                for (int i = 1;; i++) {
                    if (node.contains(defaultFolderName + "(" + i + ")")) {
                        continue;
                    }
                    defaultFolderName = defaultFolderName + "(" + i + ")";
                    break;
                }
            }
            ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
            if (selNode != null) {
                FolderNode newNode = new FolderNode(defaultFolderName, (GroupNode) node);
                node.addChild(newNode, false);
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
                tree.startEditingAtPath(path);
            } else {
                JOptionPane.showMessageDialog(popup, "Select the Group Node first");
            }
        });
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    try {
                        addNode(nodeList);
                    } catch (NullPointerException npe) {

                    }
                }
        );
        JMenuItem mitemDeleteGroup = new JMenuItem("Delete Group(s)");
        mitemDeleteGroup.addActionListener(
                (ActionEvent ae) -> {
                    deleteNode(nodeList);
                }
        );
        if (((GroupNode) node).prop.groupType == Types.GROUP_AROMA_THEMES) {
            mitemDeleteGroup.setText("Delete Theme(s)");
        }
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            switch (((GroupNode) node).prop.groupType) {
                case Types.GROUP_SYSTEM_APK:
                case Types.GROUP_SYSTEM_PRIV_APK:
                case Types.GROUP_DATA_APP:
                case Types.GROUP_CUSTOM:
                    popup.add(mitemAddFolder);
                    popup.add(mitemAddFile);
                    break;
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                case Types.GROUP_AROMA_THEMES:
                    popup.add(mitemAddFile);
                    break;
                case Types.GROUP_SYSTEM_FONTS:
                case Types.GROUP_DATA_LOCAL:
                case Types.GROUP_SYSTEM_MEDIA:
                    popup.add(mitemAddSubGroup);
                    break;
            }

        }
        popup.add(mitemDeleteGroup);
        return popup;
    }

    public static JPopupMenu getSubGroupMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    try {
                        addNode(nodeList);
                    } catch (NullPointerException npe) {

                    }
                }
        );
        JMenuItem mitemDeleteSubGroup = new JMenuItem("Delete SubGroup(s)");
        mitemDeleteSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    deleteNode(nodeList);
                }
        );
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            popup.add(mitemAddFile);
        }
        popup.add(mitemDeleteSubGroup);
        return popup;
    }

    public static JPopupMenu getFolderMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    try {
                        addNode(nodeList);
                    } catch (NullPointerException npe) {

                    }
                }
        );
        JMenuItem mitemAddFolder = new JMenuItem("Add Folder");
        mitemAddFolder.addActionListener((ActionEvent ae) -> {
            String defaultFolderName = "NewFolder";
            if (node.contains(defaultFolderName)) {
                for (int i = 1;; i++) {
                    if (node.contains(defaultFolderName + "(" + i + ")")) {
                        continue;
                    }
                    defaultFolderName = defaultFolderName + "(" + i + ")";
                    break;
                }
            }
            ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
            if (selNode != null) {
                FolderNode newNode = new FolderNode(defaultFolderName, (FolderNode) node);
                node.addChild(newNode, false);
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
                tree.startEditingAtPath(path);
            } else {
                JOptionPane.showMessageDialog(popup, "Select the Folder Node first");
            }
        });
        JMenuItem mitemDeleteFolder = new JMenuItem("Delete Folder(s)");
        mitemDeleteFolder.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            popup.add(mitemAddFile);
            popup.add(mitemAddFolder);
        }
        popup.add(mitemDeleteFolder);
        return popup;
    }

    public static JPopupMenu getFileMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenuItem mitemDeleteFile = new JMenuItem("Delete File(s)");
        mitemDeleteFile.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            //all other popups will be added here.
        }
        popup.add(mitemDeleteFile);
        return popup;
    }

    public static void addNode(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        switch (node.prop.type) {
            case Types.NODE_GROUP:
                GroupNode gNode = (GroupNode) node;
                for (File tempFile : MyFileFilter.getSelectedFiles(gNode.prop.extension)) {
                    FileNode fnode;
                    if (Preferences.pp.IsFromLollipop) {
                        switch (gNode.prop.groupType) {
                            case Types.GROUP_SYSTEM_APK:
                            case Types.GROUP_SYSTEM_PRIV_APK:
                            case Types.GROUP_DATA_APP:
                                String folderName = tempFile.getName().replaceFirst("[.][^.]+$", "");
                                //it is okay to remove following condition as this is handled directly while creating folder node
                                if (gNode.prop.groupType == Types.GROUP_DATA_APP) {
                                    folderName = tempFile.getName().replaceFirst("[.][^.]+$", "") + "-1";
                                }
                                FolderNode folderNode = new FolderNode(folderName, gNode);
                                fnode = new FileNode(tempFile.getAbsolutePath(), folderNode);
                                folderNode.addChild(fnode, true);
                                gNode.addChild(folderNode, false);
                                break;
                            default:
                                fnode = new FileNode(tempFile.getAbsolutePath(), gNode);
                                gNode.addChild(fnode, true);
                        }
                    } else {
                        fnode = new FileNode(tempFile.getAbsolutePath(), gNode);
                        gNode.addChild(fnode, true);
                    }
                }
                break;
            case Types.NODE_SUBGROUP:
                for (File tempFile : MyFileFilter.getSelectedFiles(((SubGroupNode) node).prop.extension)) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (SubGroupNode) node);
                    //fnode.fileSourcePath = tempFile.getAbsolutePath();
                    ((SubGroupNode) node).addChild(fnode, true);
                }
                break;
            case Types.NODE_FOLDER:
                for (File tempFile : MyFileFilter.getSelectedFiles("folder")) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (FolderNode) node);
                    ((FolderNode) node).addChild(fnode, true);
                }
                break;
        }

    }

    public static void deleteNode(ArrayList<ProjectItemNode> nodeList) {
        for (ProjectItemNode node : nodeList) {
            node.removeMe();
        }
    }

    public static void addName(int type, ProjectItemNode node) {
        addGroup = new AddGroup(type, node);
    }
}
