package com.polarbear.feigndemo.service.user;

import com.polarbear.feigndemo.config.error.exceptions.ServerException;
import com.polarbear.feigndemo.config.infra.feign.FeignExceptionHandler;
import feign.Response;
import org.springframework.http.HttpStatus;

public class UserFeignExceptionHandlerForGetUser implements FeignExceptionHandler {

    @Override
    public Exception handle(String methodKey, Response response, String body) {
        if (HttpStatus.NOT_FOUND.equals(HttpStatus.resolve(response.status()))) {
            return new UserNotFoundException();
        }
        return new ServerException();
    }
}
