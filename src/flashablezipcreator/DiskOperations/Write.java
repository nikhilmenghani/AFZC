/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.DiskOperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikhil
 */
public class Write {

    static String path = "";

    public void appendStringToFile(String strToWrite, String filePath) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, true); //the true will append the new data
            fw.write(strToWrite);//appends the string to the file
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //this function is used when a file is required to be created with string data in it.
    public void writeStringToFile(String strToWrite, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(strToWrite);

        } catch (IOException e) {
            System.out.println("Exception Caught..!!");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void writeByteToFile(byte buffer[], String filePath) throws IOException {
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        if (filePath.contains(File.separator)) {
            String str = filePath.substring(0, filePath.lastIndexOf(File.separator));
            System.out.println(str);
            (new File(filePath.substring(0, filePath.lastIndexOf(File.separator)))).mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(buffer);
        }
    }

    public void writeFile(String sourcePath, String destPath) throws FileNotFoundException, IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (sourceFile.exists()) {
            FileInputStream fis = new FileInputStream(sourceFile);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            int len;
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(destPath);
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fis.close();
            fos.close();
        } else {
            System.out.println("Source File Not Found at : " + sourcePath);
        }
    }

    public void createFolder(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder created at : " + name);
        }
    }
    
    public void createFolders(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
            System.out.println("Folder created at : " + name);
        }
    }

    public void deleteDirectories(String location) {
        File directory = new File(location);
        if (!directory.exists()) {
            System.out.println("Directory Doesn't Exist..!!");
        } else {
            this.delete(directory);
        }
    }

    public void delete(File file) {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory Deleted.. " + file.getAbsolutePath());
            } else {
                String files[] = file.list();

                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    delete(fileDelete);
                }

                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }
        } else {
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public void rename(String oldName, String newName) throws IOException {
        File f = new File(oldName);
        Path p = FileSystems.getDefault().getPath(f.getAbsolutePath());
        Files.move(p, p.resolveSibling(newName), REPLACE_EXISTING);
    }
}
