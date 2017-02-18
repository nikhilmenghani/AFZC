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
import flashablezipcreator.UserInterface.Preferences;
import flashablezipcreator.UserInterface.AddName;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTree;

/**
 *
 * @author Nikhil
 */
public class MyPopup {

    static JPopupMenu popup;
    static AddName addGNUI;

    public static JPopupMenu getPopup(TreePath[] paths, JTree myTree) {
        popup = new JPopupMenu();
        ArrayList<ProjectItemNode> nodeList = new ArrayList<>();
        Set<Integer> typeList = new HashSet<>();
        for (TreePath path : paths) {
            nodeList.add((ProjectItemNode) path.getLastPathComponent());
            typeList.add(((ProjectItemNode) path.getLastPathComponent()).type);
        }
        if (typeList.size() > 1) {
            JOptionPane.showMessageDialog(null, "You cannot select nodes of different types.");
        } else {
            switch (nodeList.get(0).type) {
                case ProjectItemNode.NODE_ROOT:
                    popup = getRootMenu(nodeList);
                    break;
                case ProjectItemNode.NODE_PROJECT:
                    popup = getProjectMenu(nodeList);
                    break;
                case ProjectItemNode.NODE_GROUP:
                    popup = getGroupMenu(nodeList, myTree);
                    break;
                case ProjectItemNode.NODE_SUBGROUP:
                    popup = getSubGroupMenu(nodeList);
                    break;
                case ProjectItemNode.NODE_FOLDER:
                    popup = getFolderMenu(nodeList);
                    break;
                case ProjectItemNode.NODE_FILE:
                    popup = getFileMenu(nodeList);
                    break;
            }
        }
        return popup;
    }

    public static JPopupMenu getRootMenu(ArrayList<ProjectItemNode> nodeList) {
        JMenuItem mitemAddProject = new JMenuItem("Add Project");
        mitemAddProject.addActionListener((ActionEvent ae) -> {
            addName("Project", "", nodeList.get(0));
        });
        popup = new JPopupMenu();
        popup.add(mitemAddProject);
        return popup;
    }

