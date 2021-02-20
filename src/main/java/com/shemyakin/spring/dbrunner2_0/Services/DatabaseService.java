package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Exceptions.DatabaseServiceException;
import com.shemyakin.spring.dbrunner2_0.Exceptions.SetupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Scope("singleton")
public class DatabaseService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RunnerXMLConf runnerXMLConf;
    @Autowired
    private RunnerFolders runnerFolders;

    @PostConstruct
    @Profile("default")
    private void prepareBlankDB(){
        addRunnableDB((Database) applicationContext.getBean("OracleDatabaseByName", "DB1", "localhost:1521/XEPDB1", "DIMON", "Q1w2e3r4t5y6", true));
        addRunnableDB((Database) applicationContext.getBean("OracleDatabaseByName", "DB2", "localhost:1521/XEPDB2", "TECHUSER", "Q1w2e3r4t5y6", true));
        Database db = (Database) applicationContext.getBean("OracleDatabaseByName", "DB3", "localhost:1521/XEPDB3", "VIEWERUSER", "Q1w2e3r4t5y6", true);
        addRunnableDB(db);
        removeRunnableDB(db);
    }

    public void addRunnableDB(Database database){
        runnerXMLConf.createDB(database);
        runnerFolders.prepareDBFolders(database);
    }

    public void removeRunnableDB(Database database){
        runnerXMLConf.removeDB(database);
        try {
            runnerFolders.dropDbFolder(database,false);
        } catch (SetupException e) {
            e.printStackTrace();
        }
    }

    public Database getRunnableDBInfoByName(String name) throws DatabaseServiceException{
        Database db = runnerXMLConf.getDBInfoFromXMLByName(name);
        if (db == null)
            throw new DatabaseServiceException("Ошибка поиска БД: " + name);
        return db;
    }

    public void updateRunnableDBIsActiveAttribute(String databaseName, String status) throws DatabaseServiceException{
        if (!runnerXMLConf.setIsActiveAttribute(getRunnableDBInfoByName(databaseName), status))
            throw new DatabaseServiceException("Ошибка установки флага активности базе данных: " + databaseName);
    }

}
