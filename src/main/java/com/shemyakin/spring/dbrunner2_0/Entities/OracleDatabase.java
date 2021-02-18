package com.shemyakin.spring.dbrunner2_0.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

//@Component
public class OracleDatabase implements Database {
    @Getter @Setter private File folder;
    @Getter @Setter private String connection;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private Boolean isActive;

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
