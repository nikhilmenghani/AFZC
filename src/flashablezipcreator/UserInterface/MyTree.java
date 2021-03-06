/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.UserInterface;

import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectTreeBuilder;
import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.FlashableZipCreator;
import flashablezipcreator.Operations.DeviceOperations;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.MyFileFilter;
import flashablezipcreator.Operations.TreeOperations;
import flashablezipcreator.Operations.UpdaterScriptOperations;
import flashablezipcreator.Protocols.Control;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Export;
import flashablezipcreator.Protocols.Import;
import flashablezipcreator.Protocols.Jar;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Update;
import flashablezipcreator.Protocols.Web;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class MyTree extends javax.swing.JFrame {

    /**
     * Creates new form MyTreeUI
     */
    public static DefaultTreeModel model;
    public static ProjectItemNode rootNode;
    public static int progressBarFlag = 0;
    TreeOperations to;
    UpdaterScriptOperations uso;
    MyFileFilter uio = new MyFileFilter();
    public static int maxSize = 0;
    public static int progressValue = 0;

    public MyTree(int cardLayoutNo) throws IOException {
        this();
        MyTree.setCardLayout(cardLayoutNo);
    }

    public MyTree() throws IOException {
        tree = ProjectTreeBuilder.buildTree();
        model = ProjectTreeBuilder.buildModel();
        rootNode = ProjectTreeBuilder.rootNode;
        progressBarImportExport = new JProgressBar();
        if (Jar.isExecutingThrough()) {
            Logs.write("adding themes");
            Jar.addThemesToTree();
            Logs.write("added themes");
        }
        initComponents();
//        Adb.letsBegin();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        layeredPaneHome = new javax.swing.JLayeredPane();
        panelMain = new javax.swing.JPanel();
        panel_logo = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        SP_tree = ProjectTreeBuilder.buildScrollPane();
        //tree = new javax.swing.JTree();
        panelLower = new javax.swing.JPanel();
        layeredPaneButtons = new javax.swing.JLayeredPane();
        panelImportZip = new javax.swing.JPanel();
        btnImportZip = new javax.swing.JButton();
        panelCreateZip = new javax.swing.JPanel();
        btnCreateZip = new javax.swing.JButton();
        layeredPaneProgressBar = new javax.swing.JLayeredPane();
        progressBarImportExport = new javax.swing.JProgressBar();
        layeredPaneProgress = new javax.swing.JLayeredPane();
        panelProgressBar = new javax.swing.JPanel();
        circularProgressBar = new flashablezipcreator.UserInterface.CircularProgressBar();
        txtProgressTitle = new javax.swing.JTextField();
        txtProgressContent = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemPreference = new javax.swing.JMenuItem();
        menuItemExit = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        menuItemSwitchView = new javax.swing.JMenuItem();
        menuItemPushFileToDevice = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenu();
        menuItemDevelopers = new javax.swing.JMenuItem();
        menuItemDonate = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemInstructions = new javax.swing.JMenuItem();
        menuItemCheckForUpdates = new javax.swing.JMenuItem();
        menuDevice = new javax.swing.JMenu();
        menuItemQuickConnect = new javax.swing.JMenuItem();
        menuItemUSB = new javax.swing.JMenuItem();
        menuItemWifi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        panel_logo.setBackground(new java.awt.Color(0, 121, 107));
        panel_logo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHeader.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setText("   Android Flashable Zip Creator");

        lblVersion.setForeground(new java.awt.Color(255, 255, 255));
        lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVersion.setText(FlashableZipCreator.VERSION);

        javax.swing.GroupLayout panel_logoLayout = new javax.swing.GroupLayout(panel_logo);
        panel_logo.setLayout(panel_logoLayout);
        panel_logoLayout.setHorizontalGroup(
                panel_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_logoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panel_logoLayout.setVerticalGroup(
                panel_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_logoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

//        tree.setModel(new FileSystemModel(new File("src")));
//        tree.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
//        tree.setRowHeight(21);
//        tree.setVisibleRowCount(18);
//        SP_tree.setViewportView(tree);
        panelLower.setBackground(new java.awt.Color(255, 255, 255));
        panelLower.setLayout(new java.awt.CardLayout());

        panelImportZip.setBackground(new java.awt.Color(255, 255, 255));

        btnImportZip.setBackground(new java.awt.Color(153, 153, 255));
        btnImportZip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnImportZip.setForeground(new java.awt.Color(62, 39, 35));
        btnImportZip.setText("Import Zip");
        btnImportZip.setBorder(null);
        btnImportZip.setContentAreaFilled(false);
        btnImportZip.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImportZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportZipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelImportZipLayout = new javax.swing.GroupLayout(panelImportZip);
        panelImportZip.setLayout(panelImportZipLayout);
        panelImportZipLayout.setHorizontalGroup(
                panelImportZipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnImportZip, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );
        panelImportZipLayout.setVerticalGroup(
                panelImportZipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnImportZip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        panelCreateZip.setBackground(new java.awt.Color(255, 255, 255));

        btnCreateZip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCreateZip.setForeground(new java.awt.Color(62, 39, 35));
        btnCreateZip.setText("Create Zip");
        btnCreateZip.setBorder(null);
        btnCreateZip.setContentAreaFilled(false);
        btnCreateZip.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreateZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateZipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCreateZipLayout = new javax.swing.GroupLayout(panelCreateZip);
        panelCreateZip.setLayout(panelCreateZipLayout);
        panelCreateZipLayout.setHorizontalGroup(
                panelCreateZipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCreateZip, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );
        panelCreateZipLayout.setVerticalGroup(
                panelCreateZipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCreateZip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layeredPaneButtons.setLayer(panelImportZip, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPaneButtons.setLayer(panelCreateZip, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneButtonsLayout = new javax.swing.GroupLayout(layeredPaneButtons);
        layeredPaneButtons.setLayout(layeredPaneButtonsLayout);
        layeredPaneButtonsLayout.setHorizontalGroup(
                layeredPaneButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredPaneButtonsLayout.createSequentialGroup()
                                .addContainerGap(227, Short.MAX_VALUE)
                                .addComponent(panelImportZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelCreateZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layeredPaneButtonsLayout.setVerticalGroup(
                layeredPaneButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredPaneButtonsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layeredPaneButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelImportZip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelCreateZip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        panelLower.add(layeredPaneButtons, "card1");

        progressBarImportExport.setBackground(new java.awt.Color(255, 255, 255));
        progressBarImportExport.setForeground(new java.awt.Color(0, 121, 107));
        progressBarImportExport.setToolTipText("Click To Change Progress Mode");
        progressBarImportExport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        progressBarImportExport.setDoubleBuffered(true);
        progressBarImportExport.setStringPainted(true);
        progressBarImportExport.setVerifyInputWhenFocusTarget(false);
        progressBarImportExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                progressBarImportExportMouseClicked(evt);
            }
        });

        layeredPaneProgressBar.setLayer(progressBarImportExport, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneProgressBarLayout = new javax.swing.GroupLayout(layeredPaneProgressBar);
        layeredPaneProgressBar.setLayout(layeredPaneProgressBarLayout);
        layeredPaneProgressBarLayout.setHorizontalGroup(
                layeredPaneProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(progressBarImportExport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
        );
        layeredPaneProgressBarLayout.setVerticalGroup(
                layeredPaneProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(progressBarImportExport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
        );

        panelLower.add(layeredPaneProgressBar, "card2");

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
                panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(panelLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(panel_logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SP_tree)
        );
        panelMainLayout.setVerticalGroup(
                panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(panel_logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(SP_tree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(panelLower, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layeredPaneHome.setLayer(panelMain, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneHomeLayout = new javax.swing.GroupLayout(layeredPaneHome);
        layeredPaneHome.setLayout(layeredPaneHomeLayout);
        layeredPaneHomeLayout.setHorizontalGroup(
                layeredPaneHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layeredPaneHomeLayout.setVerticalGroup(
                layeredPaneHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(layeredPaneHome, "L2");

        panelProgressBar.setBackground(new java.awt.Color(255, 255, 255));

        circularProgressBar.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout circularProgressBarLayout = new javax.swing.GroupLayout(circularProgressBar);
        circularProgressBar.setLayout(circularProgressBarLayout);
        circularProgressBarLayout.setHorizontalGroup(
                circularProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 423, Short.MAX_VALUE)
        );
        circularProgressBarLayout.setVerticalGroup(
                circularProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 364, Short.MAX_VALUE)
        );

        txtProgressTitle.setEditable(false);
        txtProgressTitle.setBackground(new java.awt.Color(255, 255, 255));
        txtProgressTitle.setFont(new java.awt.Font("Tahoma", 0, 21)); // NOI18N
        txtProgressTitle.setForeground(new java.awt.Color(0, 121, 107));
        txtProgressTitle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProgressTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtProgressContent.setEditable(false);
        txtProgressContent.setBackground(new java.awt.Color(255, 255, 255));
        txtProgressContent.setFont(new java.awt.Font("Tahoma", 0, 21)); // NOI18N
        txtProgressContent.setForeground(new java.awt.Color(0, 121, 107));
        txtProgressContent.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProgressContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout panelProgressBarLayout = new javax.swing.GroupLayout(panelProgressBar);
        panelProgressBar.setLayout(panelProgressBarLayout);
        panelProgressBarLayout.setHorizontalGroup(
                panelProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(circularProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelProgressBarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtProgressTitle)
                                        .addComponent(txtProgressContent, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                                .addContainerGap())
        );
        panelProgressBarLayout.setVerticalGroup(
                panelProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProgressBarLayout.createSequentialGroup()
                                .addComponent(circularProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProgressTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProgressContent, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(17, Short.MAX_VALUE))
        );

        layeredPaneProgress.setLayer(panelProgressBar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneProgressLayout = new javax.swing.GroupLayout(layeredPaneProgress);
        layeredPaneProgress.setLayout(layeredPaneProgressLayout);
        layeredPaneProgressLayout.setHorizontalGroup(
                layeredPaneProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layeredPaneProgressLayout.setVerticalGroup(
                layeredPaneProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(layeredPaneProgress, "L1");

        menuFile.setText("File");

        menuItemPreference.setText("Preferences");
        menuItemPreference.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    menuItemPreferenceActionPerformed(evt);
                } catch (ParserConfigurationException | SAXException | IOException ex) {
                    Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuFile.add(menuItemPreference);

        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemExit);

        menuBar.add(menuFile);

        menuView.setText("View");

        menuItemSwitchView.setText("Switch View");
        menuItemSwitchView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSwitchViewActionPerformed(evt);
            }
        });
        menuView.add(menuItemSwitchView);

        menuBar.add(menuView);

        menuDevice.setText("Device");

        menuItemQuickConnect.setText("Quick Connect");
        menuItemQuickConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemQuickConnectActionPerformed(evt);
            }
        });
        menuDevice.add(menuItemQuickConnect);

        menuItemWifi.setText("Connect Via Wifi");
        menuItemWifi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemWifiActionPerformed(evt);
            }
        });
        menuDevice.add(menuItemWifi);

        menuItemPushFileToDevice.setText("Push File To Device");
        menuItemPushFileToDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPushFileToDeviceActionPerformed(evt);
            }
        });
        menuDevice.add(menuItemPushFileToDevice);

        menuBar.add(menuDevice);

        menuAbout.setText("About");

        menuItemDevelopers.setText("Developers");
        menuItemDevelopers.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDevelopersActionPerformed(evt);
            }
        });
        menuAbout.add(menuItemDevelopers);

        menuItemDonate.setText("Donate");
        menuItemDonate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDonateActionPerformed(evt);
            }
        });
        menuAbout.add(menuItemDonate);

        menuBar.add(menuAbout);

        menuHelp.setText("Help");

        menuItemInstructions.setText("Instructions");
        menuItemInstructions.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    menuItemInstructionsActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuHelp.add(menuItemInstructions);

        menuItemCheckForUpdates.setText("Check For Update");
        menuItemCheckForUpdates.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    menuItemCheckForUpdatesActionPerformed(evt);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuHelp.add(menuItemCheckForUpdates);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        getAccessibleContext().setAccessibleParent(layeredPaneHome);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>            
    //<editor-fold defaultstate="collapsed" desc="Event Handling ">

    private void btnImportZipActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String importFrom = MyFileFilter.browseZipDestination();
            if (importFrom != null) {
                Thread importZip = new Thread(new Import(importFrom), "ImportZip");
                importZip.start();
            }
        } catch (Exception e) {
            Logs.write(e.getMessage());
            setCardLayout(1);
        }
    }

    private void btnCreateZipActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Project.outputPath = MyFileFilter.browseZipDestination();
            if (!Project.outputPath.equals("")) {
                Thread exportZip = new Thread(new Export(), "ExportZip");
                exportZip.start();
            }
        } catch (Exception e) {
            Logs.write(e.getMessage());
            setCardLayout(1);
        }
    }

    private void progressBarImportExportMouseClicked(java.awt.event.MouseEvent evt) {
        //switch case for future extensibility
        switch (progressBarFlag) {
            case 0:
                progressBarFlag = 1;
                break;
            case 1:
                progressBarFlag = 0;
                break;
        }
    }

    private void menuItemPreferenceActionPerformed(java.awt.event.ActionEvent evt) throws ParserConfigurationException, SAXException, IOException {
        new Preference();
    }

    private void menuItemDevelopersActionPerformed(java.awt.event.ActionEvent evt) {
        new About();
    }

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String dir = "AFZC Projects";
            if ((new File(dir).exists())) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Do you want to delete temporary files?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    deleteDirectory(new File(dir));
                    System.out.println("Window Closing..");
                    System.exit(0);
                } else if (dialogResult == JOptionPane.NO_OPTION) {
                    System.out.println("Window Closing..");
                    System.exit(0);
                }
            } else {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to Exit?", "", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            Logs.write("Deleting Temporary Files");
            Logs.write(Logs.getExceptionTrace(e));
            JOptionPane.showMessageDialog(this, "Something Went Wrong!\nCouldn't delete Temp files!");
        }
    }

    private void menuItemInstructionsActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
