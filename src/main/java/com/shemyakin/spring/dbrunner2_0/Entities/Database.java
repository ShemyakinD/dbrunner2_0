package com.shemyakin.spring.dbrunner2_0.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public interface Database {
    String getConnection();
    String getUsername();
    String getPassword();
    Boolean getIsActive();
    File getFolder();
    String getName();
}
