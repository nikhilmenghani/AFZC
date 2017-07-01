/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.UserInterface.MyTree;
import flashablezipcreator.Operations.XmlOperations;
import static flashablezipcreator.Protocols.Export.to;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class Xml {

    static XmlOperations xo;
    public static String device_config_path = "device.config";
    static String custom_path = "afzc/custom_data.xml";
    static String delete_path = "afzc/delete_data.xml";
    static String data_path = "afzc/file_data.xml";
    public static String file_details_path = "file_details.xml";
    static String originalData = "";
    static String generatedData = "";
    static String fileData = "";
    static String deleteData = "";
    public static String fileDetailsData = "";

    public static String generateFileDataXml() throws ParserConfigurationException, TransformerException {
        xo = new XmlOperations();
        xo.createXML();
        for (ProjectItemNode project : to.getProjectsSorted(MyTree.rootNode)) {
            if (((ProjectNode) project).prop.projectType != Types.PROJECT_THEMES) {
                xo.addProjectNode((ProjectNode) project);
            }
        }
        return xo.getCleanXML();
    }

    public static void parseXml(int type) throws ParserConfigurationException, SAXException, IOException {
        ProjectItemNode rootNode = MyTree.rootNode;
        switch (type) {
            case Types.GROUP_DELETE_FILES:
                xo.parseXML(deleteData);
                break;
            default:
                xo.parseDataXML(fileData);//this is for setting description of files.
        }

    }

    //following functions are used in generating generatedData in Import.java
    public static void initialize() throws ParserConfigurationException {
        xo = new XmlOperations();
        xo.createXML();
    }

    public static void terminate() throws TransformerException {
        generatedData = xo.getXML();
    }

    //following will initialize project variables if external xml file is present.
    public static void initializeProjectDetails(String data) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        xo.initializeProjectData(data);
    }

    public static String getPreferenceConfigString(PreferenceProperties pp)
            throws ParserConfigurationException, TransformerException {
        xo = new XmlOperations();
        xo.createConfigurationConfig(pp);
        return xo.getXML();
    }

    public static String getAromaVersion(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getStringConfigValue(configData, "AromaVersion");
    }

    public static boolean getAndroidVersionDetail(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "AboveLollipop");
    }

    public static boolean getQuickSetup(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "IsQuickSetup");
    }
    
    public static boolean getDisplayAddonDSupport(String configData) throws ParserConfigurationException, SAXException, IOException{
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "displayAddonDSupport");
    }
    
    public static boolean getEnableAddonDSupport(String configData) throws ParserConfigurationException, SAXException, IOException{
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "enableAddonDSupport");
    }

    public static ArrayList<String> getThemes(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getThemes(configData);
    }

    public static String getZipCreatorName(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getStringConfigValue(configData, "zipCreatorName");
    }

    public static String getZipVersion(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getStringConfigValue(configData, "zipVersion");
    }

    public static boolean getLogsIndicator(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "saveLogs");
    }

    public static boolean getCheckUpdatesIndicator(String configData) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        return xo.getBoolConfigValue(configData, "CheckUpdates");
    }
    
    public static String getCreateZipType(String configData) throws ParserConfigurationException, SAXException, IOException{
        xo = new XmlOperations();
        return xo.getStringConfigValue(configData, "createZipType");
    }
}
