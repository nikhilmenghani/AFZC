/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.FlashableZipCreator;
import flashablezipcreator.Protocols.Types;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Nikhil
 */
public class NodeRenderer extends DefaultTreeCellRenderer {

    public ImageIcon iconRoot = new ImageIcon(FlashableZipCreator.class.getResource("res/root.png"));
    public ImageIcon iconThemeProject = new ImageIcon(FlashableZipCreator.class.getResource("res/themes.png"));
    public ImageIcon iconAromaProject = new ImageIcon(FlashableZipCreator.class.getResource("res/projects.png"));
    public ImageIcon iconSystemGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconDataGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconFontsGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconThemeGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/theme.png"));
    public ImageIcon iconBAGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconMusicGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconKernelGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconKernelSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconFontsSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconBASubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));
    public ImageIcon iconFolder = new ImageIcon(FlashableZipCreator.class.getResource("res/folder.png"));

    public ImageIcon iconFile = new ImageIcon(FlashableZipCreator.class.getResource("res/file.png"));
    public ImageIcon iconApkFile = new ImageIcon(FlashableZipCreator.class.getResource("res/apk-file-format.png"));
    public ImageIcon iconAlarmFile = new ImageIcon(FlashableZipCreator.class.getResource("res/alarm.png"));
    public ImageIcon iconNotificationFile = new ImageIcon(FlashableZipCreator.class.getResource("res/notification.png"));
    public ImageIcon iconRingtoneFile = new ImageIcon(FlashableZipCreator.class.getResource("res/ringtone.png"));
    public ImageIcon iconUIFile = new ImageIcon(FlashableZipCreator.class.getResource("res/ui.png"));
    public ImageIcon iconZipFile = new ImageIcon(FlashableZipCreator.class.getResource("res/Winrar.png"));
    public ImageIcon iconFontFile = new ImageIcon(FlashableZipCreator.class.getResource("res/text.png"));
    public ImageIcon iconLibFile = new ImageIcon(FlashableZipCreator.class.getResource("res/dll.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, leaf, leaf, row, leaf);
        setNodeIcon((ProjectItemNode) value);
        return this;
    }

    private void setNodeIcon(ProjectItemNode node) {
        switch (node.prop.type) {
            case Types.NODE_ROOT:
                setIcon(iconRoot);
                break;
            case Types.NODE_PROJECT:
                ProjectNode pNode = (ProjectNode) node;
                switch (pNode.prop.projectType) {
                    case Types.PROJECT_THEMES:
                        setIcon(iconThemeProject);
                        break;
                    case Types.PROJECT_GAPPS:
                        setIcon(iconAromaProject);
                        break;
                    default:
                        setIcon(iconAromaProject);
                        break;
                }
                break;
            case Types.NODE_GROUP:
                GroupNode gNode = (GroupNode) node;
                if (((ProjectNode) gNode.prop.parent).prop.projectType == Types.PROJECT_THEMES) {
                    setIcon(iconThemeGroup);
                } else {
                    switch (gNode.prop.groupType) {
                        case Types.GROUP_SYSTEM:
                        case Types.GROUP_SYSTEM_APK:
                        case Types.GROUP_SYSTEM_PRIV_APK:
                        case Types.GROUP_SYSTEM_BIN:
                        case Types.GROUP_SYSTEM_XBIN:
                        case Types.GROUP_SYSTEM_ETC:
                        case Types.GROUP_SYSTEM_FRAMEWORK:
                        case Types.GROUP_SYSTEM_LIB:
                        case Types.GROUP_SYSTEM_LIB64:
                            setIcon(iconSystemGroup);
                            break;
                        case Types.GROUP_DATA_APP:
                            setIcon(iconDataGroup);
                            break;
                        case Types.GROUP_SYSTEM_FONTS:
                            setIcon(iconFontsGroup);
                            break;
                        case Types.GROUP_DATA_LOCAL:
                        case Types.GROUP_SYSTEM_MEDIA:
                            setIcon(iconBAGroup);
                            break;
                        case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                        case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                        case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                        case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                            setIcon(iconMusicGroup);
                            break;
                        default:
                            setIcon(iconGroup);
                    }
                }
                break;
            case Types.NODE_SUBGROUP:
                SubGroupNode sgNode = (SubGroupNode) node;
                switch (sgNode.prop.subGroupType) {
                    case Types.GROUP_SYSTEM_FONTS:
                        setIcon(iconFontsSubGroup);
                        break;
                    case Types.GROUP_SYSTEM_MEDIA:
                    case Types.GROUP_DATA_LOCAL:
                        setIcon(iconBASubGroup);
                        break;
                }
                break;
            case Types.NODE_FOLDER:
                setIcon(iconFolder);
                break;
            case Types.NODE_FILE:
                if (node.prop.title.endsWith(".apk")) {
                    setIcon(iconApkFile);
                } else if (node.prop.title.endsWith(".zip")) {
                    setIcon(iconZipFile);
                } else if (node.prop.title.endsWith(".ttf")) {
                    setIcon(iconFontFile);
                } else if (node.prop.title.endsWith(".so")) {
                    setIcon(iconLibFile);
                } else if (node.prop.parent.prop.groupType == Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS) {
                    setIcon(iconAlarmFile);
                } else if (node.prop.parent.prop.groupType == Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS) {
                    setIcon(iconNotificationFile);
                } else if (node.prop.parent.prop.groupType == Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES) {
                    setIcon(iconRingtoneFile);
                } else if (node.prop.parent.prop.groupType == Types.GROUP_SYSTEM_MEDIA_AUDIO_UI) {
                    setIcon(iconUIFile);
                } else {
                    setIcon(iconFile);
                }
                break;
            case Types.NODE_DELETE:
                setIcon(iconFile);
                break;
        }
    }
}
