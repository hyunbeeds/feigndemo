package com.polarbear.feigndemo.controller;

import com.polarbear.feigndemo.controller.view.UserView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/users/{id}")
    public UserView getUser(@PathVariable Long id) {
        return UserView.createUser(id);
    }

    @GetMapping("/users")
    public List<UserView> getUsers(@RequestParam int num) {
        List<UserView> users = new ArrayList<>();
        for (int i=0;i<num;i++) {
            users.add(UserView.createUser((long) i));
        }
        return users;
    }

    @GetMapping("/error")
    public void error() {
        throw new RuntimeException();
    }

}
