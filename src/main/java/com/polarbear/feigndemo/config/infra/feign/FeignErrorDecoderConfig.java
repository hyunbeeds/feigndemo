package com.polarbear.feigndemo.config.infra.feign;

import com.polarbear.feigndemo.config.error.exceptions.ClientException;
import com.polarbear.feigndemo.config.error.exceptions.ServerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignErrorDecoderConfig {

    @Bean
    public ExceptionHandlingFeignErrorDecoder exceptionHandlingFeignErrorDecoder(ApplicationContext applicationContext) {
        return new ExceptionHandlingFeignErrorDecoder(
                applicationContext, new FeignDefaultErrorDecoder(new ClientException(), new ServerException())
        );
    }
}
