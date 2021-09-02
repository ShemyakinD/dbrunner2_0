package com.shemyakin.spring.dbrunner2_0.Services.Implemetations;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Exceptions.DatabaseServiceException;
import com.shemyakin.spring.dbrunner2_0.Exceptions.SetupException;
import com.shemyakin.spring.dbrunner2_0.Services.DatabaseService;
import com.shemyakin.spring.dbrunner2_0.Services.RunnerFolders;
import com.shemyakin.spring.dbrunner2_0.Services.RunnerXMLConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Scope("singleton")
public class DatabaseServiceImpl implements DatabaseService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RunnerXMLConf runnerXMLConf;
    @Autowired
    private RunnerFolders runnerFolders;

    @PostConstruct
    @Profile("default")
    private void prepareBlankDB(){
        addDB((Database) applicationContext.getBean("OracleDatabaseByName", "DB1", "localhost:1521/XEPDB1", "DIMON", "Q1w2e3r4t5y6", true));
        addDB((Database) applicationContext.getBean("OracleDatabaseByName", "DB2", "localhost:1521/XEPDB2", "TECHUSER", "Q1w2e3r4t5y6", true));
        Database db = (Database) applicationContext.getBean("OracleDatabaseByName", "DB3", "localhost:1521/XEPDB3", "VIEWERUSER", "Q1w2e3r4t5y6", true);
        addDB(db);
        removeDB(db);
    }

    @Override
    public List<Database> getDBAvailableList() {
        return runnerXMLConf.getDBListFromXml();
    }

    public void addDB(Database database){
        runnerXMLConf.createDB(database);
        runnerFolders.prepareDBFolders(database);
    }

    public void removeDB(Database database){
        runnerXMLConf.removeDB(database);
        try {
            runnerFolders.dropDbFolder(database,false);
        } catch (SetupException e) {
            e.printStackTrace();
        }
    }

    public Database getDBInfoByName(String name) throws DatabaseServiceException {
        Database db = runnerXMLConf.getDBInfoFromXMLByName(name);
        if (db == null)
            throw new DatabaseServiceException("Ошибка поиска БД: " + name);
        return db;
    }

    public void updateDBIsActiveAttribute(String databaseName, String status) throws DatabaseServiceException{
        if (!runnerXMLConf.setIsActiveAttribute(getDBInfoByName(databaseName), status))
            throw new DatabaseServiceException("Ошибка установки флага активности базе данных: " + databaseName);
    }

}
