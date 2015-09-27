/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.FlashableZipCreator;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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
    public ImageIcon iconSystemGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/system.png"));
    public ImageIcon iconDataGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/data.png"));
    public ImageIcon iconFontsGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/fonts.png"));
    public ImageIcon iconThemeGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/theme.png"));
    public ImageIcon iconBAGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/bootanimations.png"));
    public ImageIcon iconMusicGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/music.png"));
    public ImageIcon iconKernelGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/kernels.png"));
    public ImageIcon iconKernelSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/kernel.png"));
    public ImageIcon iconGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/project.png"));
    public ImageIcon iconSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/Documents-icon.png"));
    public ImageIcon iconFontsSubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/font.png"));
    public ImageIcon iconBASubGroup = new ImageIcon(FlashableZipCreator.class.getResource("res/bootanimation.png"));
    
    public ImageIcon iconFile = new ImageIcon(FlashableZipCreator.class.getResource("res/Actions-document-edit-icon.png"));
    public ImageIcon iconFontFile = new ImageIcon(FlashableZipCreator.class.getResource("res/fontsfile.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, leaf, leaf, row, leaf);
        ProjectItemNode node = (ProjectItemNode) value;

        switch (node.getType()) {
            case ProjectItemNode.NODE_ROOT:
                setIcon(iconRoot);
                break;
            case ProjectItemNode.NODE_PROJECT:
                ProjectNode pNode = (ProjectNode) node;
                if (pNode.projectType == ProjectNode.PROJECT_THEMES) {
                    setIcon(iconThemeProject);
                } else {
                    setIcon(iconAromaProject);
                }
                break;
            case ProjectItemNode.NODE_GROUP:
                GroupNode gNode = (GroupNode) node;
                if (((ProjectNode) gNode.parent).projectType == ProjectNode.PROJECT_THEMES) {
                    setIcon(iconThemeGroup);
                } else {
                    switch (gNode.groupType) {
                        case GroupNode.GROUP_SYSTEM_APK:
                        case GroupNode.GROUP_SYSTEM_CSC:
                        case GroupNode.GROUP_SYSTEM_ETC:
                        case GroupNode.GROUP_SYSTEM_FRAMEWORK:
                        case GroupNode.GROUP_SYSTEM_LIB:
                        case GroupNode.GROUP_SYSTEM_PRIV_APK:
                        case GroupNode.GROUP_PRELOAD_SYMLINK_SYSTEM_APP:
                            setIcon(iconSystemGroup);
                            break;
                        case GroupNode.GROUP_DATA_APP:
                            setIcon(iconDataGroup);
                            break;
                        case GroupNode.GROUP_SYSTEM_FONTS:
                            setIcon(iconFontsGroup);
                            break;
                        case GroupNode.GROUP_DATA_LOCAL:
                        case GroupNode.GROUP_SYSTEM_MEDIA:
                            setIcon(iconBAGroup);
                            break;
                        case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                        case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                        case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                        case GroupNode.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                            setIcon(iconMusicGroup);
                            break;
                        case GroupNode.GROUP_AROMA_KERNEL:
                            setIcon(iconKernelGroup);
                            break;
                        default:
                            setIcon(iconGroup);
                    }
                }
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                SubGroupNode sgNode = (SubGroupNode) node;
                switch (sgNode.subGroupType) {
                    case SubGroupNode.TYPE_SYSTEM_FONTS:
                        setIcon(iconFontsSubGroup);
                        break;
                    case SubGroupNode.TYPE_SYSTEM_MEDIA:
                    case SubGroupNode.TYPE_DATA_LOCAL:
                        setIcon(iconBASubGroup);
                        break;
                    case SubGroupNode.TYPE_KERNEL:
                        setIcon(iconKernelSubGroup);
                        break;
                }
                break;
            case ProjectItemNode.NODE_FILE:
                setIcon(iconFile);
                break;
        }
        return this;
    }
}
