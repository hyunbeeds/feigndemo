package com.polarbear.feigndemo.config.infra.feign;

import feign.Request;
import feign.Response;
import org.springframework.http.HttpStatus;

public interface FeignExceptionHandler {

    Exception handle(String methodKey, Response response, String body);

    default String getDefaultErrorMessage(String methodKey, Response response, String body) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        Request request = response.request();

        return String.format("[FAIL] URL : %s, method : %s, status : %s(%d), methodKey : %s, ResponseBody : %s",
                request.url(), request.httpMethod(), status.name(), status.value(), methodKey, body);
    }
}
