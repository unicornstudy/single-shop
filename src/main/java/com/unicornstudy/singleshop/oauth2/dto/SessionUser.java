package com.unicornstudy.singleshop.oauth2.dto;

import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private final String name;
    private final String email;
    private final String picture;
    private final Role role;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}
