/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.Read;
import flashablezipcreator.FlashableZipCreator;
import java.io.IOException;

/**
 *
 * @author Nikhil
 */
public class Script {
    
    public static final String afzcScriptFilePath = "/flashablezipcreator/META-INF/com/google/android/afzc";
    public static final String addonScriptFilePath = "/flashablezipcreator/META-INF/com/google/android/addon";
    public static final String afzcScriptZipPath = "afzc/afzc";
    public static final String addonScriptZipPath = "afzc/addon";
    public static final String afzcScriptTempPath = "/tmp/AFZCScripts/afzc";
    public static final String addonScriptTempPath = "/tmp/AFZCScripts/addon";
    public static final String logDataPath = "/tmp/zipContent/addondbackupdata";
    
    public static String getAfzcString() throws IOException{
        Read reader = new Read();
        String afzcScript = reader.getStringFromFile(FlashableZipCreator.class.getResourceAsStream(afzcScriptFilePath));
        return afzcScript;
    }
    
    public static String getAddonString() throws IOException{
        Read reader = new Read();
        String addonScript = reader.getStringFromFile(FlashableZipCreator.class.getResourceAsStream(addonScriptFilePath));
        return addonScript;
    }
}
