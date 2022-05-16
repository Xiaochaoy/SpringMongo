package com.example.springmongo.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class UserDto {
    private int myId;
    private String email;
    private String password;
    private String fullName;

    public UserDto(int myId, String email, String password, String fullName) {
        this.myId = myId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public UserDto(User u) {
        this.myId = u.getMyId();
        this.email = u.getEmail();
        this.password = u.getPassword();
        this.fullName = u.getFullName();
    }
}
