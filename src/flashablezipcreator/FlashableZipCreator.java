/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator;

import flashablezipcreator.UserInterface.JTreeDemo;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Jar;
import flashablezipcreator.Protocols.Xml;
import flashablezipcreator.UserInterface.AddDevice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static void main(String args[]) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, ParserConfigurationException, SAXException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            if (JarOperations.getSystemOS().equals("Windows")) {
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

        try {
            if (Jar.isExecutingThrough()) {
                JarOperations.setJarFileList();
                Device.loadDeviceList();
                String configString = "";
                File f = new File(Xml.device_config_path);
                if (f.exists()) {
                    Read r = new Read();
                    configString = r.getFileString(Xml.device_config_path);
                    Device.selected = Xml.getDeviceName(configString);
                } else {
                    AddDevice ad = new AddDevice();
                }
            } else {
                Xml.file_details_path = "dist/" + Xml.file_details_path;
            }
            File f = new File(Xml.file_details_path);
            Read r = new Read();
            if (f.exists()) {
                Xml.fileDetailsData = r.getFileString(Xml.file_details_path);
                Xml.initializeProjectDetails(Xml.fileDetailsData);
            }
            //if(!Device.selected.equals("")){
            new MyTree().setVisible(true);
            //}
        } catch (IOException ex) {
            Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    if (Jar.isExecutingThrough()) {
//                        JarOperations.setJarFileList();
//                        Device.loadDeviceList();
//                        String configString = "";
//                        File f = new File(Xml.device_config_path);
//                        if(f.exists()){
//                            Read r = new Read();
//                            configString = r.getFileString(Xml.device_config_path);
//                            Device.selected = Xml.getDeviceName(configString);
//                        }else{
//                            AddDevice ad = new AddDevice();
//                        }
//                    } else {
//                        Xml.file_details_path = "dist/" + Xml.file_details_path;
//                    }
//                    //if(!Device.selected.equals("")){
//                        new MyTree().setVisible(true);
//                    //}
//                } catch (IOException ex) {
//                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ParserConfigurationException ex) {
//                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (TransformerException ex) {
//                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (SAXException ex) {
//                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
    }
}
