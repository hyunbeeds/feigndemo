package com.polarbear.feigndemo.config.infra.feign;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

@Configuration
public class FeignDefaultConfig {


    // 해당 설정 없을시 requestBody, requestParam에 localDateTime 있을시 parsing 이상하게 될 수 있음!
    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }
}
