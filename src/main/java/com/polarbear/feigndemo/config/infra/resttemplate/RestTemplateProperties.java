package com.polarbear.feigndemo.config.infra.resttemplate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateProperties {

    @Bean("userServiceProperty")
    @ConfigurationProperties("resttemplate.configs.user")
    public RestTemplateProperty userServiceProperty() {
        return new RestTemplateProperty();
    }
}
