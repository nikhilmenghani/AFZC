/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Adb.Adb;
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
                case Types.NODE_DELETE:
                    getDeleteGroup(nodeList);
                    break;
            }
        }
        return popup;
    }

    public static JPopupMenu getDeleteGroup(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenuItem mitemDeleteFile = new JMenuItem("Delete File(s)");
        mitemDeleteFile.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });
        popup = new JPopupMenu();
        popup.add(mitemDeleteFile);
        return popup;
    }

    public static JPopupMenu getRootMenu() {
        JMenu addProjectMenu = new JMenu("Add Project");
        JMenu addGappsMenu = new JMenu("Gapps");
        JMenuItem mitemAddModsProject = new JMenuItem("Mods");
        mitemAddModsProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_MOD, Mod.MOD_LESS);
        });
        JMenuItem mitemAddAromaProject = new JMenuItem("Aroma");
        mitemAddAromaProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_AROMA, Mod.MOD_LESS);
        });
        JMenuItem mitemAddPicoGappsProject = new JMenuItem("Pico");
        mitemAddPicoGappsProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_GAPPS, Types.GAPPS_PICO);
        });
        JMenuItem mitemAddCoreGappsProject = new JMenuItem("Core");
        mitemAddCoreGappsProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_GAPPS, Types.GAPPS_CORE);
        });
        JMenuItem mitemAddNanoGappsProject = new JMenuItem("Nano");
        mitemAddNanoGappsProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_GAPPS, Types.GAPPS_NANO);
        });
        JMenuItem mitemAddMyGappsProject = new JMenuItem("My");
        mitemAddMyGappsProject.addActionListener((ActionEvent ae) -> {
            addQuickProjectObject(Types.PROJECT_GAPPS, Types.GAPPS_MY);
        });
        addGappsMenu.add(mitemAddCoreGappsProject);
        addGappsMenu.add(mitemAddPicoGappsProject);
        addGappsMenu.add(mitemAddNanoGappsProject);
        addGappsMenu.add(mitemAddMyGappsProject);
        addProjectMenu.add(mitemAddAromaProject);
        addProjectMenu.add(addGappsMenu);
        popup = new JPopupMenu();
        popup.add(addProjectMenu);
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

        JMenuItem mitemSystemXBin = new JMenuItem("system/xbin");
        mitemSystemBin.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_XBIN, node, "System xBin");
        });

        JMenuItem mitemSystemFramework = new JMenuItem("system/framework");
        mitemSystemFramework.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_FRAMEWORK, node, "System Framework");
        });

        JMenuItem mitemSystemLib = new JMenuItem("system/lib");
        mitemSystemLib.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_LIB, node, "System Lib");
        });

        JMenuItem mitemSystemLib64 = new JMenuItem("system/lib64");
        mitemSystemLib64.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_SYSTEM_LIB64, node, "System Lib64");
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

        JMenuItem mitemVendor = new JMenuItem("vendor");
        mitemVendor.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR, node, "Vendor Files");
        });

        JMenuItem mitemVendorApp = new JMenuItem("vendor/app");
        mitemVendorApp.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_APP, node, "Vendor Apps");
        });

        JMenuItem mitemVendorBin = new JMenuItem("vendor/bin");
        mitemVendorBin.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_BIN, node, "Vendor Bin");
        });

        JMenuItem mitemVendorEtc = new JMenuItem("vendor/etc");
        mitemVendorEtc.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_ETC, node, "Vendor Etc");
        });

        JMenuItem mitemVendorLib = new JMenuItem("vendor/lib");
        mitemVendorLib.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_LIB, node, "Vendor Lib");
        });

        JMenuItem mitemVendorLib64 = new JMenuItem("vendor/lib64");
        mitemVendorLib64.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_LIB64, node, "Vendor Lib64");
        });

        JMenuItem mitemVendorFramework = new JMenuItem("vendor/framework");
        mitemVendorFramework.addActionListener((ActionEvent ae) -> {
            addQuickGroupObject(Types.GROUP_VENDOR_FRAMEWORK, node, "Vendor Framework");
        });

        JMenuItem mitemDeleteProject = new JMenuItem("Delete Project");
        mitemDeleteProject.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });
        JMenuItem mItemMoveUp = new JMenuItem("Move Up");
        mItemMoveUp.addActionListener((ActionEvent ae) -> {
            ((ProjectNode) node).prop.parent.moveUp(node);
        });
        JMenuItem mItemMoveDown = new JMenuItem("Move Down");
        mItemMoveDown.addActionListener((ActionEvent ae) -> {
            ((ProjectNode) node).prop.parent.moveDown(node);
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
                addSystemGroupMenu.add(mitemSystemXBin);
                addSystemGroupMenu.add(mitemSystemEtc);
                addSystemGroupMenu.add(mitemSystemFramework);
                addSystemGroupMenu.add(mitemSystemLib);
                addSystemGroupMenu.add(mitemSystemLib64);
                JMenu addVendorGroupMenu = new JMenu("Vendor Group");
                addVendorGroupMenu.add(mitemVendor);
                addVendorGroupMenu.add(mitemVendorApp);
                addVendorGroupMenu.add(mitemVendorBin);
                addVendorGroupMenu.add(mitemVendorEtc);
                addVendorGroupMenu.add(mitemVendorFramework);
                addVendorGroupMenu.add(mitemVendorLib);
                addVendorGroupMenu.add(mitemVendorLib64);
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
//                JMenu addGappsMenu = new JMenu("Build Gapps");
                JMenuItem mitemBuildGappsFromDevice = new JMenuItem("Build Gapps");
                mitemBuildGappsFromDevice.addActionListener((ActionEvent ae) -> {
                    Adb device = new Adb();
                    device.importGapps(node);
                });
                switch (node.prop.projectType) {
                    case Types.PROJECT_AROMA:
                        addGroupMenu.add(addDataGroupMenu);
                        addGroupMenu.add(addFontsGroupMenu);
                        addGroupMenu.add(addTonesGroupMenu);
                        addGroupMenu.add(addBootAnimationGroupMenu);
                        break;
                    case Types.PROJECT_GAPPS:
//                        addGappsMenu.add(mitemBuildPicoGappsFromDevice);
                        popup.add(mitemBuildGappsFromDevice);
                        break;
                }
                addGroupMenu.add(addSystemGroupMenu);
                addGroupMenu.add(addVendorGroupMenu);
                addGroupMenu.add(mitemDeleteGroup);
                addGroupMenu.add(mitemCustomGroup);
                popup.add(addGroupMenu);
            }
        }
        if (node.prop.parent.getChildCount() != 1) {
            if (node.prop.parent.getIndex(node) != 0) {
                popup.add(mItemMoveUp);
            }
            if (node.prop.parent.getIndex(node) != node.prop.parent.getChildCount() - 1) {
                popup.add(mItemMoveDown);
            }
        }
        popup.add(mitemDeleteProject);
        return popup;
    }

    public static JPopupMenu getGroupMenu(ArrayList<ProjectItemNode> nodeList, JTree myTree) {
        GroupNode node = (GroupNode) nodeList.get(0);
        JMenuItem mitemAddSubGroup = null;
        JMenuItem mitemAddFromDevice = null;
        JMenu addDeviceTalksMenu = new JMenu("Device Talks");
        JMenuItem mitemUpdateApp = new JMenuItem("Update Files");
        mitemUpdateApp.addActionListener(
                (ActionEvent ae) -> {
                    Adb adb = new Adb();
                    adb.checkForUpdate(node);
                }
        );
        int groupType = (node).prop.groupType;
        switch (node.prop.packageType) {
            case Types.PACKAGE_SUBGROUP_FILE:
            case Types.PACKAGE_DELETE_FILE:
                mitemAddSubGroup = new JMenuItem("Add " + node.prop.folderMenuName);
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
        JMenuItem mItemMoveUp = new JMenuItem("Move Up");
        mItemMoveUp.addActionListener((ActionEvent ae) -> {
            ((GroupNode) node).prop.parent.moveUp(node);
        });
        JMenuItem mItemMoveDown = new JMenuItem("Move Down");
        mItemMoveDown.addActionListener((ActionEvent ae) -> {
            ((GroupNode) node).prop.parent.moveDown(node);
        });
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            switch (node.prop.packageType) {
                case Types.PACKAGE_APP:
                    addDeviceTalksMenu.add(mitemUpdateApp);
                case Types.PACKAGE_FOLDER_FILE:
                    popup.add(mitemAddFolder);
                case Types.PACKAGE_FILE:
                    mitemAddFromDevice = new JMenuItem("Add " + node.prop.folderMenuName + "");
                case Types.PACKAGE_THEME:
                    popup.add(mitemAddFile);
                    break;
                case Types.PACKAGE_SUBGROUP_FILE:
                    mitemAddFromDevice = new JMenuItem("Add " + node.prop.folderMenuName + "");
                case Types.PACKAGE_DELETE_FILE:
                    mitemUpdateApp = null;
                    mitemAddFromDevice = null;
                    popup.add(mitemAddSubGroup);
                    break;
                case Types.PACKAGE_CUSTOM:
                    popup.add(mitemAddFolder);
                    break;
            }
            if (mitemAddFromDevice != null) {
                mitemAddFromDevice.addActionListener(
                        (ActionEvent ae) -> {
                            Adb device = new Adb();
                            device.importFiles(node);
                        }
                );
                addDeviceTalksMenu.add(mitemAddFromDevice);
            }
        }
        if (mitemUpdateApp != null && mitemAddFromDevice != null) {
            popup.add(addDeviceTalksMenu);
        }
        if (node.prop.parent.getChildCount() != 1) {
            if (node.prop.parent.getIndex(node) != 0) {
                popup.add(mItemMoveUp);
            }
            if (node.prop.parent.getIndex(node) != node.prop.parent.getChildCount() - 1) {
                popup.add(mItemMoveDown);
            }
        }
        popup.add(mitemDeleteGroup);
        return popup;
    }

    public static JPopupMenu getSubGroupMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenu addDeviceTalksMenu = new JMenu("Device Talks");
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        mitemAddFile.addActionListener(
                (ActionEvent ae) -> {
                    try {
                        addNode(nodeList);
                    } catch (NullPointerException npe) {

                    }
                }
        );
        JMenuItem mitemUpdateApp = new JMenuItem("Update Files");
        mitemUpdateApp.addActionListener(
                (ActionEvent ae) -> {
                    Adb adb = new Adb();
                    adb.checkForUpdate(node);
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
            addDeviceTalksMenu.add(mitemUpdateApp);
            popup.add(mitemAddFile);
            popup.add(addDeviceTalksMenu);
        }
        popup.add(mitemDeleteSubGroup);
        return popup;
    }

    public static JPopupMenu getFolderMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenu addDeviceTalksMenu = new JMenu("Device Talks");
        JMenuItem mitemAddFile = new JMenuItem("Add File(s)");
        JMenuItem mitemUpdateApp = new JMenuItem("Update Files");
        mitemUpdateApp.addActionListener(
                (ActionEvent ae) -> {
                    Adb adb = new Adb();
                    adb.checkForUpdate(node);
                }
        );
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
            switch (node.prop.groupParent.prop.packageType) {
                case Types.PACKAGE_APP:
                    addDeviceTalksMenu.add(mitemUpdateApp);
                    popup.add(addDeviceTalksMenu);
                    break;
            }
            popup.add(mitemAddFile);
            popup.add(mitemAddFolder);

        }
        popup.add(mitemDeleteFolder);
        return popup;
    }

    public static JPopupMenu getFileMenu(ArrayList<ProjectItemNode> nodeList) {
        ProjectItemNode node = nodeList.get(0);
        JMenu addDeviceTalksMenu = new JMenu("Device Talks");
        JMenuItem mitemDeleteFile = new JMenuItem("Delete File(s)");
        mitemDeleteFile.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });
        JMenuItem mitemUpdateApp = new JMenuItem("Update Files");
        mitemUpdateApp.addActionListener(
                (ActionEvent ae) -> {
                    Adb adb = new Adb();
                    adb.checkForUpdate(node);
                }
        );
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            //all other popups will be added here.
            addDeviceTalksMenu.add(mitemUpdateApp);
            popup.add(addDeviceTalksMenu);
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
                    switch (gNode.prop.packageType) {
                        case Types.PACKAGE_APP:
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

    public static void addQuickProjectObject(int type, int modType) {
        String defaultProjName = "My Project";
        switch (type) {
            case Types.PROJECT_GAPPS:
                switch (modType) {
                    case Types.GAPPS_CORE:
                        defaultProjName = "Core Gapps";
                        break;
                    case Types.GAPPS_PICO:
                        defaultProjName = "Pico Gapps";
                        break;
                    case Types.GAPPS_NANO:
                        defaultProjName = "Nano Gapps";
                        break;
                    default:
                        defaultProjName = "My Gapps";
                }
                break;
        }
        defaultProjName = getUniqueName(rootNode, defaultProjName);
        ProjectItemNode selNode = (ProjectItemNode) MyTree.tree.getLastSelectedPathComponent();
        if (selNode != null) {
            ProjectNode newNode = new ProjectNode(defaultProjName, type, modType, rootNode);
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
            defaultName = getUniqueName(parent, defaultName);
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

    public static String getUniqueName(ProjectItemNode parent, String defaultName) {
        if (parent.contains(defaultName)) {
            for (int i = 1;; i++) {
                if (parent.contains(defaultName + "(" + i + ")")) {
                    continue;
                }
                defaultName = defaultName + "(" + i + ")";
                break;
            }
        }
        return defaultName;
    }
}
