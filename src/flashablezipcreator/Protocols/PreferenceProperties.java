/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import java.util.ArrayList;

/**
 *
 * @author Nikhil
 */
public class PreferenceProperties {

    public String aromaVersion = "Version 3.00b1 - MELATI";
    public boolean androidVersionAboveLP;
    public boolean isQuickSetup = true;
    public boolean checkUpdatesOnStartUp = true;
    public String zipCreatorName = "Nikhil";
    public String zipVersion = "1.0";
    public boolean saveLogs = false;
    public String currentVersion = "v4.1";
    public String versionType = "stable";
    public boolean preferencesFilePresent = false;
    public String preferencesConfig;
    public ArrayList<String> themes = new ArrayList<>();
    public boolean useUniversalBinary = true;
    public boolean enableAddonDSupport = true;
    public boolean displayAddonDSupport = true;
    public String createZipType = "Aroma";
}
