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
import flashablezipcreator.Protocols.Project;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
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

    public String getDeviceName(String configData) throws ParserConfigurationException, SAXException, IOException {
        String deviceName = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(configData)));
            NodeList nameList = genDoc.getElementsByTagName("Device");
            for (int i = 0; i < nameList.getLength(); i++) {
                Node nameNode = nameList.item(i);
                if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nameNode;
                    deviceName = element.getElementsByTagName("Name").item(0).getTextContent();
                }
            }
        } catch (SAXParseException ex) {
            System.out.println("Device Details Empty");
        }
        return deviceName;
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
        attrPName.setValue(project.title);
        Attr attrPType = document.createAttribute("type");
        attrPType.setValue(Integer.toString(project.projectType));
        projectElem.setAttributeNode(attrPName);
        projectElem.setAttributeNode(attrPType);
        for (ProjectItemNode projectChild : project.children) {
            projectElem.appendChild(addGroupNode((GroupNode) projectChild));
        }
        root.appendChild(projectElem);
    }

    public Element addFolderNode(FolderNode folder) {
        Element folderElem = document.createElement("FolderData");
        Attr attrFolName = document.createAttribute("name");
        attrFolName.setValue(folder.title);
        folderElem.setAttributeNode(attrFolName);
        for (ProjectItemNode folderChild : folder.children) {
            switch (folderChild.type) {
                case ProjectItemNode.NODE_FOLDER:
                    folderElem.appendChild(addFolderNode((FolderNode) folderChild));
                    break;
                case ProjectItemNode.NODE_FILE:
                    folderElem.appendChild(addFileNode((FileNode) folderChild));
                    break;
            }
        }
        return folderElem;
    }

    public Element addFileNode(FileNode file) {
        Element fileElem = document.createElement("FileData");
        Attr attrFName = document.createAttribute("name");
        attrFName.setValue(file.title);
        fileElem.setAttributeNode(attrFName);
        Element description = document.createElement("description");
        description.appendChild(document.createTextNode(file.description));
        fileElem.appendChild(description);
        return fileElem;
    }

    public Element addSubGroupNode(SubGroupNode sgNode) {
        Element subGroupElem = document.createElement("SubGroupData");
        Attr attrSGName = document.createAttribute("name");
        attrSGName.setValue(sgNode.title);
        Attr attrSGType = document.createAttribute("type");
        attrSGType.setValue(Integer.toString(sgNode.subGroupType));
        subGroupElem.setAttributeNode(attrSGName);
        subGroupElem.setAttributeNode(attrSGType);
        for (ProjectItemNode subGroupChild : sgNode.children) {
            switch (subGroupChild.type) {
                case ProjectItemNode.NODE_FOLDER:
                    subGroupElem.appendChild(addFolderNode((FolderNode) subGroupChild));
                    break;
                case ProjectItemNode.NODE_FILE:
                    subGroupElem.appendChild(addFileNode((FileNode) subGroupChild));
                    break;
            }
        }
        return subGroupElem;
    }

    public Element addGroupNode(GroupNode gNode) {
        Element groupElem = document.createElement("GroupData");
        Attr attrGName = document.createAttribute("name");
        attrGName.setValue(gNode.title);
        Attr attrGType = document.createAttribute("type");
        attrGType.setValue(Integer.toString(gNode.groupType));
        groupElem.setAttributeNode(attrGName);
        groupElem.setAttributeNode(attrGType);
        for (ProjectItemNode groupChild : gNode.children) {
            switch (groupChild.type) {
                case ProjectItemNode.NODE_SUBGROUP:
                    groupElem.appendChild(addSubGroupNode((SubGroupNode) groupChild));
                    break;
                case ProjectItemNode.NODE_FOLDER:
                    groupElem.appendChild(addFolderNode((FolderNode) groupChild));
                    break;
                case ProjectItemNode.NODE_FILE:
                    groupElem.appendChild(addFileNode((FileNode) groupChild));
                    break;
            }
        }
        return groupElem;
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
//            NodeList romList = genDoc.getElementsByTagName("Rom");
//            for (int i = 0; i < romList.getLength(); i++) {
//                Node romNode = romList.item(i);
//                if (romNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) romNode;
//                    Project.romName = element.getElementsByTagName("RName").item(0).getTextContent();
//                    Project.romVersion = element.getElementsByTagName("RVersion").item(0).getTextContent();
//                    Project.romAuthor = element.getElementsByTagName("RAuthor").item(0).getTextContent();
//                    Project.romDevice = element.getElementsByTagName("RDevice").item(0).getTextContent();
//                    Project.romDate = element.getElementsByTagName("RDate").item(0).getTextContent();
//                }
//            }
//
//            NodeList gappsList = genDoc.getElementsByTagName("Gapps");
//            for (int i = 0; i < gappsList.getLength(); i++) {
//                Node gappsNode = gappsList.item(i);
//                if (gappsNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) gappsNode;
//                    Project.gappsName = element.getElementsByTagName("GName").item(0).getTextContent();
//                    Project.gappsType = element.getElementsByTagName("GType").item(0).getTextContent();
//                    Project.gappsDate = element.getElementsByTagName("GDate").item(0).getTextContent();
//                    Project.androidVersion = element.getElementsByTagName("GAndroidVersion").item(0).getTextContent();
//                }
//            }

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
    public void parseXML(String original, ProjectItemNode rootNode, DefaultTreeModel model) throws ParserConfigurationException, SAXException, IOException {
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
                    to.addFileToTree(element.getAttribute("name"),
                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
                            Integer.parseInt(element.getElementsByTagName("GroupType").item(0).getTextContent()),
                            element.getElementsByTagName("ProjectName").item(0).getTextContent(),
                            Integer.parseInt(element.getElementsByTagName("ProjectType").item(0).getTextContent()));
                }
            }
        }
    }

    //following is to set description of files.
    public void parseDataXML(ProjectItemNode rootNode, String data) throws SAXException, IOException, ParserConfigurationException {
        TreeOperations to = new TreeOperations();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document genDoc = dBuilder.parse(new InputSource(new StringReader(data)));
        NodeList fileList = genDoc.getElementsByTagName("FileData");
        for (int j = 0; j < fileList.getLength(); j++) {
            Node fileNode = fileList.item(j);
            if (fileNode.getParentNode().getNodeName().equals("GroupData")) {
                if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) fileNode;
                    FileNode file = to.getFileNode(element.getAttribute("name"),
                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
                            element.getElementsByTagName("ProjectName").item(0).getTextContent());
                    setFileValues(file, element);
                }
            } else if (fileNode.getParentNode().getNodeName().equals("SubGroupData")) {
                if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) fileNode;
                    FileNode file = to.getFileNode(element.getAttribute("name"),
                            element.getElementsByTagName("SubGroupName").item(0).getTextContent(),
                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
                            element.getElementsByTagName("ProjectName").item(0).getTextContent());
                    setFileValues(file, element);
                }
            }
        }
    }

    //following is to set values of custom group.
    public void parseGeneratedXML(ProjectItemNode rootNode, String generated, String original) throws ParserConfigurationException, SAXException, IOException {
        TreeOperations to = new TreeOperations();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document genDoc = dBuilder.parse(new InputSource(new StringReader(generated)));
        NodeList fileList = genDoc.getElementsByTagName("FileData");
        for (int j = 0; j < fileList.getLength(); j++) {
            Node fileNode = fileList.item(j);
            if (fileNode.getParentNode().getNodeName().equals("GroupData")) {
                if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) fileNode;
                    FileNode file = to.getFileNode(element.getAttribute("name"),
                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
                            element.getElementsByTagName("ProjectName").item(0).getTextContent());
                    parseOriginalXML(file, original);
                }
            } else if (fileNode.getParentNode().getNodeName().equals("SubGroupData")) {
                if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) fileNode;
                    FileNode file = to.getFileNode(element.getAttribute("name"),
                            element.getElementsByTagName("SubGroupName").item(0).getTextContent(),
                            element.getElementsByTagName("GroupName").item(0).getTextContent(),
                            element.getElementsByTagName("ProjectName").item(0).getTextContent());
                    parseOriginalXML(file, original);
                }
            }
        }
    }

    public void parseOriginalXML(FileNode file, String original) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(original)));
        NodeList fileList = doc.getElementsByTagName("FileData");
        for (int j = 0; j < fileList.getLength(); j++) {
            Node fileNode = fileList.item(j);
            if (file.title.equals(((Element) fileNode).getAttribute("name"))) {
                setFileValues(file, (Element) fileNode);
            }
        }
    }

    public void setFileValues(FileNode file, Element fileNode) {
        switch (file.parent.type) {
            case ProjectItemNode.NODE_GROUP:
                if (fileNode.getParentNode().getNodeName().equals("GroupData")
                        && fileNode.getElementsByTagName("GroupName").item(0).getTextContent().equals(file.parent.title)
                        && fileNode.getElementsByTagName("GroupType").item(0).getTextContent().equals(((GroupNode) file.parent).groupType + "")
                        && fileNode.getElementsByTagName("ProjectName").item(0).getTextContent().equals(file.parent.parent.title)
                        && fileNode.getElementsByTagName("ProjectType").item(0).getTextContent().equals(((ProjectNode) file.parent.parent).projectType + "")) {
                    file.installLocation = fileNode.getElementsByTagName("InstallLocation").item(0).getTextContent();
                    file.filePermission = fileNode.getElementsByTagName("Permissions").item(0).getTextContent();
                    file.description = fileNode.getElementsByTagName("Description").item(0).getTextContent();
                }
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                if (fileNode.getParentNode().getNodeName().equals("SubGroupData")
                        && fileNode.getElementsByTagName("SubGroupName").item(0).getTextContent().equals(file.parent.title)
                        && fileNode.getElementsByTagName("SubGroupType").item(0).getTextContent().equals(((SubGroupNode) file.parent).subGroupType + "")
                        && fileNode.getElementsByTagName("GroupName").item(0).getTextContent().equals(file.parent.parent.title)
                        && fileNode.getElementsByTagName("GroupType").item(0).getTextContent().equals(((GroupNode) file.parent.parent).groupType + "")
                        && fileNode.getElementsByTagName("ProjectName").item(0).getTextContent().equals(file.parent.parent.parent.title)
                        && fileNode.getElementsByTagName("ProjectType").item(0).getTextContent().equals(((ProjectNode) file.parent.parent.parent).projectType + "")) {
                    file.installLocation = fileNode.getElementsByTagName("InstallLocation").item(0).getTextContent();
                    file.filePermission = fileNode.getElementsByTagName("Permissions").item(0).getTextContent();
                    file.description = fileNode.getElementsByTagName("Description").item(0).getTextContent();
                    ((SubGroupNode) file.parent).description = fileNode.getElementsByTagName("Description").item(0).getTextContent();
                }
                break;
        }
    }
}