    public static JPopupMenu getProjectMenu(ArrayList<ProjectItemNode> nodeList) {
        JMenuItem mitemSystemApp = new JMenuItem("system/app");
        mitemSystemApp.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/app", nodeList.get(0));
        });
        JMenuItem mitemPrivApp = new JMenuItem("system/priv-app");
        mitemPrivApp.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/priv-app", nodeList.get(0));
        });
        JMenuItem mitemDataApp = new JMenuItem("data/app");
        mitemDataApp.addActionListener((ActionEvent ae) -> {
            addName("Group", "data/app", nodeList.get(0));
        });
        JMenuItem mitemAlarms = new JMenuItem("system/media/audio/alarms");
        mitemAlarms.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/media/audio/alarms", nodeList.get(0));
        });
        JMenuItem mitemNotifications = new JMenuItem("system/media/audio/notifications");
        mitemNotifications.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/media/audio/notifications", nodeList.get(0));
        });
        JMenuItem mitemRingtones = new JMenuItem("system/media/audio/ringtones");
        mitemRingtones.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/media/audio/ringtones", nodeList.get(0));
        });
        JMenuItem mitemUI = new JMenuItem("system/media/audio/ui");
        mitemUI.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/media/audio/ui", nodeList.get(0));
        });
        JMenuItem mitemFonts = new JMenuItem("system/fonts");
        mitemFonts.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/fonts", nodeList.get(0));
        });
        JMenuItem mitemBootAnimSystem = new JMenuItem("system/media");
        mitemBootAnimSystem.addActionListener((ActionEvent ae) -> {
            addName("Group", "system/media", nodeList.get(0));
        });
        JMenuItem mitemBootAnimLocal = new JMenuItem("data/local");
        mitemBootAnimLocal.addActionListener((ActionEvent ae) -> {
            addName("Group", "data/local", nodeList.get(0));
        });
        JMenuItem mitemDeleteProject = new JMenuItem("Delete Project");
        mitemDeleteProject.addActionListener((ActionEvent ae) -> {
            deleteNode(nodeList);
        });

        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            if (((ProjectNode) nodeList.get(0)).projectType == ProjectNode.PROJECT_THEMES) {
                JMenuItem mitemAddThemes = new JMenuItem("Add Theme");
                mitemAddThemes.addActionListener((ActionEvent ae) -> {
                    addName("Theme", "themes", nodeList.get(0));
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
                addGroupMenu.add(addSystemGroupMenu);
                addGroupMenu.add(addDataGroupMenu);
                addGroupMenu.add(addFontsGroupMenu);
                addGroupMenu.add(addTonesGroupMenu);
                addGroupMenu.add(addBootAnimationGroupMenu);
                popup.add(addGroupMenu);
            }
        }
        popup.add(mitemDeleteProject);
        return popup;
    }

    public static JPopupMenu getGroupMenu(ArrayList<ProjectItemNode> nodeList, JTree myTree) {
        JMenuItem mitemAddSubGroup = null;
        int groupType = ((GroupNode) nodeList.get(0)).groupType;
        switch (groupType) {
            case GroupNode.GROUP_SYSTEM_FONTS:
                mitemAddSubGroup = new JMenuItem("Add Fonts");
                break;
            case GroupNode.GROUP_DATA_LOCAL:
                mitemAddSubGroup = new JMenuItem("Add Boot Animations");
                break;
            case GroupNode.GROUP_SYSTEM_MEDIA:
                mitemAddSubGroup = new JMenuItem("Add Boot Animations");
                break;
            default:
                mitemAddSubGroup = new JMenuItem("Add SubGroup");
        }
        mitemAddSubGroup.addActionListener(
                (ActionEvent ae) -> {
                    switch (groupType) {
                        case GroupNode.GROUP_SYSTEM_FONTS:
                            addName("SubGroup", "system/fonts", nodeList.get(0));
                            break;
                        case GroupNode.GROUP_DATA_LOCAL:
                            addName("SubGroup", "data/local", nodeList.get(0));
                            break;
                        case GroupNode.GROUP_SYSTEM_MEDIA:
                            addName("SubGroup", "system/media", nodeList.get(0));
                            break;
                    }
                }
        );
        JMenuItem mitemAddFolder = new JMenuItem("Add Folder");
        mitemAddFolder.addActionListener((ActionEvent ae) -> {
            addName("Folder", "", nodeList.get(0));
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
        if (((GroupNode) nodeList.get(0)).groupType == GroupNode.GROUP_AROMA_THEMES) {
            mitemDeleteGroup.setText("Delete Theme(s)");
        }
        popup = new JPopupMenu();
        if (nodeList.size() == 1) {
            switch (((GroupNode) nodeList.get(0)).groupType) {
                case GroupNode.GROUP_SYSTEM_APK:
                case GroupNode.GROUP_SYSTEM_PRIV_APK:
                case GroupNode.GROUP_DATA_APP:
                    popup.add(mitemAddFolder);
                    popup.add(mitemAddFile);
                    break;
                case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                case GroupNode.GROUP_AROMA_THEMES:
                    popup.add(mitemAddFile);
                    break;
                case GroupNode.GROUP_SYSTEM_FONTS:
                case GroupNode.GROUP_DATA_LOCAL:
                case GroupNode.GROUP_SYSTEM_MEDIA:
                    popup.add(mitemAddSubGroup);
                    break;
            }

        }
        popup.add(mitemDeleteGroup);
        return popup;
    }

    public static JPopupMenu getSubGroupMenu(ArrayList<ProjectItemNode> nodeList) {
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
            addName("Folder", "", nodeList.get(0));
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
        switch (nodeList.get(0).type) {
            case ProjectNode.NODE_GROUP:
                GroupNode gNode = (GroupNode) nodeList.get(0);
                for (File tempFile : MyFileFilter.getSelectedFiles(gNode.extension)) {
                    FileNode fnode;
                    if (Preferences.IsFromLollipop) {
                        switch (gNode.groupType) {
                            case GroupNode.GROUP_SYSTEM_APK:
                            case GroupNode.GROUP_SYSTEM_PRIV_APK:
                            case GroupNode.GROUP_DATA_APP:
                                String folderName = tempFile.getName().replaceFirst("[.][^.]+$", "");
                                //it is okay to remove following condition as this is handled directly while creating folder node
                                if (gNode.groupType == GroupNode.GROUP_DATA_APP) {
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
            case ProjectNode.NODE_SUBGROUP:
                for (File tempFile : MyFileFilter.getSelectedFiles(((SubGroupNode) nodeList.get(0)).extension)) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (SubGroupNode) nodeList.get(0));
                    //fnode.fileSourcePath = tempFile.getAbsolutePath();
                    ((SubGroupNode) nodeList.get(0)).addChild(fnode, true);
                }
                break;
            case ProjectNode.NODE_FOLDER:
                for (File tempFile : MyFileFilter.getSelectedFiles("folder")) {
                    FileNode fnode = new FileNode(tempFile.getAbsolutePath(), (FolderNode) nodeList.get(0));
                    ((FolderNode) nodeList.get(0)).addChild(fnode, true);
                }
                break;
        }

    }

    public static void deleteNode(ArrayList<ProjectItemNode> nodeList) {
        for (ProjectItemNode node : nodeList) {
            node.removeMe();
        }
    }

    public static void addName(String nodeType, String location, ProjectItemNode node) {
        int groupType = -1;
        String extension = "";
        boolean isSelectBox = false;
        switch (location) {
            case "system/app":
                groupType = GroupNode.GROUP_SYSTEM_APK;
                extension = ".apk";
                break;
            case "system/priv-app":
                groupType = GroupNode.GROUP_SYSTEM_PRIV_APK;
                extension = ".apk";
                break;
            case "data/app":
                groupType = GroupNode.GROUP_DATA_APP;
                extension = ".apk";
                break;
            case "system/media/audio/alarms":
                groupType = GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS;
                extension = ".acc||.mp3||.m4a||.wav||.ogg";
                break;
            case "system/media/audio/notifications":
                groupType = GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS;
                extension = ".acc||.mp3||.m4a||.wav||.ogg";
                break;
            case "system/media/audio/ringtones":
                groupType = GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES;
                extension = ".acc||.mp3||.m4a||.wav||.ogg";
                break;
            case "system/media/audio/ui":
                groupType = GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI;
                extension = ".acc||.mp3||.m4a||.wav||.ogg";
                break;
            case "system/fonts":
                groupType = GroupNode.GROUP_SYSTEM_FONTS;
                extension = ".tff";
                isSelectBox = true;
                break;
            case "system/media":
                groupType = GroupNode.GROUP_SYSTEM_MEDIA;
                extension = ".zip";
                isSelectBox = true;
                break;
            case "data/local":
                groupType = GroupNode.GROUP_DATA_LOCAL;
                extension = ".zip";
                isSelectBox = true;
                break;
            case "themes":
                groupType = GroupNode.GROUP_AROMA_THEMES;
                extension = ".png||.prop";
                isSelectBox = true;
                break;
        }
        switch (nodeType) {
            case "Group":
                addGNUI = new AddName(location, groupType, extension, isSelectBox, node);
                break;
            case "SubGroup":
                addGNUI = new AddName(location, groupType, extension, isSelectBox, node);
                break;
            case "Project":
                addGNUI = new AddName("Project", ProjectNode.PROJECT_AROMA, node);
                break;
            case "Folder":
                addGNUI = new AddName("Folder", ProjectItemNode.NODE_FOLDER, node);
                break;

            case "Theme":
                addGNUI = new AddName(nodeType, groupType, extension, isSelectBox, node);
                break;
        }

    }
}
