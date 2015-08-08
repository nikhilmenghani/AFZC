/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Operations;

import flashablezipcreator.Core.FileNode;
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

    public void createXML() throws ParserConfigurationException {
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("Root");
        document.appendChild(root);
        rootGroup = document.createElement("GroupData");
        addGroupNode(root, rootGroup);
        rootSubGroup = document.createElement("SubGroupData");
        addSubGroupNode(root, rootSubGroup);
    }

    public void addGroupNode(Element root, Element group) {
        root.appendChild(group);
    }

    public void addSubGroupNode(Element root, Element subGroup) {
        root.appendChild(subGroup);
    }

    public void addFileNode(FileNode fileNode, Element parent) {
        Element file = document.createElement("FileData");
        parent.appendChild(file);
        Attr attribute = document.createAttribute("name");
        attribute.setValue(fileNode.title);
        file.setAttributeNode(attribute);
        document.getDocumentElement().normalize();
        switch (fileNode.parent.type) {
            case ProjectItemNode.NODE_GROUP:
                addChildNode("ProjectName", fileNode.parent.parent.title, file);
                addChildNode("ProjectType", ((ProjectNode) fileNode.parent.parent).projectType + "", file);
                addChildNode("GroupName", fileNode.parent.title, file);
                addChildNode("GroupType", ((GroupNode) fileNode.parent).groupType + "", file);
                addChildNode("Description", fileNode.description, file);
                addChildNode("InstallLocation", fileNode.installLocation, file);
                addChildNode("Permissions", fileNode.filePermission, file);
                addChildNode("ZipPath", fileNode.fileZipPath, file);
                break;
            case ProjectItemNode.NODE_SUBGROUP:
                addChildNode("ProjectName", fileNode.parent.parent.parent.title, file);
                addChildNode("ProjectType", ((ProjectNode) fileNode.parent.parent.parent).projectType + "", file);
                addChildNode("GroupName", fileNode.parent.parent.title, file);
                addChildNode("GroupType", ((GroupNode) fileNode.parent.parent).groupType + "", file);
                addChildNode("SubGroupName", fileNode.parent.title, file);
                addChildNode("SubGroupType", ((SubGroupNode) fileNode.parent).subGroupType + "", file);
                addChildNode("Description", ((SubGroupNode) fileNode.parent).description, file);
                addChildNode("InstallLocation", fileNode.installLocation, file);
                addChildNode("Permissions", fileNode.filePermission, file);
                addChildNode("ZipPath", fileNode.fileZipPath, file);
                break;
        }
    }

    public void addChildNode(String childName, String childValue, Element parent) {
        Element child = document.createElement(childName);
        child.appendChild(document.createTextNode(childValue));
        parent.appendChild(child);
    }

    public String getXML() throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    //following function not used
//    public void writeXML(String path) throws TransformerConfigurationException, TransformerException {
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//        transformer.transform(new DOMSource(document), new StreamResult(new File(path)));
//        JOptionPane.showMessageDialog(null, "File saved to specified path");
//    }
//    public static FileNode addFileToTree(String fileName, String groupName, int groupType, String projectName, int projectType,
//            ProjectItemNode rootNode, DefaultTreeModel model, TreeOperations to) {
//        if (to.getProjectNode(projectName, projectType) == null) {
//            to.addChildTo(rootNode, projectName, projectType, model);
//        }
//        if (to.getGroupNode(groupName, groupType, projectName) == null) {
//            to.addChildTo(to.getProjectNode(projectName, projectType), groupName, groupType, model);
//            JOptionPane.showMessageDialog(null, groupName + " imported");
//        }
//        if (to.getFileNode(fileName, groupName, projectName) != null) {
//            if (groupType == GroupNode.GROUP_OTHER) {
//                fileName += "_1";
//                fileName = getUniqueName(fileName);
//            }
//        }
//        if (groupType == GroupNode.GROUP_CUSTOM) {
//            to.addChildTo(to.getGroupNode(groupName, groupType, projectName), fileName, "", "", model);
//        } else {
//            to.addChildTo(to.getGroupNode(groupName, groupType, projectName), fileName, ProjectItemNode.NODE_FILE, model);
//        }
//        return to.getFileNode(fileName, groupName, projectName);
//    }
    //following is to initialize details of project (like rom name, version, etc) from external xml file.
    public void initializeProjectData(String data) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
            Document genDoc = dBuilder.parse(new InputSource(new StringReader(data)));
            NodeList romList = genDoc.getElementsByTagName("Rom");
            for (int i = 0; i < romList.getLength(); i++) {
                Node romNode = romList.item(i);
                if (romNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) romNode;
                    Project.romName = element.getElementsByTagName("RName").item(0).getTextContent();
                    Project.romVersion = element.getElementsByTagName("RVersion").item(0).getTextContent();
                    Project.romAuthor = element.getElementsByTagName("RAuthor").item(0).getTextContent();
                    Project.romDevice = element.getElementsByTagName("RDevice").item(0).getTextContent();
                    Project.romDate = element.getElementsByTagName("RDate").item(0).getTextContent();
                }
            }

            NodeList gappsList = genDoc.getElementsByTagName("Gapps");
            for (int i = 0; i < gappsList.getLength(); i++) {
                Node gappsNode = gappsList.item(i);
                if (gappsNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) gappsNode;
                    Project.gappsName = element.getElementsByTagName("GName").item(0).getTextContent();
                    Project.gappsType = element.getElementsByTagName("GType").item(0).getTextContent();
                    Project.gappsDate = element.getElementsByTagName("GDate").item(0).getTextContent();
                    Project.androidVersion = element.getElementsByTagName("GAndroidVersion").item(0).getTextContent();
                }
            }

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
        TreeOperations to = new TreeOperations(rootNode);
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
                            Integer.parseInt(element.getElementsByTagName("ProjectType").item(0).getTextContent()), rootNode, model);
                }
            }
        }
    }

    //following is to set description of files.
    public void parseDataXML(ProjectItemNode rootNode, String data) throws SAXException, IOException, ParserConfigurationException {
        TreeOperations to = new TreeOperations(rootNode);
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
        TreeOperations to = new TreeOperations(rootNode);
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
