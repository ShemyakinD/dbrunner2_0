package com.shemyakin.spring.dbrunner2_0;

import com.shemyakin.spring.dbrunner2_0.Services.Engine;
import com.shemyakin.spring.dbrunner2_0.Services.RunnerXMLConf;
import com.shemyakin.spring.dbrunner2_0.Services.RunnerFolders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Dbrunner20Application {
    private static final Logger logger = LoggerFactory.getLogger(Dbrunner20Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Dbrunner20Application.class, args);

    }

}
