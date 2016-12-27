/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.Operations.JarOperations;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Binary {

    public static String updateBinaryInstallerPath = "META-INF/com/google/android/update-binary-installer";
    public static String updateBinaryPath = "META-INF/com/google/android/update-binary";

    public static byte[] getUpdateBinary(ProjectItemNode rootNode) throws IOException {
//        switch (Project.returnMainProject(rootNode)) {
//            case ProjectNode.PROJECT_ROM:
//            case ProjectNode.PROJECT_GAPPS:
//            case ProjectNode.PROJECT_AROMA:
        if (!Device.isNeonCompatible()) {
            return JarOperations.non_neon_binary;
        }
        return JarOperations.neon_binary;
//            case ProjectNode.PROJECT_NORMAL:
//                return Device.getBinary();
//        }
//        return null;
    }

    public static byte[] getInstallerBinary(ProjectItemNode rootNode) throws IOException {
        return Device.getBinary();
    }
}