//        new Instructions(); //till i make a new UI, exporting instructions to root directory
        try {
            if (Jar.isExecutingThrough()) {
                Write w = new Write();
                w.writeStringToFile(JarOperations.instructions, "Instructions.txt");
                JOptionPane.showMessageDialog(null, "File Exported, check for Instructions.txt in the same directory as of jar file.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong!\n Delete unwanted files from root directory and try again!");
        }
    }

    private void menuItemCheckForUpdatesActionPerformed(java.awt.event.ActionEvent evt) throws URISyntaxException {
        Control.check();
        String availableVersion = "";
        if (Web.netIsAvailable()) {
            if (Control.forceCheckOnStartUp) {
                availableVersion = Update.runUpdateCheck();
            } else if (Preference.pp.checkUpdatesOnStartUp) {
                availableVersion = Update.runUpdateCheck();
            }
            if (!availableVersion.equals("")) {
                Update.executeDownload();
            } else {
                JOptionPane.showMessageDialog(this, "Your version is Up-to-date!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Check Yout Internet Connection!!");
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        try {
            String dir = "AFZC Projects";
            if ((new File(dir).exists())) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Do you want to delete temporary files?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    deleteDirectory(new File(dir));
                    System.out.println("Window Closing..");
                    System.exit(0);
                } else if (dialogResult == JOptionPane.NO_OPTION) {
                    System.out.println("Window Closing..");
                    System.exit(0);
                }
            } else {
//                int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to Exit?", "", JOptionPane.YES_NO_OPTION);
//                if (dialogResult == JOptionPane.YES_OPTION) {
                System.exit(0);
//                }
            }
        } catch (Exception e) {
            Logs.write("Deleting Temporary Files");
            Logs.write(Logs.getExceptionTrace(e));
            JOptionPane.showMessageDialog(this, "Something Went Wrong!\nCouldn't delete Temp files!");
        }
    }

    private void menuItemDonateActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Web.openWebpage(new URI("https://www.paypal.me/NikhilMenghani"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Thank you for your interest but something went wrong.\nPlease check your internet connection!");
        }
    }

    private void menuItemSwitchViewActionPerformed(java.awt.event.ActionEvent evt) {
        toggleCardLayout();
    }

    private void menuItemQuickConnectActionPerformed(java.awt.event.ActionEvent evt) {
        Device.quickConnect();
    }

    private void menuItemPushFileToDeviceActionPerformed(java.awt.event.ActionEvent evt) {
        File[] files = MyFileFilter.getSelectedFiles("folder");
        new Thread(() -> {
            MyTree.setCardLayout(2);
            (new DeviceOperations()).pushToDevice(files);
            MyTree.setCardLayout(1);
        }).start();
    }

    private void menuItemWifiActionPerformed(java.awt.event.ActionEvent evt) {
        DeviceConnectUI dcui = new DeviceConnectUI();
        dcui.setVisible(true);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Functions ">
    public static void setCardLayout(int cardNo) {
        switch (cardNo) {
            case 1:
                layeredPaneHome.setVisible(true);
                layeredPaneProgress.setVisible(false);
                break;
            case 2:
                layeredPaneHome.setVisible(false);
                layeredPaneProgress.setVisible(true);
                break;
        }
        //Disabling traditional progress bar since we have circular progressbar
        //CardLayout cardLayout = (CardLayout) panelLower.getLayout();
        //cardLayout.show(panelLower, "card" + Integer.toString(cardNo));
    }

    public static void updateProgressBar(int fileIndex, String progress_title_text, String progress_content_text) {
        progressValue = (fileIndex * 100) / maxSize;
        String str = (new File(progress_content_text)).getName();
        if (str.length() > 60) {
            str = str.substring(0, str.length() / 3) + "..." + str.substring(str.length() - 10, str.length());
        } else if (str.length() > 40) {
            str = str.substring(0, str.length() / 2) + "..." + str.substring(str.length() - 10, str.length());
        }
        txtProgressTitle.setText(progress_title_text);
        txtProgressContent.setText(str);
        circularProgressBar.updateProgress(progressValue);
    }

    public static void toggleCardLayout() {
        if (layeredPaneHome.isVisible()) {
            layeredPaneHome.setVisible(false);
            layeredPaneProgress.setVisible(true);
        } else {
            layeredPaneHome.setVisible(true);
            layeredPaneProgress.setVisible(false);
        }
    }

    public boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyTreeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyTreeUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane SP_tree;
    private javax.swing.JButton btnCreateZip;
    private javax.swing.JButton btnImportZip;
    private javax.swing.JLayeredPane layeredPaneButtons;
    public static javax.swing.JLayeredPane layeredPaneHome;
    public static javax.swing.JLayeredPane layeredPaneProgress;
    private javax.swing.JLayeredPane layeredPaneProgressBar;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JMenu menuAbout;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuDevice;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemCheckForUpdates;
    private javax.swing.JMenuItem menuItemDevelopers;
    private javax.swing.JMenuItem menuItemDonate;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemInstructions;
    private javax.swing.JMenuItem menuItemPreference;
    private javax.swing.JMenuItem menuItemQuickConnect;
    private javax.swing.JMenuItem menuItemPushFileToDevice;
    private javax.swing.JMenuItem menuItemSwitchView;
    private javax.swing.JMenuItem menuItemUSB;
    private javax.swing.JMenuItem menuItemWifi;
    private javax.swing.JMenu menuView;
    private javax.swing.JPanel panelCreateZip;
    private javax.swing.JPanel panelImportZip;
    public static javax.swing.JPanel panelLower;
    private javax.swing.JPanel panelMain;
    public static flashablezipcreator.UserInterface.CircularProgressBar circularProgressBar;
    public static javax.swing.JPanel panelProgressBar;
    private javax.swing.JPanel panel_logo;
    public static javax.swing.JProgressBar progressBarImportExport;
    public static javax.swing.JTree tree;
    public static javax.swing.JTextField txtProgressContent;
    public static javax.swing.JTextField txtProgressTitle;
    // End of variables declaration                   
}
