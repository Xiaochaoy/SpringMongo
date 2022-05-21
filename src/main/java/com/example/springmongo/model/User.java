package com.example.springmongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;


@Data
@Document(collection = "users") // db.???.find()
public class User {

    @Id
    private int id;

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    private String name;
    private String email;
    @DBRef
    private List<Product> products;

    public void addProduct(Product product){
        products.add(product);
    }

}
