package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Exceptions.DatabaseServiceException;

import java.util.List;

public interface DatabaseService {

    List<Database> getDBAvailableList();

    void addDB(Database database);

    void removeDB(Database database);

    Database getDBInfoByName(String name) throws DatabaseServiceException;

    void updateDBIsActiveAttribute(String databaseName, String status) throws DatabaseServiceException;

}
