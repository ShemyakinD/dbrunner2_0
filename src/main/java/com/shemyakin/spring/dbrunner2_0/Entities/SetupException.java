package com.shemyakin.spring.dbrunner2_0.Entities;

import com.shemyakin.spring.dbrunner2_0.Dbrunner20Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

public class SetupException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(SetupException.class);
    public SetupException(String errorMessage){
        super(errorMessage);
        logger.error(errorMessage);
    }
    public SetupException(String errorMessage, Level level){
        super(errorMessage);
        logger.warn(errorMessage);
    }
}
