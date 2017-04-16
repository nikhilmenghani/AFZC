/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator;

import flashablezipcreator.UserInterface.JTreeDemo;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.DiskOperations.ReadZip;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Jar;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Xml;
import flashablezipcreator.UserInterface.MyTree;
import flashablezipcreator.UserInterface.Preferences;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class FlashableZipCreator {

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     */
    
    public static String OS = "Windoes";
            
    public static void main(String args[]) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, ParserConfigurationException, SAXException, URISyntaxException, TransformerException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        OS = "Windows";
        try {
            OS = JarOperations.getSystemOS();
            if (OS.equals("Windows")) {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } else {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("GTK+".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }

                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "intoMultiCatch");
            java.util.logging.Logger.getLogger(JTreeDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        letsBegin();
    }
    
    public static void letsBegin(){
        try {
            File f = new File("Preferences.config");
            Read r = new Read();
            Logs.logFile = "Logs_" + Logs.getTime() + ".log";
            if (f.exists()) {
                Preferences.pp.preferencesFilePresent = true;
                Preferences.pp.preferencesConfig = r.getFileString("Preferences.config");
                String preferencesConfig = Preferences.pp.preferencesConfig;
                Preferences.pp.themes = Xml.getThemes(preferencesConfig);
                Preferences.pp.aromaVersion = Xml.getAromaVersion(preferencesConfig);
                Preferences.pp.IsFromLollipop = Xml.getAndroidVersionDetail(preferencesConfig);
                Preferences.pp.isQuickSetup = Xml.getQuickSetup(preferencesConfig);
                Preferences.pp.zipCreatorName = Xml.getZipCreatorName(preferencesConfig);
                Preferences.pp.zipVersion = Xml.getZipVersion(preferencesConfig);
                Preferences.pp.saveLogs = Xml.getLogsIndicator(preferencesConfig);
                Preferences.pp.checkUpdatesOnStartUp = Xml.getCheckUpdatesIndicator(preferencesConfig);
                Logs.write("Created Logs File..");
                Logs.write(OS + " Operating System Found..!!");
                Logs.write("Preferences.config Found");
                Logs.write("Preferences Loaded");
            }
            if (Preferences.pp.themes.isEmpty()) {
                Preferences.pp.themes.add("Nikhil");
                Preferences.pp.themes.add("Ics");
                Preferences.pp.themes.add("RedBlack");
            }

//            Control.check();
//            if(Control.forceCheckOnStartUp){
//                Update.runUpdateCheck();
//            }else if (Preferences.checkUpdatesOnStartUp) {
//                Update.runUpdateCheck();
//            }
            //Device Configuration
            if ((new File("update-binary").exists())) {
                Device.binary = (new Read()).getFileBytes("update-binary");
                Logs.write("update-binary found");
                Preferences.pp.useUniversalBinary = false;
            } else {
                Logs.write("update-binary not found, using Universal Binary");
                Preferences.pp.useUniversalBinary = true;
            }

            if (Jar.isExecutingThrough()) {
                Logs.write("setting jar file list");
                JarOperations.setJarFileList();
                Logs.write("set jar file list");
            } else {
                Xml.file_details_path = "dist/" + Xml.file_details_path;
            }
            f = new File(Xml.file_details_path);
            if (f.exists()) {
                Xml.fileDetailsData = r.getFileString(Xml.file_details_path);
                Xml.initializeProjectDetails(Xml.fileDetailsData);
            }
            new MyTree().setVisible(true);
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            JOptionPane.showMessageDialog(null, Logs.getExceptionTrace(ex));
            Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void extractZip() throws IOException {
        String dirPath = "C:\\Users\\Nikhil\\Documents\\NetBeansProjects\\FlashableZipCreator\\Fonts";
        File folder = new File(dirPath);
        for (File file : folder.listFiles()) {
            ReadZip rz = new ReadZip(file.getAbsolutePath());
            String fileName = file.getName().replace("Font_", "");
            if (fileName.indexOf(".") > 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
                ZipEntry ze = e.nextElement();
                String name = ze.getName();
                if (name.startsWith("META-INF")) {
                    continue;
                }
                File f = new File(name);

                String parent = f.getParent();
                String fName = f.getName();
                String writeToPath = "My Fonts" + File.separator + parent + File.separator + fileName + File.separator + fName;
                InputStream in = rz.zf.getInputStream(ze);
                rz.writeFileFromZip(in, writeToPath);
                System.out.println(writeToPath);
            }
            System.out.println();
        }
    }
}
