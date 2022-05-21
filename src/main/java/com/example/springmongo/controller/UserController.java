package com.example.springmongo.controller;

import com.example.springmongo.model.Product;
import com.example.springmongo.model.User;
import com.example.springmongo.repositories.UserDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class UserController {
    UserDao userDao;
    ProductController productController;
    @Autowired
    public UserController(UserDao userDao, ProductController productController) {
        this.userDao = userDao;
        this.productController = productController;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getUser(int id) {
        return userDao.findById(id).get();
    }

    public void addUser(User user) {
        List<Product> products = user.getProducts();
        productController.addAllProducts(products);
        userDao.save(user);
    }

    public void deleteUser(int id) {
        User user = getUser(id);
        userDao.delete(user);
    }

    public void putUser(User user, int id) {

        User real = getUser(id);
        real.setEmail(user.getEmail());
        real.setName(user.getName());

        List<Product> products = user.getProducts();
        for (Product p: real.getProducts()){
            for (Product pp: products){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    p.setQuantity(pp.getQuantity());
                }
            }
        }
        productController.actualizarTodo(user.getProducts());

        userDao.save(real);
    }

    public void patchUser(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = getUser(id);
        User userPatched = applyPatch(patch, user);

        if (user.getProducts().size() == userPatched.getProducts().size()){
            productController.actualizarTodo(userPatched.getProducts());
        }else if (user.getProducts().size() < userPatched.getProducts().size()){
            productController.addAllProducts(userPatched.getProducts());
        }

        userDao.save(userPatched);

    }

    private User applyPatch(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    public void addProduct(Product product, int id) {
        User u = getUser(id);
        productController.añadir(product);
        Product p = productController.getProduct(product.getId());
        boolean encontrar = false;
        for (Product pp : u.getProducts()){
            if (pp.getId() == p.getId()) {
                encontrar = true;
                break;
            }
        }
        if (!encontrar){
            u.addProduct(p);
        }
        userDao.save(u);
    }

    public void deleteProductOnUser(int id, int index) {
        User u = getUser(id);
        u.getProducts().remove(index);
        userDao.save(u);
    }

}
    /*
    {
        "op":"replace",
        "path":"/pruducts/0/name",
        "value":"afafqwr"
    }
    {
        "op":"add",
        "path":"/products/0",
        "value":{
                "id": 5,
                "name": "alantonto",
                "quantity": 9,
                "precio": 99
                }
    }
    {
        "op":"remove",
        "path":"/fullName"
    }
    {
        "op":"move",
        "from":"/products/0",
        "path":"/products/2"
    }
    {
        "op":"copy",
        "from":"/products/0",
        "path":"/products/3"
    }
    {
        "op":"test",
        "path":"/fullName",
        "value":"Rataaaa"
    }
*/