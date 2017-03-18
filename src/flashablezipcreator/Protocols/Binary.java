/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.ProjectItemNode;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class Binary {

    public static String updateBinaryInstallerPath = "META-INF/com/google/android/update-binary-installer";
    public static String updateBinaryPath = "META-INF/com/google/android/update-binary";

    public static byte[] getUpdateBinary(ProjectItemNode rootNode) throws IOException {
        return Jar.getAromaBinary();
    }
    
    public static byte[] getInstallerBinary(ProjectItemNode rootNode) throws IOException {
        return Device.getBinary();
    }
}
