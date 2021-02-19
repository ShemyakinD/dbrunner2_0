package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Configure.RunnerConfigurationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("singleton")
@DependsOn({"runnerFolders","runnerConfigurationParams","crypta"})
public class RunnerXMLConf {
    private static final Logger logger = LoggerFactory.getLogger(RunnerXMLConf.class);

    @Autowired
    private Crypta crypta;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RunnerConfigurationParams runnerConfigurationParams;

    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    @PostConstruct
    public void initDBDataToXML() {
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document dom = documentBuilder.newDocument();
            Element root = dom.createElement("Databases");
            dom.appendChild(root);

            SaveDBXML(dom);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    private void SaveDBXML(Document dom) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty("method", "xml");
            tr.setOutputProperty("encoding", "UTF-8");
            if (!(new File(runnerConfigurationParams.getXmlConfigurePath())).exists()) {
                (new File(runnerConfigurationParams.getXmlConfigurePath())).getParentFile().mkdirs();
                (new File(runnerConfigurationParams.getXmlConfigurePath())).createNewFile();
            }
            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(runnerConfigurationParams.getXmlConfigurePath())));
        } catch (TransformerException | IOException te) {
            te.printStackTrace();
        }
    }

    public void createDB(Database database) {
        Element db;
        Element nested;
        try {
            Document dom = dbf.newDocumentBuilder().parse(new File(runnerConfigurationParams.getXmlConfigurePath()));

            db = dom.createElement("database");
            db.setAttribute("name", database.getName());
            db.setAttribute("isActive", database.getIsActive().toString());
            nested = dom.createElement("connection");
            nested.setTextContent(database.getConnection());
            db.appendChild(nested);
            nested = dom.createElement("username");
            nested.setTextContent(database.getUsername());
            db.appendChild(nested);
            nested = dom.createElement("password");
            nested.setTextContent(crypta.encrypt(database.getPassword()));
            db.appendChild(nested);
            dom.getElementsByTagName("Databases").item(0).appendChild(db);
            dom.getDocumentElement().normalize();
            SaveDBXML(dom);
            logger.info("Добавлена запись о БД " + database.toString());
        } catch (Exception e) {
            logger.error("Ошибка записи параметров БД");
            e.printStackTrace();
        }
    }

    public void removeDB(Database database) {
        try {
            Document document = dbf.newDocumentBuilder().parse(new File(runnerConfigurationParams.getXmlConfigurePath()));
            NodeList matchedElementList = document.getElementsByTagName("database");
            for (int temp = 0; temp < matchedElementList.getLength(); temp++) {
                Node nNode = matchedElementList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (database.getName().equals(eElement.getAttribute("name")))
                        nNode.getParentNode().removeChild(nNode);
                }
            }
            SaveDBXML(document);
            logger.info("Удалена запись о БД " + database.toString());
        } catch (IOException | ParserConfigurationException | SAXException ioException) {
            logger.error("Ошибка удаления записи БД в XML");
            ioException.printStackTrace();
        }
    }

    public List<Database> getDBList(){
        try {
            List<Database> dbList = new ArrayList();
            Document document = dbf.newDocumentBuilder().parse(new File(runnerConfigurationParams.getXmlConfigurePath()));
            NodeList matchedElementList = document.getElementsByTagName("database");

            for (int temp = 0; temp < matchedElementList.getLength(); temp++) {
                Node nNode = matchedElementList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String dbFolder = eElement.getAttribute("name");
                    Boolean dbActive = Boolean.parseBoolean(eElement.getAttribute("isActive"));
                    String dbConnection = eElement.getElementsByTagName("connection").item(0).getTextContent();
                    String dbUsername = eElement.getElementsByTagName("username").item(0).getTextContent();
                    String dbPassword = Crypta.decrypt(eElement.getElementsByTagName("password").item(0).getTextContent());

                    dbList.add((Database) applicationContext.getBean("OracleDatabaseByName", dbFolder, dbConnection, dbUsername, dbPassword, dbActive));
                }
            }
            return dbList;
        } catch (Exception e) {
            logger.error("Ошибка считывания списка БД " + e.getMessage());
            return null;
        }
    }

    public Database getDBInfoFromXMLByName(String databaseName){
        try {
            Document document = dbf.newDocumentBuilder().parse(new File(runnerConfigurationParams.getXmlConfigurePath()));
            NodeList matchedElementList = document.getElementsByTagName("database");
            for (int i = 0; i < matchedElementList.getLength(); i++) {
                Node xmlDB = matchedElementList.item(i);
                if (xmlDB.getNodeType() == Node.ELEMENT_NODE && xmlDB.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase(databaseName)) {
                        Element eElement = (Element) xmlDB;
                        String dbFolder = eElement.getAttribute("name");
                        Boolean dbActive = Boolean.parseBoolean(eElement.getAttribute("isActive"));
                        String dbConnection = eElement.getElementsByTagName("connection").item(0).getTextContent();
                        String dbUsername = eElement.getElementsByTagName("username").item(0).getTextContent();
                        String dbPassword = Crypta.decrypt(eElement.getElementsByTagName("password").item(0).getTextContent());
                        return (Database) applicationContext.getBean("OracleDatabaseByName", dbFolder, dbConnection, dbUsername, dbPassword, dbActive);
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("Ошибка считывания информации о БД " + e.getMessage());
            return null;
        }
    }

}
