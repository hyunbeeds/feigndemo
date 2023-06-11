package com.polarbear.feigndemo.controller.view;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserView {

    private Long id;
    private String name;

    public UserView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserView createUser(Long id) {
        return new UserView(id, id.toString());
    }
}
