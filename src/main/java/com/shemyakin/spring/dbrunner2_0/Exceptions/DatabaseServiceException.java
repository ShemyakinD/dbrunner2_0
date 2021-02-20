package com.shemyakin.spring.dbrunner2_0.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseServiceException extends Exception {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceException.class);

    public DatabaseServiceException(String message){
        super(message);
        logger.error("Ошибка поиска БД. Сообщение: " + message);
    }
}
