package com.polarbear.feigndemo.controller;

import com.polarbear.feigndemo.controller.view.UserView;
import com.polarbear.feigndemo.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test/get-user-by-feign-client")
    public UserView testGetUserByFeignClient(@RequestParam Long id) {
        return userService.getUserByFeignClient(id);
    }

    @GetMapping("/test/get-user-by-rest-template")
    public UserView testGetUserByRestTemplate(@RequestParam Long id) {
        return userService.getUserByRestTemplate(id);
    }
}
