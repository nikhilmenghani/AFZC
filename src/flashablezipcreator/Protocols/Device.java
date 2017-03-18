/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.Operations.JarOperations;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Device {
    public static byte[] binary = null;

    public static byte[] getBinary() throws IOException {
        if (Device.binary != null) {
            return Device.binary;
        }
        return null;
    }
}
