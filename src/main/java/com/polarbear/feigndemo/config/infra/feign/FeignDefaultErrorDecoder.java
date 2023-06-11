package com.polarbear.feigndemo.config.infra.feign;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignDefaultErrorDecoder implements FeignExceptionHandler {

    private final Exception clientError;
    private final Exception serverError;

    public FeignDefaultErrorDecoder(Exception clientError, Exception serverError) {
        this.clientError = clientError;
        this.serverError = serverError;
    }

    @Override
    public Exception handle(String methodKey, Response response, String body) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is5xxServerError()) {
            log.error(this.getDefaultErrorMessage(methodKey, response, body));
            return serverError;
        } else {
            return clientError;
        }
    }
}
