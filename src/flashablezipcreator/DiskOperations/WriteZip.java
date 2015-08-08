/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.DiskOperations;

import static flashablezipcreator.AFZC.Protocols.p;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public final class WriteZip extends Write {

    ZipOutputStream zos = null;

    public WriteZip() {

    }

    //constructor overloading
    public WriteZip(String filePath) throws FileNotFoundException, IOException {
        this.zos = createZOS(filePath);
        p("Zip created at " + filePath);
    }

    //this will create a zip outputstream
    public ZipOutputStream createZOS(String filePath) throws FileNotFoundException, IOException {
        return new ZipOutputStream(new FileOutputStream(createFile(filePath)));
    }

    //this function will create zip File.
    public File createFile(String filePath) throws IOException {
        File fileDest = new File(filePath);
        if (!fileDest.exists()) {
            fileDest.createNewFile();
            System.out.println("File Created..");
        } else {
            fileDest.delete();
            System.out.println("Existing File Deleted..!!");
            fileDest.createNewFile();
            System.out.println("File Created..");
        }
        return fileDest;
    }

    //this function will create a file in zip and write specified string to it.
    public void writeStringToZip(String strToWrite, String WriteAt) throws IOException {
        writeFileToZip(new ByteArrayInputStream(strToWrite.getBytes()), WriteAt);
    }

    //this will be used to write binaries to zip.
    public void writeByteToFile(byte buffer[], String filePath) throws IOException {
        writeFileToZip(new ByteArrayInputStream(buffer), filePath);
    }
    
    //this function is simple to use, source and destination path will do the work.
    public void writeFileToZip(String filePath, String writeAt) throws IOException {
        System.out.println("Looking for file path " + filePath);
        writeFileToZip(new FileInputStream(new File(filePath)), writeAt);
    }

    //this function is used to write file to zip for any zos.
    public void writeFileToZip(InputStream in, ZipOutputStream zos, String writeAt) throws IOException {
        p("File Writing at " + writeAt);
        //JOptionPane.showMessageDialog(null, "writing at " + writeAt);
        byte[] buffer = new byte[1024];
        ZipEntry ze = new ZipEntry(writeAt);
        try {
            zos.putNextEntry(ze);
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            p("File Written..");
        } catch (ZipException e) {
            System.out.println("Skipping Duplicate Entry : " + ze.getName());
        }
        in.close();
    }
    
    public void createFolderInZip(String path) throws IOException{
        ZipEntry ze = new ZipEntry(path);
        this.zos.putNextEntry(ze);
    }

    //this function is used to write file to zip for default zos.
    public void writeFileToZip(InputStream in, String writeAt) throws IOException {
        writeFileToZip(in, this.zos, writeAt);
    }

    //this function will close all the open streams
    public void close() throws IOException {
        this.zos.closeEntry();
        this.zos.close();
    }
}
