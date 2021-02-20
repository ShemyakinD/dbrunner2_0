package com.shemyakin.spring.dbrunner2_0.Entities;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Objects;

@Getter @Setter
public class OracleDatabase implements Database {
    private File folder;
    private String connection;
    private String username;
    private String password;
    private Boolean isActive;

    public String getName(){
        return folder.getName();
    }

    public OracleDatabase(File folder, String connection, String username, String password, Boolean isActive) {
        this.folder = folder;
        this.connection = connection;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public OracleDatabase(String folder, String connection, String username, String password, Boolean isActive) {
        this(new File(folder),connection,username,password,isActive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OracleDatabase oracleDatabase = (OracleDatabase) o;
        return getName().equals(oracleDatabase.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection, username);
    }

    @Override
    public String toString() {
        return "Database{" +
                "name=" + getName() +
                ", connection='" + connection + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
