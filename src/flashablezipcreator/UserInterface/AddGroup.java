/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.UserInterface;

import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.NodeProperties;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Protocols.Types;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class AddGroup extends javax.swing.JFrame {

    /**
     * Creates new form CustomGroupUI
     */
    public ProjectNode pNode = null;
    public GroupNode gNode = null;
    public String title = "Group";
    public String location = "";
    public String owner = "";
    public String group = "";
    public String perm = "";
    public boolean modifyPerms = true;
    public boolean isSingleFileGroup = false;
    public int groupType;
    public int parentType;
    public int headerWidth = 30;

    public AddGroup(ProjectNode project) {
        this.pNode = project;
        initComponents();
    }

    public AddGroup(int groupType, ProjectItemNode node) {
        this.groupType = groupType;
        switch (node.prop.type) {
            case Types.NODE_PROJECT:
                this.pNode = (ProjectNode) node;
                this.parentType = Types.NODE_PROJECT;
                break;
            case Types.NODE_GROUP:
                this.gNode = (GroupNode) node;
                this.parentType = Types.NODE_GROUP;
                break;
        }
        switch (groupType) {
            case Types.GROUP_SYSTEM:
                location = "/system";
                owner = "0";
                group = "0";
                perm = "0644";
                title = "System Group";
                headerWidth = 26;
                break;
            case Types.GROUP_SYSTEM_APK:
                location = "/system/app";
                owner = "0";
                group = "0";
                perm = "0644";
                title = "System Apps Group";
                headerWidth = 26;
                break;
            case Types.GROUP_SYSTEM_PRIV_APK:
                location = "/system/priv-app";
                owner = "0";
                group = "0";
                perm = "0644";
                title = "Priv Apps Group";
                break;
            case Types.GROUP_SYSTEM_BIN:
                location = "/system/bin";
                owner = "0";
                group = "2000";
                perm = "0755";
                title = "System Bin Group";
                break;
            case Types.GROUP_SYSTEM_ETC:
                location = "/system/etc";
                owner = "0";
                group = "0";
                perm = "0644";
                title = "System Etc Group";
                break;
            case Types.GROUP_SYSTEM_FRAMEWORK:
                location = "/system/framework";
                owner = "0";
                group = "0";
                perm = "0644";
                title = "System Framework Group";
                headerWidth = 24;
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_ALARMS:
                location = "/system/media/audio/alarms";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = "Alarms Group";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_NOTIFICATIONS:
                location = "/system/media/audio/notifications";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = "Notifications Group";
                headerWidth = 28;
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_RINGTONES:
                location = "/system/media/audio/ringtones";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = "Ringtones Group";
                break;
            case Types.GROUP_SYSTEM_MEDIA_AUDIO_UI:
                location = "/system/media/audio/ui";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = "UI Tones Group";
                break;
            case Types.GROUP_SYSTEM_MEDIA:
                location = "/system/media";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = pNode != null ? "Boot Animations Group" : "Boot Animation";
                headerWidth = 24;
                isSingleFileGroup = true;
                break;
            case Types.GROUP_SYSTEM_FONTS:
                location = "/system/fonts";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = pNode != null ? "Fonts Group" : "Font";
                isSingleFileGroup = true;
                break;
            case Types.GROUP_DATA_APP:
                location = "/data/app";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = "Data Apps Group";
                break;
            case Types.GROUP_DATA_LOCAL:
                location = "/data/local";
                owner = "1000";
                group = "1000";
                perm = "0644";
                title = pNode != null ? "Boot Animations Group" : "Boot Animation";
                headerWidth = 24;
                isSingleFileGroup = true;
                break;
            case Types.GROUP_CUSTOM:
                title = "Custom Group";
                break;
            case Types.GROUP_MOD:
                break;
            case Types.GROUP_AROMA_THEMES:
                break;
            case Types.GROUP_DELETE_FILES:
                break;
            case Types.GROUP_SCRIPT:
                break;
        }
        if (!location.equals("")) {
            modifyPerms = false;
        }
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          

    private void initComponents() {

        dialog = new JDialog(this, "Add " + title, true);
        dialog.setResizable(false);

        customrbGroup = new javax.swing.ButtonGroup();
        customPanelMain = new javax.swing.JPanel();
        custombtnAdd = new javax.swing.JButton();
        custompanelPermissions = new javax.swing.JPanel();
        customcbOthersX = new javax.swing.JCheckBox();
        customcbOwnerW = new javax.swing.JCheckBox();
        customcbGroupW = new javax.swing.JCheckBox();
        customcbSticky = new javax.swing.JCheckBox();
        customlblR = new javax.swing.JLabel();
        customlblOwner = new javax.swing.JLabel();
        customlblX = new javax.swing.JLabel();
        customcbOwnerR = new javax.swing.JCheckBox();
        customlblPermissions = new javax.swing.JLabel();
        customllblW = new javax.swing.JLabel();
        customlblGroup = new javax.swing.JLabel();
        customcbGroupX = new javax.swing.JCheckBox();
        customcbSetuid = new javax.swing.JCheckBox();
        customcbGroupR = new javax.swing.JCheckBox();
        customcbSetgid = new javax.swing.JCheckBox();
        customcbOthersW = new javax.swing.JCheckBox();
        customcbOthersR = new javax.swing.JCheckBox();
        customlblOthers = new javax.swing.JLabel();
        customcbOwnerX = new javax.swing.JCheckBox();
        customcbPermissions = new javax.swing.JCheckBox();
        customlblPermInString = new javax.swing.JLabel();
        customtxtOwner = new PHTextField("Owner(1000)");
        customtxtOwner.setText(owner);
        customtxtGroup = new PHTextField("Group(1000)");
        customtxtGroup.setText(group);
        customtxtPerm = new PHTextField("Perm(0000)");
        customtxtPerm.setText(perm);
        setPermissionsFromDigit(perm);
        custompanelGroupDetails = new javax.swing.JPanel();
        customtxtGroupName = new PHTextField("Enter " + title + " Name");
        customtxtInstallLocation = new PHTextField("Enter Install Location (/system/app)");
        customtxtInstallLocation.setText(location);
        customtxtInstallLocation.setEnabled(modifyPerms);
        customrbSingleFile = new javax.swing.JRadioButton();
        customrbSingleFile.setEnabled(modifyPerms);
        customrbMultipleFiles = new javax.swing.JRadioButton();
        customrbMultipleFiles.setEnabled(modifyPerms);
        custompanelHeader = new javax.swing.JPanel();
        customlblHeader = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        customPanelMain.setBackground(new java.awt.Color(255, 255, 255));

        custombtnAdd.setBackground(new java.awt.Color(255, 255, 255));
        custombtnAdd.setText("Add");
        custombtnAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        custombtnAdd.setContentAreaFilled(false);
        custombtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custombtnAddActionPerformed(evt);
            }
        });

        custompanelPermissions.setBackground(new java.awt.Color(255, 255, 255));

        customcbOthersX.setBackground(new java.awt.Color(255, 255, 255));
        customcbOthersX.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOthersXItemStateChanged(evt);
            }
        });

        customcbOwnerW.setBackground(new java.awt.Color(255, 255, 255));
        customcbOwnerW.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOwnerWItemStateChanged(evt);
            }
        });

        customcbGroupW.setBackground(new java.awt.Color(255, 255, 255));
        customcbGroupW.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbGroupWItemStateChanged(evt);
            }
        });

        customcbSticky.setBackground(new java.awt.Color(255, 255, 255));
        customcbSticky.setText("sticky");
        customcbSticky.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbStickyItemStateChanged(evt);
            }
        });

        customlblR.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customlblR.setText(" R");
        customlblR.setPreferredSize(new java.awt.Dimension(21, 21));

        customlblOwner.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customlblOwner.setText("Owner");

        customlblX.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customlblX.setText("  X");
        customlblX.setPreferredSize(new java.awt.Dimension(21, 21));

        customcbOwnerR.setBackground(new java.awt.Color(255, 255, 255));
        customcbOwnerR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOwnerRItemStateChanged(evt);
            }
        });

        customlblPermissions.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customlblPermissions.setText("Permissions");

        customllblW.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customllblW.setText(" W");
        customllblW.setPreferredSize(new java.awt.Dimension(21, 21));

        customlblGroup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customlblGroup.setText("Group");

        customcbGroupX.setBackground(new java.awt.Color(255, 255, 255));
        customcbGroupX.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbGroupXItemStateChanged(evt);
            }
        });

        customcbSetuid.setBackground(new java.awt.Color(255, 255, 255));
        customcbSetuid.setText("setuid");
        customcbSetuid.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbSetuidItemStateChanged(evt);
            }
        });

        customcbGroupR.setBackground(new java.awt.Color(255, 255, 255));
        customcbGroupR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbGroupRItemStateChanged(evt);
            }
        });

        customcbSetgid.setBackground(new java.awt.Color(255, 255, 255));
        customcbSetgid.setText("setgid");
        customcbSetgid.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbSetgidItemStateChanged(evt);
            }
        });

        customcbOthersW.setBackground(new java.awt.Color(255, 255, 255));
        customcbOthersW.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOthersWItemStateChanged(evt);
            }
        });

        customcbOthersR.setBackground(new java.awt.Color(255, 255, 255));
        customcbOthersR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOthersRItemStateChanged(evt);
            }
        });

        customlblOthers.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customlblOthers.setText("Others");

        customcbOwnerX.setBackground(new java.awt.Color(255, 255, 255));
        customcbOwnerX.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbOwnerXItemStateChanged(evt);
            }
        });

        customcbPermissions.setBackground(new java.awt.Color(255, 255, 255));
        customcbPermissions.setToolTipText("Uncheck this if location is sdcard, otherwise check");
        customcbPermissions.setSelected(modifyPerms);
        customcbPermissions.setEnabled(modifyPerms);
        modifyPermissionsState();
        customcbPermissions.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customcbPermissionsItemStateChanged(evt);
            }
        });

        customlblPermInString.setBackground(new java.awt.Color(255, 255, 255));
        customlblPermInString.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customlblPermInString.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customlblPermInString.setText("----------");
        customlblPermInString.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        customtxtOwner.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        customtxtOwner.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customtxtOwner.setToolTipText("Owner (e.g. 0 for root 1000 for system)");
        customtxtOwner.setPreferredSize(new java.awt.Dimension(82, 22));

        customtxtGroup.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        customtxtGroup.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customtxtGroup.setToolTipText("Group (e.g. 0 for root 1000 for system)");
        customtxtGroup.setPreferredSize(new java.awt.Dimension(82, 22));

        customtxtPerm.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        customtxtPerm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customtxtPerm.setToolTipText("Permissions (e.g. 0644, 0755)");
        customtxtPerm.setPreferredSize(new java.awt.Dimension(82, 22));
        customtxtPerm.setEnabled(false);

        javax.swing.GroupLayout custompanelPermissionsLayout = new javax.swing.GroupLayout(custompanelPermissions);
        custompanelPermissions.setLayout(custompanelPermissionsLayout);
        custompanelPermissionsLayout.setHorizontalGroup(
                custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, custompanelPermissionsLayout.createSequentialGroup()
                                .addContainerGap(24, Short.MAX_VALUE)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(customlblGroup)
                                                        .addComponent(customlblOthers)
                                                        .addComponent(customlblOwner))
                                                .addGap(18, 18, 18)
                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                                                .addComponent(customcbGroupR)
                                                                                .addGap(40, 40, 40)
                                                                                .addComponent(customcbGroupW))
                                                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(customlblR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(customcbOwnerR))
                                                                                .addGap(40, 40, 40)
                                                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(customcbOwnerW)
                                                                                        .addComponent(customllblW, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                .addGap(40, 40, 40)
                                                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(customcbOwnerX)
                                                                        .addComponent(customcbGroupX)
                                                                        .addComponent(customlblX, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                                .addComponent(customcbOthersR)
                                                                .addGap(40, 40, 40)
                                                                .addComponent(customcbOthersW)
                                                                .addGap(40, 40, 40)
                                                                .addComponent(customcbOthersX))
                                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                                .addGap(38, 38, 38)
                                                                .addComponent(customcbSetgid)
                                                                .addGap(38, 38, 38)
                                                                .addComponent(customcbSticky))))
                                        .addComponent(customcbSetuid)
                                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(customcbPermissions)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(customlblPermissions)))
                                .addGap(9, 9, 9))
                        .addComponent(customlblPermInString, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(customtxtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(customtxtGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(customtxtPerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        custompanelPermissionsLayout.setVerticalGroup(
                custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(custompanelPermissionsLayout.createSequentialGroup()
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(customlblPermissions)
                                        .addComponent(customcbPermissions))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customlblR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customllblW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customlblX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(customcbOwnerR)
                                        .addComponent(customcbOwnerW)
                                        .addComponent(customcbOwnerX)
                                        .addComponent(customlblOwner, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(7, 7, 7)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(customlblGroup)
                                        .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(customcbGroupR)
                                                .addComponent(customcbGroupW)
                                                .addComponent(customcbGroupX)))
                                .addGap(7, 7, 7)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(customlblOthers)
                                        .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(customcbOthersR)
                                                .addComponent(customcbOthersW)
                                                .addComponent(customcbOthersX)))
                                .addGap(7, 7, 7)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customcbSetuid)
                                        .addComponent(customcbSticky)
                                        .addComponent(customcbSetgid))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(customlblPermInString, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(custompanelPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customtxtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customtxtGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customtxtPerm, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        custompanelGroupDetails.setBackground(new java.awt.Color(255, 255, 255));

        customtxtGroupName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customtxtGroupName.setToolTipText("This will contain set of files you will choose from in Aroma Installer");

        customtxtInstallLocation.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        customtxtInstallLocation.setToolTipText("This is the location where your files will be installed (e.g. \\system\\framework)");

        customrbSingleFile.setBackground(new java.awt.Color(255, 255, 255));
        customrbGroup.add(customrbSingleFile);
        customrbSingleFile.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customrbSingleFile.setSelected(isSingleFileGroup);
        customrbSingleFile.setText("Install Single File");
        customrbSingleFile.setToolTipText("Required in Groups such as BootAnimations, Fonts, Kernels, etc where you can only choose one from the list");

        customrbMultipleFiles.setBackground(new java.awt.Color(255, 255, 255));
        customrbGroup.add(customrbMultipleFiles);
        customrbMultipleFiles.setSelected(!isSingleFileGroup);
        customrbMultipleFiles.setText("Install Multiple Files");
        customrbMultipleFiles.setToolTipText("Required in Groups where you can only choose multiple files from the list");

        javax.swing.GroupLayout custompanelGroupDetailsLayout = new javax.swing.GroupLayout(custompanelGroupDetails);
        custompanelGroupDetails.setLayout(custompanelGroupDetailsLayout);
        custompanelGroupDetailsLayout.setHorizontalGroup(
                custompanelGroupDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(custompanelGroupDetailsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(custompanelGroupDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(custompanelGroupDetailsLayout.createSequentialGroup()
                                                .addComponent(customrbSingleFile)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(customrbMultipleFiles))
                                        .addComponent(customtxtInstallLocation)
                                        .addComponent(customtxtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        custompanelGroupDetailsLayout.setVerticalGroup(
                custompanelGroupDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(custompanelGroupDetailsLayout.createSequentialGroup()
                                .addComponent(customtxtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(customtxtInstallLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(custompanelGroupDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(customrbSingleFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(customrbMultipleFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        custompanelHeader.setBackground(new java.awt.Color(0, 102, 102));
        custompanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        customlblHeader.setFont(new java.awt.Font("Tahoma", 0, headerWidth)); // NOI18N
        customlblHeader.setForeground(new java.awt.Color(255, 255, 255));
        customlblHeader.setText("Add " + title);

        javax.swing.GroupLayout custompanelHeaderLayout = new javax.swing.GroupLayout(custompanelHeader);
        custompanelHeader.setLayout(custompanelHeaderLayout);
        custompanelHeaderLayout.setHorizontalGroup(
                custompanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(custompanelHeaderLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(customlblHeader)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        custompanelHeaderLayout.setVerticalGroup(
                custompanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(custompanelHeaderLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(customlblHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout customPanelMainLayout = new javax.swing.GroupLayout(customPanelMain);
        customPanelMain.setLayout(customPanelMainLayout);
        customPanelMainLayout.setHorizontalGroup(
                customPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(custompanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(customPanelMainLayout.createSequentialGroup()
                                .addComponent(custompanelGroupDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customPanelMainLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(custombtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customPanelMainLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(custompanelPermissions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))
        );
        customPanelMainLayout.setVerticalGroup(
                customPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(customPanelMainLayout.createSequentialGroup()
                                .addComponent(custompanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(custompanelGroupDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(custompanelPermissions, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(custombtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(customPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(customPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        updatePermissions();

        dialog.getContentPane().add(customPanelMain);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dialog.dispose();
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Functions">           
    public void modifyPermissionsState() {
        boolean flag = false;
        if (customcbPermissions.isSelected()) {
            flag = true;
        } else if (modifyPerms) {
            customcbGroupR.setSelected(flag);
            customcbGroupW.setSelected(flag);
            customcbGroupX.setSelected(flag);
            customcbOthersR.setSelected(flag);
            customcbOthersW.setSelected(flag);
            customcbOthersX.setSelected(flag);
            customcbOwnerR.setSelected(flag);
            customcbOwnerW.setSelected(flag);
            customcbOwnerX.setSelected(flag);
            customcbSetgid.setSelected(flag);
            customcbSetuid.setSelected(flag);
            customcbSticky.setSelected(flag);
            customtxtOwner.setText("");
            customtxtGroup.setText("");
        }
        customcbGroupR.setEnabled(flag);
        customcbGroupW.setEnabled(flag);
        customcbGroupX.setEnabled(flag);
        customcbOthersR.setEnabled(flag);
        customcbOthersW.setEnabled(flag);
        customcbOthersX.setEnabled(flag);
        customcbOwnerR.setEnabled(flag);
        customcbOwnerW.setEnabled(flag);
        customcbOwnerX.setEnabled(flag);
        customcbSetgid.setEnabled(flag);
        customcbSetuid.setEnabled(flag);
        customcbSticky.setEnabled(flag);
        customtxtOwner.setEnabled(flag);
        customtxtGroup.setEnabled(flag);
    }

    public void updatePermissions() {
        String miscPermissions = getPermissionsInDigit(customcbSetuid.isSelected(), customcbSetgid.isSelected(), customcbSticky.isSelected());
        String ownerPermissions = getPermissionsInDigit(customcbOwnerR.isSelected(), customcbOwnerW.isSelected(), customcbOwnerX.isSelected());
        String groupPermissions = getPermissionsInDigit(customcbGroupR.isSelected(), customcbGroupW.isSelected(), customcbGroupX.isSelected());
        String otherPermissions = getPermissionsInDigit(customcbOthersR.isSelected(), customcbOthersW.isSelected(), customcbOthersX.isSelected());
        customtxtPerm.setText((miscPermissions + ownerPermissions + groupPermissions + otherPermissions).replace("0000", ""));
        ownerPermissions = getPermissionsInString(customcbOwnerR.isSelected(), customcbOwnerW.isSelected(), customcbOwnerX.isSelected());
        if (customcbSetuid.isSelected()) {
            ownerPermissions = ownerPermissions.substring(0, ownerPermissions.length() - 1) + "S";
        }
        groupPermissions = getPermissionsInString(customcbGroupR.isSelected(), customcbGroupW.isSelected(), customcbGroupX.isSelected());
        if (customcbSetgid.isSelected()) {
            groupPermissions = groupPermissions.substring(0, groupPermissions.length() - 1) + "S";
        }
        otherPermissions = getPermissionsInString(customcbOthersR.isSelected(), customcbOthersW.isSelected(), customcbOthersX.isSelected());
        if (customcbSticky.isSelected()) {
            otherPermissions = otherPermissions.substring(0, otherPermissions.length() - 1) + "T";
        }
        customlblPermInString.setText("-" + ownerPermissions + groupPermissions + otherPermissions);
    }

    public void setPermissionsFromDigit(String digit) {
        char misc = '0';
        char owner = '0';
        char group = '0';
        char other = '0';
        digit = digit.equals("") ? "0000" : digit;
        misc = digit.charAt(0);
        owner = digit.charAt(1);
        group = digit.charAt(2);
        other = digit.charAt(3);
        setPerm(misc, customcbSetuid, customcbSetgid, customcbSticky);
        setPerm(owner, customcbOwnerR, customcbOwnerW, customcbOwnerX);
        setPerm(group, customcbGroupR, customcbGroupW, customcbGroupX);
        setPerm(other, customcbOthersR, customcbOthersW, customcbOthersX);
    }

    public void setPerm(char perm, JCheckBox cbR, JCheckBox cbW, JCheckBox cbX) {
        switch (perm) {
            case '0':
                cbR.setSelected(false);
                cbW.setSelected(false);
                cbX.setSelected(false);
                break;
            case '1':
                cbR.setSelected(false);
                cbW.setSelected(false);
                cbX.setSelected(true);
                break;
            case '2':
                cbR.setSelected(false);
                cbW.setSelected(true);
                cbX.setSelected(false);
                break;
            case '3':
                cbR.setSelected(false);
                cbW.setSelected(true);
                cbX.setSelected(true);
                break;
            case '4':
                cbR.setSelected(true);
                cbW.setSelected(false);
                cbX.setSelected(false);
                break;
            case '5':
                cbR.setSelected(true);
                cbW.setSelected(false);
                cbX.setSelected(true);
                break;
            case '6':
                cbR.setSelected(true);
                cbW.setSelected(true);
                cbX.setSelected(false);
                break;
            case '7':
                cbR.setSelected(true);
                cbW.setSelected(true);
                cbX.setSelected(true);
                break;
        }
    }

    public String getPermissionsInDigit(boolean R, boolean W, boolean X) {
        int perm = 0;
        if (R) {
            perm += 4;
            if (W) {
                perm += 2;
                if (X) {
                    perm += 1;
                }
            } else if (X) {
                perm += 1;
            }
        } else if (W) {
            perm += 2;
            if (X) {
                perm += 1;
            }
        } else if (X) {
            perm += 1;
        }
        return String.valueOf(perm);
    }

    public String getPermissionsInString(boolean R, boolean W, boolean X) {
        String perm = "";
        if (R) {
            perm += "r";
            if (W) {
                perm += "w";
                if (X) {
                    perm += "x";
                } else {
                    perm += "-";
                }
            } else if (X) {
                perm += "-x";
            } else {
                perm += "--";
            }
        } else if (W) {
            perm += "-w";
            if (X) {
                perm += "x";
            } else {
                perm += "-";
            }
        } else if (X) {
            perm += "--x";
        } else {
            perm += "---";
        }
        return String.valueOf(perm);
    }

    public boolean isValid(String groupName, String location, String owner, String group, String perm, boolean allowPerm) {
        boolean flag = true;
        if (groupName.equals("")) {
            JOptionPane.showMessageDialog(this, "Group Name cannot be empty", "Validation Message", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (location.equals("")) {
            JOptionPane.showMessageDialog(this, "location cannot be empty", "Validation Message", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!location.startsWith("/")) {
            JOptionPane.showMessageDialog(this, "location name should start with /", "Validation Message", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (location.length() > 1 && location.endsWith("/")) {
            JOptionPane.showMessageDialog(this, "location name should not end with /", "Validation Message", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (allowPerm) {
            if (owner.equals("")) {
                JOptionPane.showMessageDialog(this, "owner cannot be empty (1000 may be?)", "Validation Message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (group.equals("")) {
                JOptionPane.showMessageDialog(this, "group cannot be empty (1000 may be?)", "Validation Message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (perm.equals("")) {
                JOptionPane.showMessageDialog(this, "permissions cannot be empty (0644 may be?)", "Validation Message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Integer.valueOf(owner);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "owner cannot be a string value (1000 may be?)", "Validation Message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Integer.valueOf(group);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "group cannot be a string value (1000 may be?)", "Validation Message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return flag;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Events">           
    private void custombtnAddActionPerformed(java.awt.event.ActionEvent evt) {
        String groupName = customtxtGroupName.getText();
        String loc = customtxtInstallLocation.getText();
        String o = customtxtOwner.getText();
        String g = customtxtGroup.getText();
        String p = customtxtPerm.getText();
        boolean allowPerm = customcbPermissions.isSelected();
        NodeProperties np = null;
        if (isValid(groupName, loc, o, g, p, allowPerm)) {
            switch (this.parentType) {
                case Types.NODE_PROJECT:
                    np = new NodeProperties(groupName, groupType, pNode);
                    np.setPermissions(o, g, p);
                    np.location = loc;
                    np.owner = o;
                    np.group = g;
                    np.perm = p;
                    if (groupType == Types.GROUP_CUSTOM) {
                        np.reloadOriginalStringType();
                        np.reloadZipPath();
                        np.setPermissions = allowPerm;
                    }
                    np.isSelectBox = customrbSingleFile.isSelected();
                    pNode.addChild(new GroupNode(np), false);
                    break;
                case Types.NODE_GROUP:
                    np = new NodeProperties(groupName, groupType, gNode);
                    np.setPermissions(o, g, p);
                    np.location = loc;
                    np.isSelectBox = customrbSingleFile.isSelected();
                    gNode.addChild(new SubGroupNode(np), false);
                    break;
            }
            this.dispose();
        }
    }

    private void customcbOwnerRItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbOwnerWItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbOwnerXItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbGroupRItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbGroupWItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbOthersRItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbOthersWItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbOthersXItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbSetuidItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbSetgidItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbStickyItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    private void customcbPermissionsItemStateChanged(java.awt.event.ItemEvent evt) {
        modifyPermissionsState();
    }

    private void customcbGroupXItemStateChanged(java.awt.event.ItemEvent evt) {
        updatePermissions();
    }

    //</editor-fold>
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddGroupUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddGroupUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddGroupUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddGroupUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddGroupUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    // Variables declaration - do not modify                     
    private javax.swing.JPanel customPanelMain;
    private javax.swing.JButton custombtnAdd;
    private javax.swing.JCheckBox customcbGroupR;
    private javax.swing.JCheckBox customcbGroupW;
    private javax.swing.JCheckBox customcbGroupX;
    private javax.swing.JCheckBox customcbOthersR;
    private javax.swing.JCheckBox customcbOthersW;
    private javax.swing.JCheckBox customcbOthersX;
    private javax.swing.JCheckBox customcbOwnerR;
    private javax.swing.JCheckBox customcbOwnerW;
    private javax.swing.JCheckBox customcbSetgid;
    private javax.swing.JCheckBox customcbSetuid;
    private javax.swing.JCheckBox customcbSticky;
    private javax.swing.JCheckBox customcbOwnerX;
    private javax.swing.JCheckBox customcbPermissions;
    private javax.swing.JLabel customlblDescription;
    private javax.swing.JLabel customlblFileInstructions;
    private javax.swing.JLabel customlblGroup;
    private javax.swing.JLabel customlblGroupName;
    private javax.swing.JLabel customlblHeader;
    private javax.swing.JLabel customlblInstallLocation;
    private javax.swing.JLabel customlblOthers;
    private javax.swing.JLabel customlblOwner;
    private javax.swing.JLabel customlblPermInString;
    private javax.swing.JLabel customlblPermissions;
    private javax.swing.JLabel customlblR;
    private javax.swing.JLabel customlblX;
    private javax.swing.JLabel customllblW;
    private javax.swing.JPanel custompanelGroupDetails;
    private javax.swing.JPanel custompanelHeader;
    private javax.swing.JPanel custompanelPermissions;
    private javax.swing.ButtonGroup customrbGroup;
    private javax.swing.JRadioButton customrbMultipleFiles;
    private javax.swing.JRadioButton customrbSingleFile;
    private javax.swing.JTextField customtxtGroup;
    private javax.swing.JTextField customtxtGroupName;
    private javax.swing.JTextField customtxtInstallLocation;
    private javax.swing.JTextField customtxtOwner;
    private javax.swing.JTextField customtxtPerm;
    public JDialog dialog;
    // End of variables declaration                   
}
