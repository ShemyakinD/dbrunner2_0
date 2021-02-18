package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Entities.SetupException;
import com.shemyakin.spring.dbrunner2_0.RunnerConfigurationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Repository
@Scope("singleton")
public class DatabaseChanger {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RunnerXMLConf runnerXMLConf;
    @Autowired
    private RunnerFolders runnerFolders;
    @Autowired
    private RunnerConfigurationParams runnerConfigurationParams;

    @PostConstruct
    @Profile("default")
    private void prepareBlankDB(){

        AddRunnableDB((Database) applicationContext.getBean("OracleDatabase", "DB1", "localhost:1521/XEPDB1", "DIMON", "Q1w2e3r4t5y6", true));
        AddRunnableDB((Database) applicationContext.getBean("OracleDatabase", "DB2", "localhost:1521/XEPDB2", "TECHUSER", "Q1w2e3r4t5y6", true));
        AddRunnableDB((Database) applicationContext.getBean("OracleDatabase", "DB3", "localhost:1521/XEPDB3", "VIEWERUSER", "Q1w2e3r4t5y6", true));
    }

    public void AddRunnableDB(Database database){
        runnerXMLConf.createDB(database);
        runnerFolders.prepareDBFolders(database);
    }

    public void RemoveRunnableDB(Database database){
        runnerXMLConf.removeDB(database);
        try {
            runnerFolders.dropDbFolder(database,false);
        } catch (SetupException e) {
            e.printStackTrace();
        }
    }

}
