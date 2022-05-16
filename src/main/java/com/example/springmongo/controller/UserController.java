package com.example.springmongo.controller;

import com.example.springmongo.model.User;
import com.example.springmongo.model.UserDto;
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

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(new UserDto(u));
        }
        return usersDto;
    }

    public void addUser(UserDto userDto) {
        User user = new User(userDto);
        userDao.save(user);
    }

    public UserDto getUser(Integer id) {
        Optional<User> users = userDao.findByMyId(id);
        UserDto userDto = new UserDto(users.get());
        return userDto;
    }

    public void deleteUser(Integer id) {
        userDao.deleteById(id);
    }

    public void putUser(UserDto userDto, Integer id) {
        User user = new User(userDto);

        User real = new User(getUser(id));

        //real.setId(user.getId());
        real.setEmail(user.getEmail());
        real.setPassword(user.getPassword());
        real.setFullName(user.getFullName());

        userDao.save(real);
    }

    public void patchUser(Integer id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = new User(getUser(id));
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