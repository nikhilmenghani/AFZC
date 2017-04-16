/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
import flashablezipcreator.Core.FolderNode;
import flashablezipcreator.Core.GroupNode;
import flashablezipcreator.Core.ProjectItemNode;
import flashablezipcreator.Core.ProjectNode;
import flashablezipcreator.Core.SubGroupNode;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.PreferenceProperties;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Types;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Nikhil
 */
public class XmlOperations {

    DocumentBuilderFactory documentFactory;
    DocumentBuilder documentBuilder;
    static Document document = null;
    public Element root;
    public Element rootGroup;
    public Element rootSubGroup;
    public Element rootFolder;
    TreeOperations to = new TreeOperations();

    public void createConfigurationConfig(PreferenceProperties pp) throws ParserConfigurationException {
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("Configuration");
        document.appendChild(root);
        Element aromaVersionElem = document.createElement("AromaVersion");
        aromaVersionElem.setTextContent(pp.aromaVersion);
        Element androidVersionElem = document.createElement("AboveLollipop");
        androidVersionElem.setTextContent(String.valueOf(pp.androidVersionAboveLP));
        Element quickProjectSetupElem = document.createElement("IsQuickSetup");
        quickProjectSetupElem.setTextContent(String.valueOf(pp.isQuickSetup));
        Element checkUpdatesElem = document.createElement("CheckUpdates");
        checkUpdatesElem.setTextContent(String.valueOf(pp.checkUpdatesOnStartUp));
        Element zipCreatorNameElem = document.createElement("zipCreatorName");
        zipCreatorNameElem.setTextContent(pp.zipCreatorName);
        Element zipVersionElem = document.createElement("zipVersion");
        zipVersionElem.setTextContent(pp.zipVersion);
        Element saveLogsElem = document.createElement("saveLogs");
        saveLogsElem.setTextContent(String.valueOf(pp.saveLogs));
        root.appendChild(aromaVersionElem);
        root.appendChild(androidVersionElem);
        root.appendChild(quickProjectSetupElem);
        root.appendChild(checkUpdatesElem);
        root.appendChild(zipCreatorNameElem);
        root.appendChild(zipVersionElem);
        root.appendChild(saveLogsElem);
    }

