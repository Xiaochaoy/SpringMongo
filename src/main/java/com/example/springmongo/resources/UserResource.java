package com.example.springmongo.resources;

import com.example.springmongo.controller.UserController;
import com.example.springmongo.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UserResource.USER_RESOURCE)
public class UserResource {
    public final static String USER_RESOURCE = "v0/users";
    UserController userController;

    @Autowired
    public UserResource(UserController userController) {
        this.userController = userController;
    }

    @GetMapping
    public List<UserDto> usersDto(){
        return userController.getAllUsers();
    }

    @GetMapping("{id}")
    public UserDto user(@PathVariable("id") Integer id){
        return userController.getUser(id);
    }

    @GetMapping("{id}/email")
    public Map<String,String> email(@PathVariable("id") Integer id){
        return Collections.singletonMap("email",userController.getUser(id).getEmail());
    }

    @PostMapping
    public void addUser(@RequestBody UserDto userdto){
        userController.addUser(userdto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id){
        userController.deleteUser(id);
    }

    @PutMapping("{id}")
    public void putUser(@RequestBody UserDto userDto, @PathVariable("id") Integer id){
        userController.putUser(userDto, id);
    }

    @PatchMapping("{id}")
    public void patchUser(@PathVariable("id") Integer id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        userController.patchUser(id,patch);
    }
}

