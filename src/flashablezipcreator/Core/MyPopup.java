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
import flashablezipcreator.UserInterface.Delete;
import flashablezipcreator.UserInterface.Preference;
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
            addQuickProjectObject();
        });
        popup = new JPopupMenu();
        popup.add(mitemAddProject);
        return popup;
    }

    public static JPopupMenu getProjectMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectNode node = (ProjectNode) nodeList.get(0);

        JMenuItem mitemSystem = new JMenuItem("system");
        mitemSystem.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM, node, "System Files");
        });

        JMenuItem mitemSystemApp = new JMenuItem("system/app");
        mitemSystemApp.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_APK, node, "System Apps");
        });
        JMenuItem mitemPrivApp = new JMenuItem("system/priv-app");
        mitemPrivApp.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_PRIV_APK, node, "Priv Apps");
        });
        JMenuItem mitemSystemEtc = new JMenuItem("system/etc");
        mitemSystemEtc.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_ETC, node, "System Etc");
        });

        JMenuItem mitemSystemBin = new JMenuItem("system/bin");
        mitemSystemBin.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_BIN, node, "System Bin");
        });

        JMenuItem mitemSystemFramework = new JMenuItem("system/framework");
        mitemSystemFramework.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_FRAMEWORK, node, "System Framework");
        });

        JMenuItem mitemDataApp = new JMenuItem("data/app");
        mitemDataApp.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_DATA_APP, node, "Data Apps");
        });
        JMenuItem mitemAlarms = new JMenuItem("system/media/audio/alarms");
        mitemAlarms.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS, node, "Alarm Tones");
        });
        JMenuItem mitemNotifications = new JMenuItem("system/media/audio/notifications");
        mitemNotifications.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS, node, "Notifications");
        });
        JMenuItem mitemRingtones = new JMenuItem("system/media/audio/ringtones");
        mitemRingtones.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES, node, "Ringtones");
        });
        JMenuItem mitemUI = new JMenuItem("system/media/audio/ui");
        mitemUI.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_MEDIA_AUDIO_UI, node, "UI Tones");
        });
        JMenuItem mitemFonts = new JMenuItem("system/fonts");
        mitemFonts.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_FONTS, node, "Fonts");
        });
        JMenuItem mitemBootAnimSystem = new JMenuItem("system/media");
        mitemBootAnimSystem.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_MEDIA, node, "Boot Animations");
        });
        JMenuItem mitemBootAnimLocal = new JMenuItem("data/local");
        mitemBootAnimLocal.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_DATA_LOCAL, node, "Boot Animations");
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
                    addQuickGroupObject(Types.GROUP_AROMA_THEMES, node, "My Theme");
                });
                popup.add(mitemAddThemes);
            } else {
                JMenu addGroupMenu = new JMenu("Add Group");
                JMenu addSystemGroupMenu = new JMenu("System Group");
                addSystemGroupMenu.add(mitemSystem);
                addSystemGroupMenu.add(mitemSystemApp);
                addSystemGroupMenu.add(mitemPrivApp);
                addSystemGroupMenu.add(mitemSystemBin);
                addSystemGroupMenu.add(mitemSystemEtc);
                addSystemGroupMenu.add(mitemSystemFramework);
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
                JMenuItem mitemDeleteGroup = new JMenuItem("Delete Files Group");
                mitemDeleteGroup.addActionListener((ActionEvent ae) -> {
                    addQuickGroupObject(Types.GROUP_DELETE_FILES, node, "Delete Files Group");
                });
                JMenuItem mitemCustomGroup = new JMenuItem("Custom Group");
                mitemCustomGroup.addActionListener((ActionEvent ae) -> {
                    addName(Types.GROUP_CUSTOM, node);
                });
                addGroupMenu.add(addSystemGroupMenu);
                addGroupMenu.add(addDataGroupMenu);
                addGroupMenu.add(addFontsGroupMenu);
                addGroupMenu.add(addTonesGroupMenu);
                addGroupMenu.add(addBootAnimationGroupMenu);
                addGroupMenu.add(mitemDeleteGroup);
                addGroupMenu.add(mitemCustomGroup);
                popup.add(addGroupMenu);
            }
        }
        popup.add(mitemDeleteProject);
        return popup;
    }

    public static JPopupMenu getGroupMenu(ArrayList<ProjectItemNode> nodeList, JTree myTree) {
        GroupNode node = (GroupNode) nodeList.get(0);
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
            case Types.GROUP_DELETE_FILES:
                mitemAddSubGroup = new JMenuItem("Add Files/Folders to Delete");
                break;
            default:
                mitemAddSubGroup = new JMenuItem("Add SubGroup");
        }
        mitemAddSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    switch (groupType) {
                        case Types.GROUP_SYSTEM_FONTS:
                            addQuickSubGroupObject(Types.GROUP_SYSTEM_FONTS, node, "Font");
                            break;
                        case Types.GROUP_DATA_LOCAL:
                            addQuickSubGroupObject(Types.GROUP_DATA_LOCAL, node, "Boot Animation");
                            break;
                        case Types.GROUP_SYSTEM_MEDIA:
                            addQuickSubGroupObject(Types.GROUP_SYSTEM_MEDIA, node, "Boot Animation");
                            break;
                        case Types.GROUP_DELETE_FILES:
                            Delete d = new Delete(node);
                            break;
                    }
                }
        );
        JMenuItem mitemAddFolder = new JMenuItem("Add Folder");
        mitemAddFolder.addActionListener((ActionEvent ae) -> {
            addQuickFolderObject(node);
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
                case Types.GROUP_SYSTEM:
                case Types.GROUP_SYSTEM_APK:
                case Types.GROUP_SYSTEM_PRIV_APK:
                case Types.GROUP_SYSTEM_ETC:
                case Types.GROUP_SYSTEM_FRAMEWORK:
                case Types.GROUP_DATA_APP:
                case Types.GROUP_CUSTOM:
                    popup.add(mitemAddFolder);
                    popup.add(mitemAddFile);
                    break;
                case Types.GROUP_SYSTEM_BIN:
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
                case Types.GROUP_DELETE_FILES:
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
            addQuickFolderObject(node);
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

    public static void addQuickProjectObject() {
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
    }

    public static void addQuickGroupObject(int type, ProjectNode parent, String defaultName) {
        if (!Preference.pp.isQuickSetup && type != Types.GROUP_DELETE_FILES) {
            addName(type, parent);
        } else {
            if (parent.contains(defaultName)) {
                for (int i = 1;; i++) {
                    if (parent.contains(defaultName + "(" + i + ")")) {
                        continue;
                    }
                    defaultName = defaultName + "(" + i + ")";
                    break;
                }
            }
            ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
            if (selNode != null) {
                NodeProperties p = new NodeProperties(defaultName, type, parent);
                GroupNode newNode = new GroupNode(p);
                parent.addChild(newNode, false);
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
                tree.startEditingAtPath(path);
            } else {
                JOptionPane.showMessageDialog(popup, "Select the Project first");
            }
        }
    }

    public static void addQuickSubGroupObject(int type, GroupNode parent, String defaultName) {
        if (!Preference.pp.isQuickSetup) {
            addName(type, parent);
        } else {
            if (parent.contains(defaultName)) {
                for (int i = 1;; i++) {
                    if (parent.contains(defaultName + "(" + i + ")")) {
                        continue;
                    }
                    defaultName = defaultName + "(" + i + ")";
                    break;
                }
            }
            ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
            if (selNode != null) {
                NodeProperties p = new NodeProperties(defaultName, type, parent);
                SubGroupNode newNode = new SubGroupNode(p);
                parent.addChild(newNode, false);
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
                tree.startEditingAtPath(path);
            } else {
                JOptionPane.showMessageDialog(popup, "Select the Group first");
            }
        }
    }

    public static void addQuickFolderObject(ProjectItemNode parent) {
        String defaultFolderName = "NewFolder";
        if (parent.contains(defaultFolderName)) {
            for (int i = 1;; i++) {
                if (parent.contains(defaultFolderName + "(" + i + ")")) {
                    continue;
                }
                defaultFolderName = defaultFolderName + "(" + i + ")";
                break;
            }
        }
        ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
        if (selNode != null) {
            FolderNode newNode = null;//try catch should be implemented here as if conditions may not be true
            if (parent.prop.type == Types.NODE_GROUP) {
                newNode = new FolderNode(defaultFolderName, (GroupNode) parent);
            } else if (parent.prop.type == Types.NODE_FOLDER) {
                newNode = new FolderNode(defaultFolderName, (FolderNode) parent);
            }
            parent.addChild(newNode, false);
            TreeNode[] nodes = model.getPathToRoot(newNode);
            TreePath path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
            tree.setSelectionPath(path);
            tree.startEditingAtPath(path);
        } else {
            JOptionPane.showMessageDialog(popup, "Select the Group Node first");
        }
    }
}
