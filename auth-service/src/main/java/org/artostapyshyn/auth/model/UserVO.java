package com.artostapyshyn.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserVO {
    private String id;
    private String email;
    private String password;
    private String role;
}
