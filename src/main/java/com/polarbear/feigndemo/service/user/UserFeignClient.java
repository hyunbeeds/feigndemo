package com.polarbear.feigndemo.service.user;

import com.polarbear.feigndemo.config.infra.feign.FeignDefaultErrorDecoder;
import com.polarbear.feigndemo.config.infra.feign.HandleFeignError;
import com.polarbear.feigndemo.controller.view.UserView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@HandleFeignError(FeignDefaultErrorDecoder.class)
@FeignClient(name = "user", url = "${feign.client.config.user.url}")
public interface UserFeignClient {

    @HandleFeignError(UserFeignExceptionHandlerForGetUser.class)
    @GetMapping("/users/{id}")
    UserView getUser(@PathVariable("id") Long id);

}
