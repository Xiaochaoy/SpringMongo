package com.example.springmongo.controller;

import com.example.springmongo.model.User;
import com.example.springmongo.repositories.UserDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void addUser(User user) {
        userDao.save(user);
    }

    public User getUser(Long id) {
        Optional<User> users = userDao.findById(id);
        return users.get();
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        userDao.delete(user);
    }

    public void putUser(User user, Long id) {

        User real = getUser(id);

        real.setEmail(user.getEmail());
        real.setPassword(user.getPassword());
        real.setFullName(user.getFullName());

        userDao.save(real);
    }

    public void patchUser(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = getUser(id);
        User userPatched = applyPatch(patch, user);

        userDao.save(userPatched);

    }

    private User applyPatch(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }
}
    /*
    {
        "op":"replace",
        "path":"/email",
        "value":"afafqwr"
    }
    {
        "op":"add",
        "path":"/email/0",
        "value":"Bread"
    }
    {
        "op":"remove",
        "path":"/fullName"
    }
    {
        "op":"move",
        "from":"/email/0",
        "path":"/email/?"
    }
    {
        "op":"copy",
        "from":"/email/0",
        "path":"/email/?"
    }
    {
        "op":"test",
        "path":"/fullName",
        "value":"Rataaaa"
    }
*/