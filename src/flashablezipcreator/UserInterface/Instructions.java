/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.UserInterface;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

/**
 *
 * @author Nikhil
 */
public class Instructions extends javax.swing.JFrame {

    /**
     * Creates new form Instructions
     */
    
    public JDialog dialog;

    public Instructions() {
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
        dialog = new JDialog(this, "Instructions", true);
        dialog.setResizable(false);
        
        panelMain = new javax.swing.JPanel();
        jspInstructions = new javax.swing.JScrollPane();
        jepInstructions = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jepInstructions.setEditable(false);
        jepInstructions.setContentType("text/html"); // NOI18N
        jepInstructions.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jepInstructions.setText("<html> <CENTER><B><h2>Instructions</h2></B></CENTER> <B><h3>Steps to Create a Flashable Zip</h3></B>  <br>0. Select your device if listed in Devices List, If not listed, place update-binary of your device in the directory of jar file. <br><br>1. Add a Project, give it a name.(A project can have multiple groups) <br>A popup appears on start up, you can change the settings in File->Preferences. <br><br>2. Add a Group. (A group will contain specific type of files in it) <br>For eg. you create a group (say xyz) with install location (system/app), then xyz group will include all the apk files that needs to be pushed to system/app <br><br>3. Add a Subgroup if required (When you need furthur grouping. It is required for bootanimations and fonts) <br>For eg. 2 Boot Animations will have their own bootanimation.zip. You cannot add file with same name twice. Hence you need subgroup to further distinguish the file. <br><br>4. Add a Folder. (From android 5.x+, we have apk files placed in folder which contains optional lib files as well with it. To build the same directory structure, you need to add folders) <br><br>5. Add Files to Groups/Subgroups/Folders. <br><br>6. Click on Export Zip to choose the destination and Click OK to generate zip.  <br><br><B><h3>How to add objects</h3></B>  <br>1. To add a Project <br><br>-> Right Click on AFZC Projects <br>-> Give a unique name to project <br>-> Click on Add.  <br><br>2. To add a Group <br><br>-> Right Click on the Project that you want your group to be added in <br>-> Hover on Add group and proceed ahead with selecting location to which you want to push your files to <br>-> Give a unique name <br>-> Click on Add  <br><br>3. To add a SubGroup <br><br>-> Right Click on Group <br>-> Click on Add Fonts/Boot Animations<br>-> Give a unique name <br>-> Click on Add. <U><br><br>Note: You will get an option to add subgroup only for Boot Animation and Fonts group as only they require it.</U>  <br><br>4. To add a Folder <br><br>-> Right Click on Group <br>-> Click on Add Folder <br>-> Give a unique name <br>-> Click on Add. <br><br>-> Right Click on Folder <br>-> Click on Add Folder to further add folder <br>-> Give an appropriate name <br>-> Click on Add.  <U><br><br>Tip: You can also drag and drop folders from your drive to the group. All the sub folders (if any) and files will be added automatically.</U>  <br><br>5. To add a File <br><br>-> Right Click on Group/Folder/SubGroup <br>-> Click on Add Files <br>-> Import the files.  <U><br><br>Tip: You can also drag and drop files from your drive to the Group/SubGroup/Folder. All the files will be imported.</U>  <br><br>6. To add a Theme <br><br>-> Right on Themes Project <br>-> Add Theme <br>-> Give appropriate name <br>-> Add required files to it  <br><br><B><h3>How to rename objects</h3></B>   <br>-> To rename a Project/Group/SubGroup/Folder/File, triple click on the object and change the name. Alternatively you can use F2 key to edit the object name.  <br><br><B><h3>Finding it tedious to create again and again?</h3></B>  <br>Don't worry, once you create a zip, you can import the created zip to make further changes to existing zip file. <br><br>To do so, use Import Zip button to add an already created zip OR drag the zip and add it to the tool. <br><br> <B><h3>Explore yourself to identify more hidden features.</h3></B><br> </html>");
        jepInstructions.setAutoscrolls(false);
        jepInstructions.setCaretPosition(0);
        jepInstructions.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jspInstructions.setViewportView(jepInstructions);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspInstructions, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspInstructions, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dialog.getContentPane().add(panelMain);
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
    }// </editor-fold>                        

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
            java.util.logging.Logger.getLogger(InstructionsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InstructionsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InstructionsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InstructionsUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InstructionsUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JEditorPane jepInstructions;
    private javax.swing.JScrollPane jspInstructions;
    private javax.swing.JPanel panelMain;
    // End of variables declaration                   
}
