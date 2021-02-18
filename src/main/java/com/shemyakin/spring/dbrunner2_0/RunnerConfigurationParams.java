package com.shemyakin.spring.dbrunner2_0;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "runner")
@Setter
@Getter
public class RunnerConfigurationParams {
    private String setupPath;
    private String xmlConfigurePath;
}
