/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Nikhil
 */
public class MyFileFilter {

    public static JFileChooser updateChooser(JFileChooser fileChooser, String type) {
        FileFilter filter = null;
        if (!type.equals("")) {
            fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
            switch (type) {
                case "audio":
                    filter = new AudioFilter();
                    break;
                case "themes":
                    filter = new ThemesFilter();
                    break;
                case "folder":
                    fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setMultiSelectionEnabled(true);
                    filter = fileChooser.getFileFilter();
                    break;
                default:
                    filter = new FileNameExtensionFilter("." + type, type);
            }
            fileChooser.addChoosableFileFilter(filter);
        }
        return fileChooser;
    }

    public static File[] getSelectedFiles(String type) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser = updateChooser(fileChooser, type);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFiles();
        }
        return null;
    }

    public static String browseZipDestination() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
        FileFilter filter = new FileNameExtensionFilter(".zip", "zip");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Zip File Destination Location : " + file.getAbsolutePath());
            if (!file.getAbsolutePath().endsWith(".zip")) {
                //textField.setText(file.getAbsolutePath() + ".zip");
                return file.getAbsolutePath() + ".zip";
            } else {
                //textField.setText(file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
        return null;
    }
}

class Utils {

    public final static String acc = "aac";
    public final static String mp3 = "mp3";
    public final static String m4a = "m4a";
    public final static String ogg = "ogg";
    public final static String wav = "wav";
    public final static String prop = "prop";
    public final static String png = "png";


    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}

class AudioFilter extends FileFilter {

    //Accept all directories and all acc, mp3, m4a, ogg, or wav files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals(Utils.acc)
                    || extension.equals(Utils.mp3)
                    || extension.equals(Utils.m4a)
                    || extension.equals(Utils.ogg)
                    || extension.equals(Utils.wav);
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Audio Files";
    }

}

class ThemesFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals(Utils.prop)
                    || extension.equals(Utils.png);
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Themes Files";
    }

}
