package com.shemyakin.spring.dbrunner2_0.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.text.ParseException;

@Getter
@Setter
public class DatabaseDTO {

    private String name;
    private String connection;
    private String username;
    private Boolean isActive;

    public void getNameFromFolder(File folder){
        this.name = folder.getName();
    }

}
