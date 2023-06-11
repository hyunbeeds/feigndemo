package com.polarbear.feigndemo.config.infra.resttemplate;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
public class RestTemplateProperty {

    private String host;
    private Long readTimeout;
    private Long connectionTimeout;
    private Long maxConnectionPerRoute;
    private Long maxConnectionCount;
}
