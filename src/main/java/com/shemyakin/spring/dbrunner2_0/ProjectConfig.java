package com.shemyakin.spring.dbrunner2_0;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Entities.OracleDatabase;
import com.shemyakin.spring.dbrunner2_0.Entities.SetupException;
import com.shemyakin.spring.dbrunner2_0.Services.SetupRunner;
import com.shemyakin.spring.dbrunner2_0.Services.XMLizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import java.io.File;

@Configuration
@DependsOn("runnerConfigurationParams")
@ComponentScan("com.shemyakin.spring.dbrunner2_0")
public class ProjectConfig {

    @Autowired
    RunnerConfigurationParams runnerConfigurationParams;

    @Bean(name = "OracleDatabase")
    @Scope("prototype")
    public Database createDBOracleFile(File folder, String connection, String username, String password, Boolean isActive){
        return new OracleDatabase(folder,connection,username,password,isActive);
    }

    @Bean(name = "OracleDatabase")
    @Scope("prototype")
    public Database createDBOracleName(String folder, String connection, String username, String password, Boolean isActive){
        return createDBOracleFile(new File (runnerConfigurationParams.getSetupPath() + folder),connection,username,password,isActive);
    }

  /*  @Bean("xmlizer")
    @Scope("singleton")
    public XMLizer xmLizer(){
        return new XMLizer();
    }

    @Bean("setupRunner")
    @DependsOn({"xmlizer","runnerConfigurationParams"})
    public SetupRunner setupRunner(RunnerConfigurationParams runnerConfigurationParams) {
        return new SetupRunner(runnerConfigurationParams);
    }

    @Bean("runnerConfigurationParams")
    @ConfigurationProperties(prefix = "runner")
    public RunnerConfigurationParams runnerConfigurationParams() {
        return new RunnerConfigurationParams();
    }*/

/*    @Bean(name = "SetupException")
    @Scope("prototype")
    public SetupException createSetupException(String message){
        return new SetupException(message);
    }*/
}
