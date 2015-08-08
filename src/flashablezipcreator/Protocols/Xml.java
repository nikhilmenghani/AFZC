/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Operations.XmlOperations;
import static flashablezipcreator.Protocols.Export.to;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikhil
 */
public class Xml {

    static XmlOperations xo;
    static String custom_path = "afzc/custom_data.xml";
    static String delete_path = "afzc/delete_data.xml";
    static String data_path = "afzc/file_data.xml";
    public static String file_details_path = "file_details.xml";
    static String originalData = "";
    static String generatedData = "";
    static String fileData = "";
    static String deleteData = "";
    public static String fileDetailsData = "";

    public static String getString(int type, ProjectItemNode rootNode) throws ParserConfigurationException, TransformerException {
        xo = new XmlOperations();
        xo.createXML();
        for (ProjectItemNode project : to.getProjectsSorted(rootNode)) {
            if (((ProjectNode) project).projectType != ProjectNode.PROJECT_THEMES) {
                for (ProjectItemNode groupNode : ((ProjectNode) project).children) {
                    for (ProjectItemNode node : ((GroupNode) groupNode).children) {
                        switch (type) {
                            case GroupNode.GROUP_CUSTOM:
                                if (node.type == ProjectItemNode.NODE_SUBGROUP) {
                                    for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                                        if (((SubGroupNode) fileNode.parent).subGroupType == SubGroupNode.TYPE_CUSTOM) {
                                            xo.addFileNode((FileNode) fileNode, xo.rootSubGroup);
                                        }
                                    }
                                } else if (node.type == ProjectItemNode.NODE_FILE) {
                                    if (((GroupNode) node.parent).groupType == GroupNode.GROUP_CUSTOM) {
                                        xo.addFileNode((FileNode) node, xo.rootGroup);
                                    }
                                }
                                break;
                            case GroupNode.GROUP_OTHER:
                                break;
                            case GroupNode.GROUP_DELETE_FILES:
                                if (node.type == ProjectItemNode.NODE_FILE && ((GroupNode) node.parent).groupType == GroupNode.GROUP_DELETE_FILES) {
                                    xo.addFileNode((FileNode) node, xo.rootGroup);
                                }
                                break;
                            default:
                                if (node.type == ProjectItemNode.NODE_SUBGROUP) {
                                    for (ProjectItemNode fileNode : ((SubGroupNode) node).children) {
                                        if (((SubGroupNode) fileNode.parent).subGroupType != SubGroupNode.TYPE_CUSTOM) {
                                            xo.addFileNode((FileNode) fileNode, xo.rootSubGroup);
                                        }
                                    }
                                } else if (node.type == ProjectItemNode.NODE_FILE) {
                                    if (((GroupNode) node.parent).groupType != GroupNode.GROUP_CUSTOM
                                            && ((GroupNode) node.parent).groupType != GroupNode.GROUP_DELETE_FILES
                                            && ((GroupNode) node.parent).groupType != GroupNode.GROUP_OTHER) {
                                        xo.addFileNode((FileNode) node, xo.rootGroup);
                                    }
                                }
                        }
                    }
                }
            }
        }
        return xo.getXML();
    }

    public static void parseXml(int type, ProjectItemNode rootNode, DefaultTreeModel model) throws ParserConfigurationException, SAXException, IOException {
        switch (type) {
            case GroupNode.GROUP_CUSTOM:
                xo.parseGeneratedXML(rootNode, generatedData, originalData);//generatedData and OriginalData are modified in Import.java.
                break;
            case GroupNode.GROUP_DELETE_FILES:
                xo.parseXML(deleteData, rootNode, model);
                break;
            default:
                xo.parseDataXML(rootNode, fileData);//this is for setting description of files.
        }

    }

    //following functions are used in generating generatedData in Import.java
    public static void initialize() throws ParserConfigurationException {
        xo = new XmlOperations();
        xo.createXML();
    }

    public static void addFileDataToGroup(FileNode file) {
        xo.addFileNode(file, xo.rootGroup);
    }

    public static void addFileDataToSubGroup(FileNode file) {
        xo.addFileNode(file, xo.rootSubGroup);
    }

    public static void terminate() throws TransformerException {
        generatedData = xo.getXML();
    }

    //following will initialize project variables if external xml file is present.
    public static void initializeProjectDetails(String data) throws ParserConfigurationException, SAXException, IOException {
        xo = new XmlOperations();
        xo.initializeProjectData(data);
    }
}
