package com.example.springmongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;


@Data
@Document(collection = "users") // db.???.find()
public class User {

    @Id
    private BigInteger _id;
    private Integer myId;
    private String email;
    private String password;
    private String fullName;

    public User(Integer myId, String email, String password, String fullName) {
        this.myId = myId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public User() {

    }

    public User(UserDto userDto) {
        this.myId = userDto.getMyId();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.fullName = userDto.getFullName();
    }
}
