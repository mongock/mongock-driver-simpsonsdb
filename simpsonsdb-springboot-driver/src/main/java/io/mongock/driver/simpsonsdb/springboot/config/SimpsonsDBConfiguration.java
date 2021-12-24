package io.mongock.driver.simpsonsdb.springboot.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mongock.simpsons-db")
public class SimpsonsDBConfiguration {

    private String configParameter;

    public String getConfigParameter() {
        return configParameter;
    }

    public void setConfigParameter(String configParameter) {
        this.configParameter = configParameter;
    }
}
