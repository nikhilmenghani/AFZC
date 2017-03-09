/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.ReadZip;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

/**
 *
 * @author Nikhil
 */
public final class Mod {

    public static final int MOD_LESS = 0;
    public static final int TITANIUM_BACKUP = 1;
    public static final int RADON_KERNEL_KENZO = 2;
    public static final int AGNI_KERNEL_KERNEL = 3;
    public static final int PIXEL_APPS = 4;
    public static final int DEFAULT = -1;

    public static int getModType(ReadZip rz) {
        ArrayList<String> fileList = new ArrayList<>();
        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
            ZipEntry ze = e.nextElement();
            String name = ze.getName();
            if (name.endsWith("/") || (new File(name)).isDirectory() || name.startsWith("META-INF")) {
                continue;
            }
            if (name.startsWith("customize") && name.contains("mod")) {
                String path = name.substring(name.indexOf("_") + 1, name.length());
                path = path.substring(0, path.indexOf("/"));
                if (!fileList.contains("mod__" + path)) {
                    fileList.add("mod__" + path);
                }
                fileList.add(name);
                continue;
            }
            fileList.add(name);
        }
        if (isTitanium(fileList)) {
            return TITANIUM_BACKUP;
        }
        return DEFAULT;
    }

    public static boolean isTitanium(ArrayList<String> fileList) {
        boolean flag = false;
        if (fileList.contains("com.keramidas.TitaniumBackup.apk") || fileList.contains("mod__" + TITANIUM_BACKUP)) {
            flag = true;
        }
        return flag;
    }
}
