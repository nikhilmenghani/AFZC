/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator;

import static flashablezipcreator.AFZC.Protocols.p;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.DiskOperations.ReadZip;
import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.DiskOperations.WriteZip;
import flashablezipcreator.Operations.JarOperations;
import flashablezipcreator.Operations.XmlOperations;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class FlashableZipCreator {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, ParserConfigurationException, SAXException {
//        QueryDB qdb = new QueryDB("afzc");
//        p("qdb initialized");
////        QueryDB qdb1 = new QueryDB("afzc");
////        p("started");
//        try{
//            qdb.createTable("create table package(group_name varchar(40), location varchar(60), file_name varchar(120))");
//        }catch (SQLException sqle){
//            p("Table Already Exists");
//        }
//        p("table created");
//        qdb.insert("package", "'System', 'system/app', 'abc.apk'");
//        qdb.insert("package", "'System', 'data/app', 'data.apk'");
//        ResultSet results = qdb.select("file_name", "package", "group_name IS NOT NULL");
//        while(results.next())
//        p(results.getString(1));
//        qdb.update("package", "location = '/system/app'", "group_name = 'System'");
//        results = qdb.select("*", "package", "group_name IS NOT NULL");
//        while(results.next()){
//            p("Column 1");
//            p(results.getString(1));
//            p("Column 2");
//            p(results.getString(2));
//            p("Column 3");
//            p(results.getString(3));
//        }
//        qdb.delete("package", "file_name = 'data.apk'");
//        results = qdb.select("file_name", "package", "group_name IS NOT NULL");
//        while(results.next())
//        p(results.getString(1));
//        p("record successfully updated");

//        Read r = new Read();
//        String str = r.getFileString("src" + File.separator + "flashablezipcreator" + File.separator + "Supported Devices");
//        System.out.println(str);
        //Write w = new Write();
        //w.writeStringToFile(r.getFileString("src\\flashablezipcreator\\Supported Devices"), "S D");
//        File fXmlFile = new File("test.xml");
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        
//	Document doc = dBuilder.parse(fXmlFile);
//        doc.getDocumentElement().normalize();
//        JOptionPane.showMessageDialog(null, doc.getDocumentElement().getNodeName());
//        ZipFile zf;
//        XmlOperations xo = new XmlOperations();
//        try {
//            zf = new ZipFile("test.zip");
//            for (Enumeration<? extends ZipEntry> e = zf.entries();
//                    e.hasMoreElements();) {
//                ZipEntry ze = e.nextElement();
//                String name = ze.getName();
//                if (name.endsWith(".xml")) {
//                    InputStream in = zf.getInputStream(ze);
//                    xo.readXMLFromZip(in);
//                }
//            }
//        } catch (IOException e1) {
//            System.out.println("Sorry we couldn't find the file");
//            e1.printStackTrace();
//        }
        //ReadZip rz = new ReadZip("test.zip");
        //XmlOperations xo = new XmlOperations();
//        while(rz.ze!=null){
//            p(rz.ze.getName());
//            
//            if(rz.ze.getName().endsWith(".xml")){
//                InputStream in = rz.zis;
//                xo.readXMLFromZip(in);
//            }
//            rz.ze = rz.zis.getNextEntry();
//        }
//        rz.close();
//        WriteZip wz = new WriteZip("test.zip");
//        wz.writeStringToZip("Nikhil \nMenghani", "customize");
//        wz.writeFileToZip("src\\flashablezipcreator\\Supported Devices", "src/flashablezipcreator/Supported Devices");
//        wz.close();
//        ReadZip rz = new ReadZip("test.zip");
//        while(rz.ze != null){
//            p(rz.ze.getName());
        //File file = new File(rz.ze.getName());
        //if(!file.isDirectory()){
        //p(rz.getStringFromFile(rz.zis));
        //}
//                File outputFile = new File("Temp" + File.separator + rz.ze.getName());
//                
//                rz.writeFileFromZip(rz.zis, outputFile);
//                rz.ze = rz.zis.getNextEntry();
//        }
//        rz.close();
//        Write w = new Write();
//        w.writeFile("src/flashablezipcreator/AFZC.png", "AFZC1.png");
//        JTree jt = new JTree();
//        jt.setVisible(true);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JTree().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(FlashableZipCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
