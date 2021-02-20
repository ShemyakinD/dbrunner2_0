package com.shemyakin.spring.dbrunner2_0.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
