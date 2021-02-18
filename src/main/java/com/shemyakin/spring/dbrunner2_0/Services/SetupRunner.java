package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.RunnerConfigurationParams;
import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Entities.SetupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.logging.Level;

@Service
@Scope("singleton")
@DependsOn({"runnerFolders","runnerConfigurationParams","crypta","runnerXMLConf"})
public class SetupRunner {
    private static final Logger logger = LoggerFactory.getLogger(SetupRunner.class);

    @Autowired
    RunnerFolders runnerFolders;

    @Autowired
    RunnerXMLConf runnerXMLConf;

    public SetupRunner(){


    }

    /*public static Properties getSetupProperties() {
        Properties properties = new Properties();

        try (InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("setup.properties")) {
            properties.load(io);
            for (Object propetry : properties.keySet())
                properties.put(propetry, properties.getProperty(propetry.toString()).replace("{user.home}", System.getProperty("user.home")));
        } catch (IOException ioe) {
            System.out.println("Ошибка считывания свойств " + ioe.getMessage());
            return null;
        }
        return properties;
    }*/

    public static String getInstallDir() {
        return "setupPath";
    }

    public static String geDBDir() {
        return getInstallDir() + "Databases.xml";
    }

    /*@PostConstruct
    public void InstallKurwanner() {

        for (Database db : xmLizer.getDBList()){
            prepareFolder(db);
        }
    }*/

    public static void prepareFolder(Database db) {
//        logger.warn(db.getFolder());
        (new File(db.getFolder() + "\\Success")).mkdirs();
        (new File(db.getFolder() + "\\Fail")).mkdirs();
    }

    public static void dropDbFolder(Database db, boolean force) throws SetupException {
        if (checkDbFolderContent(db.getFolder()) || force){
            deleteFolder(db.getFolder());
//            Loggator.commonLog(Level.INFO,"Каталог " + db.getFolder().getAbsolutePath() + " успешно удалён");
        }
        else throw new SetupException("Каталог БД {"+ db.getName() +"} содержит внутри себя файлы.\nВы уверены, что хотите удалить каталог?", Level.WARNING);
    }

    private static boolean checkDbFolderContent(File folder){
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory() || !checkDbFolderContent(file))
                    return false;
            }
        }
        return true;
    }

    private static void deleteFolder(File folder) throws SetupException{
        if (folder.isDirectory()){
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        if (!folder.delete())
            throw new SetupException("Ошибка удаления каталога: " + folder.getAbsolutePath() + "\n");
    }
}
