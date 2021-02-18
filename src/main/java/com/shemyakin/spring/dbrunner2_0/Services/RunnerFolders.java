package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Entities.SetupException;
import com.shemyakin.spring.dbrunner2_0.RunnerConfigurationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.logging.Level;

@Service
@Scope("singleton")
@DependsOn("runnerConfigurationParams")
public class RunnerFolders {

    private static final Logger logger = LoggerFactory.getLogger(RunnerFolders.class);

    @Autowired
    private RunnerConfigurationParams runnerConfigurationParams;

/*    public static String getInstallDir() {
        return "setupPath";
    }*/

/*    public String geDBDir() {
        return runnerConfigurationParams.getSetupPath() + "Databases.xml";
    }*/

/*    @PostConstruct
    public void InstallKurwanner() {

        for (Database db : xmLizer.getDBList()){
            prepareFolder(db);
        }
    }*/

    @PostConstruct
    public void prepareRunnerFolder() {
//        logger.warn(db.getFolder());
        logger.warn("INSTALL DIR - " + runnerConfigurationParams.getSetupPath());
        (new File(runnerConfigurationParams.getSetupPath())).mkdirs();
    }

    public void prepareDBFolders(Database db) {
//        logger.warn(db.getFolder());
        (new File(db.getFolder() + "\\Success")).mkdirs();
        (new File(db.getFolder() + "\\Fail")).mkdirs();
    }

    public void dropDbFolder(Database db, boolean force) throws SetupException {
        if (checkDbFolderContent(db.getFolder()) || force){
            deleteFolder(db.getFolder());
//            Loggator.commonLog(Level.INFO,"Каталог " + db.getFolder().getAbsolutePath() + " успешно удалён");
        }
        else throw new SetupException("Каталог БД {"+ db.getName() +"} содержит внутри себя файлы.\nВы уверены, что хотите удалить каталог?", Level.WARNING);
    }

    private boolean checkDbFolderContent(File folder){
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory() || !checkDbFolderContent(file))
                    return false;
            }
        }
        return true;
    }

    private void deleteFolder(File folder) throws SetupException{
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
