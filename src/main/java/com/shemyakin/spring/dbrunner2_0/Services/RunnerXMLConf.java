package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.RunnerConfigurationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

@Service
@Scope("singleton")
@DependsOn({"runnerFolders","runnerConfigurationParams","crypta"})
public class RunnerXMLConf {
    private static final Logger logger = LoggerFactory.getLogger(RunnerXMLConf.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Crypta crypta;

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

    @EventListener(ApplicationReadyEvent.class)
    public void  writeDBDataToXML() {
        initDBDataToXML();

        createDB((Database) applicationContext.getBean("OracleDatabase", new File (runnerConfigurationParams.getSetupPath() + "DB1"), "localhost:1521/XEPDB1", "DIMON", "Q1w2e3r4t5y6", true));
        createDB((Database) applicationContext.getBean("OracleDatabase", new File (runnerConfigurationParams.getSetupPath() + "DB2"), "localhost:1521/XEPDB2", "TECHUSER", "Q1w2e3r4t5y6", true));
        createDB((Database) applicationContext.getBean("OracleDatabase", new File (runnerConfigurationParams.getSetupPath() + "DB3"), "localhost:1521/XEPDB3", "VIEWERUSER", "Q1w2e3r4t5y6", true));
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

}
