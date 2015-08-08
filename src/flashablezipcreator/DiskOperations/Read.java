/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.DiskOperations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Read {
    BufferedReader br;
    public String getFileString(String path) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            try {
                String str = "";
                while ((br.ready())) {
                    str += (char) br.read();
                }
                return str;
            } catch (IOException ioe) {
                System.out.println("Exception caught while reading from File.." + ioe);
            }
        }else{
            JOptionPane.showMessageDialog(null, "External xml File Not Found");
        }
        return "";
    }
    
    public byte[] getFileBytes(String path) throws IOException {
        FileInputStream fis = new FileInputStream(new File(path));
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = fis.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }
    
    public byte[] getBytesFromFile(InputStream is) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }
    
    public String getStringFromFile(InputStream is) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        return baos.toString();
    }
    
}