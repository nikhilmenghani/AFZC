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

    public static final int TITANIUM_BACKUP = 0;
    public static final int RADON_KERNEL_KENZO = 1;
    public static final int AGNI_KERNEL_KERNEL = 2;
    public static final int PIXEL_APPS = 3;
    public static final int DEFAULT = -1;

    public static int getModType(ReadZip rz) {
        ArrayList<String> fileList = new ArrayList<>();
        for (Enumeration<? extends ZipEntry> e = rz.zf.entries(); e.hasMoreElements();) {
            ZipEntry ze = e.nextElement();
            String name = ze.getName();
            if (name.endsWith("/") || (new File(name)).isDirectory() || name.startsWith("META-INF")) {
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
        if (fileList.contains("com.keramidas.TitaniumBackup.apk")) {
            flag = true;
        }
        return flag;
    }
}