    public String getStringConfigValue(String configData, String elementName) throws ParserConfigurationException, SAXException, IOException {
        String configValue = null;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(configData)));
            NodeList nameList = genDoc.getElementsByTagName(elementName);
            for (int i = 0; i < nameList.getLength(); i++) {
                Node nameNode = nameList.item(i);
                if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nameNode;
                    configValue = element.getTextContent();
                }
            }
        } catch (SAXParseException ex) {
            System.out.println(elementName + " Details Empty");
        }
        return configValue;
    }

    public boolean getBoolConfigValue(String configData, String elementName) throws ParserConfigurationException, SAXException, IOException {
        boolean configValue = false;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(configData)));
            NodeList nameList = genDoc.getElementsByTagName(elementName);
            for (int i = 0; i < nameList.getLength(); i++) {
                Node nameNode = nameList.item(i);
                if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nameNode;
                    configValue = element.getTextContent().equals("true");
                }
            }
        } catch (SAXParseException ex) {
            System.out.println(elementName + " Details Empty");
        }
        return configValue;
    }

    public ArrayList<String> getThemes(String configData) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> themes = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(configData)));
            NodeList nameList = genDoc.getElementsByTagName("Theme");
            for (int i = 0; i < nameList.getLength(); i++) {
                Node nameNode = nameList.item(i);
                if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nameNode;
                    themes.add(element.getTextContent());
                }
            }
        } catch (SAXParseException ex) {
            System.out.println("Theme Details Empty");
        }
        return themes;
    }

    public void createDeviceConfig(String deviceName) throws ParserConfigurationException {
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("Device");
        document.appendChild(root);
        Element name = document.createElement("Name");
        name.setTextContent(deviceName);
        root.appendChild(name);
    }

    public void createXML() throws ParserConfigurationException {
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("Root");
        document.appendChild(root);
    }

    public void addProjectNode(ProjectNode project) {
        Element projectElem = document.createElement("ProjectData");
        Attr attrPName = document.createAttribute("name");
        attrPName.setValue(project.prop.title);
        Attr attrPType = document.createAttribute("type");
        attrPType.setValue(Integer.toString(project.prop.projectType));
        projectElem.setAttributeNode(attrPName);
        projectElem.setAttributeNode(attrPType);
        for (ProjectItemNode projectChild : project.prop.children) {
            projectElem.appendChild(addGroupNode((GroupNode) projectChild));
        }
        root.appendChild(projectElem);
    }

    public Element addFolderNode(FolderNode folder) {
        Element folderElem = document.createElement("FolderData");
        Attr attrFolName = document.createAttribute("name");
        attrFolName.setValue(folder.prop.title);
        folderElem.setAttributeNode(attrFolName);
        for (ProjectItemNode folderChild : folder.prop.children) {
            switch (folderChild.prop.type) {
                case Types.NODE_FOLDER:
                    folderElem.appendChild(addFolderNode((FolderNode) folderChild));
                    break;
                case Types.NODE_FILE:
                    folderElem.appendChild(addFileNode((FileNode) folderChild));
                    break;
            }
        }
        return folderElem;
    }

    public Element addFileNode(FileNode file) {
        Element fileElem = document.createElement("FileData");
        Attr attrFName = document.createAttribute("name");
        attrFName.setValue(file.prop.title);
        fileElem.setAttributeNode(attrFName);
        Element description = document.createElement("description");
        description.appendChild(document.createTextNode(file.prop.description));
        fileElem.appendChild(description);
        return fileElem;
    }

    public Element addSubGroupNode(SubGroupNode sgNode) {
        Element subGroupElem = document.createElement("SubGroupData");
        Attr attrSGName = document.createAttribute("name");
        attrSGName.setValue(sgNode.prop.title);
        Attr attrSGType = document.createAttribute("type");
        attrSGType.setValue(Integer.toString(sgNode.prop.subGroupType));
        subGroupElem.setAttributeNode(attrSGName);
        subGroupElem.setAttributeNode(attrSGType);
        for (ProjectItemNode subGroupChild : sgNode.prop.children) {
            switch (subGroupChild.prop.type) {
                case Types.NODE_FILE:
                    subGroupElem.appendChild(addFileNode((FileNode) subGroupChild));
                    break;
            }
        }
        return subGroupElem;
    }

    public Element addGroupNode(GroupNode gNode) {
        Element groupElem = document.createElement("GroupData");
        Attr attrGName = document.createAttribute("name");
        attrGName.setValue(gNode.prop.title);
        Attr attrGType = document.createAttribute("type");
        attrGType.setValue(Integer.toString(gNode.prop.groupType));
        groupElem.setAttributeNode(attrGName);
        groupElem.setAttributeNode(attrGType);
        for (ProjectItemNode groupChild : gNode.prop.children) {
            switch (groupChild.prop.type) {
                case Types.NODE_SUBGROUP:
                    groupElem.appendChild(addSubGroupNode((SubGroupNode) groupChild));
                    break;
                case Types.NODE_FOLDER:
                    groupElem.appendChild(addFolderNode((FolderNode) groupChild));
                    break;
                case Types.NODE_FILE:
                    groupElem.appendChild(addFileNode((FileNode) groupChild));
                    break;
            }
        }
        return groupElem;
    }

    public String getCleanXML() throws TransformerException {
        removeNodes((Node) document);
        return getXML();
    }

    public void removeNodes(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            removeNodes(list.item(i));
        }
        boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE
                && node.getChildNodes().getLength() == 0 && "FolderData".equals(node.getNodeName());
        if (emptyElement) {
            node.getParentNode().removeChild(node);
        }
    }

    //this will return string form of xml document which we can use to write it to a file
    public String getXML() throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    //following is to initialize details of project (like rom name, version, etc) from external xml file.
    public void initializeProjectData(String data) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(data)));
            NodeList modList = genDoc.getElementsByTagName("Mod");
            for (int i = 0; i < modList.getLength(); i++) {
                Node modNode = modList.item(i);
                if (modNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) modNode;
                    Project.releaseVersion = element.getElementsByTagName("MReleaseVersion").item(0).getTextContent();
                }
            }

            NodeList creatorList = genDoc.getElementsByTagName("Creator");
            for (int i = 0; i < creatorList.getLength(); i++) {
                Node creatorNode = creatorList.item(i);
                if (creatorNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) creatorNode;
                    Project.zipCreator = element.getElementsByTagName("CName").item(0).getTextContent();
                }
            }
        } catch (SAXParseException ex) {
            System.out.println("File Details Empty");
        }
    }

    //following will create file objects of delete file group.
    public void parseXML(String original) throws ParserConfigurationException, SAXException, IOException {
        TreeOperations to = new TreeOperations();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document genDoc = dBuilder.parse(new InputSource(new StringReader(original)));
        NodeList fileList = genDoc.getElementsByTagName("FileData");
        for (int j = 0; j < fileList.getLength(); j++) {
            Node fileNode = fileList.item(j);
            if (fileNode.getParentNode().getNodeName().equals("GroupData")) {
                if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) fileNode;
//                    to.addFileToTree(element.getAttribute("name"),
//                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
//                            Integer.parseInt(element.getElementsByTagName("GroupType").item(0).getTextContent()),
//                            element.getElementsByTagName("ProjectName").item(0).getTextContent(),
//                            Integer.parseInt(element.getElementsByTagName("ProjectType").item(0).getTextContent()));
                }
            }
        }
    }

    public void parseDataXML(String data) throws ParserConfigurationException, SAXException, IOException {
        TreeOperations to = new TreeOperations();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document genDoc = dBuilder.parse(new InputSource(new StringReader(data)));
        NodeList projectList = genDoc.getElementsByTagName("ProjectData");
        for (int p = 0; p < projectList.getLength(); p++) {
            Node projectNode = projectList.item(p);
            String projectName = ((Element) projectNode).getAttribute("name");
            NodeList groupList = projectNode.getChildNodes();
            for (int g = 0; g < groupList.getLength(); g++) {
                Node groupNode = groupList.item(g);
                if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                    String groupName = ((Element) groupNode).getAttribute("name");
                    NodeList groupChildList = groupNode.getChildNodes();
                    for (int gc = 0; gc < groupChildList.getLength(); gc++) {
                        Node groupChildNode = groupChildList.item(gc);
                        String subGroupName = "";
                        String folderName = "";
                        ArrayList<String> folders = new ArrayList<>();
                        if (groupChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            String groupChildName = ((Element) groupChildNode).getAttribute("name");
                            switch (groupChildNode.getNodeName()) {
                                case "SubGroupData":
                                    subGroupName = groupChildName;
                                    NodeList subGroupChildNodeList = groupChildNode.getChildNodes();
                                    for (int sgc = 0; sgc < subGroupChildNodeList.getLength(); sgc++) {
                                        Node subGroupChildNode = subGroupChildNodeList.item(sgc);
                                        if (subGroupChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                            String subGroupChildName = ((Element) subGroupChildNode).getAttribute("name");
                                            switch (subGroupChildNode.getNodeName()) {
                                                case "FolderData":
                                                    folders = new ArrayList<>();
                                                    folderName = subGroupChildName;
                                                    folders.add(folderName);
                                                    HandleFolderData(projectName, groupName, subGroupName, subGroupChildNode, folders);
                                                    break;
                                                case "FileData":
                                                    Logs.write("Working for file(inside subgroup " + subGroupName + " ): " + subGroupChildName);
                                                    FileNode file = to.getFileNode(subGroupChildName, folders, subGroupName, groupName, projectName);
                                                    file.prop.description = ((Element) subGroupChildNode).getElementsByTagName("description").item(0).getTextContent();
                                                    break;
                                            }
                                        }
                                    }
                                    break;
                                case "FolderData":
                                    folders = new ArrayList<>();
                                    folderName = groupChildName;
                                    folders.add(folderName);
                                    HandleFolderData(projectName, groupName, subGroupName, groupChildNode, folders);
                                    break;
                                case "FileData":
                                    Logs.write("Working for file(inside group " + groupName + " ): " + groupChildName);
                                    FileNode file = to.getFileNode(groupChildName, folders, subGroupName, groupName, projectName);
                                    file.prop.description = ((Element) groupChildNode).getElementsByTagName("description").item(0).getTextContent();
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void HandleFolderData(String ProjectName, String GroupName, String SubGroupName, Node folder, ArrayList<String> folders) {
        if (folder.hasChildNodes()) {
            NodeList folderChildList = folder.getChildNodes();
            for (int fc = 0; fc < folderChildList.getLength(); fc++) {
                Node folderChildNode = folderChildList.item(fc);
                if (folderChildNode.getNodeType() == Node.ELEMENT_NODE) {
                    String folderChildName = ((Element) folderChildNode).getAttribute("name");
                    switch (folderChildNode.getNodeName()) {
                        case "FolderData":
                            if (folderChildNode.hasChildNodes()) {
                                folders.add(folderChildName);
                            }
                            HandleFolderData(ProjectName, GroupName, SubGroupName, folderChildNode, folders);
                            folders.remove(folderChildName);
                            break;
                        case "FileData":
                            Logs.write("Working for file(inside folder " + folders.get(folders.size() - 1) + " ): " + folderChildName);
                            FileNode file = to.getFileNode(folderChildName, folders, SubGroupName, GroupName, ProjectName);
                            file.prop.description = ((Element) folderChildNode).getElementsByTagName("description").item(0).getTextContent();
                            break;
                    }
                }
            }
        }
    }
}
