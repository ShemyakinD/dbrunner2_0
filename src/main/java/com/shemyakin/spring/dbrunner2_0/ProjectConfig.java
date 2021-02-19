package com.shemyakin.spring.dbrunner2_0;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Entities.OracleDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.io.File;

@Configuration
@DependsOn("runnerConfigurationParams")
@ComponentScan("com.shemyakin.spring.dbrunner2_0")
public class ProjectConfig {

    @Autowired
    RunnerConfigurationParams runnerConfigurationParams;

    @Bean(name = "OracleDatabaseByFolder")
    @Scope("prototype")
    public Database createDBOracleFile(File folder, String connection, String username, String password, Boolean isActive){
        return new OracleDatabase(folder,connection,username,password,isActive);
    }

    @Bean(name = "OracleDatabaseByName")
    @Scope("prototype")
    public Database createDBOracleName(String folder, String connection, String username, String password, Boolean isActive){
        return createDBOracleFile(new File (runnerConfigurationParams.getSetupPath() + folder),connection,username,password,isActive);
    }

}
