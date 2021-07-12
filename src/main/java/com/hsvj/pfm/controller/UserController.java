package com.hsvj.pfm.controller;

import com.hsvj.pfm.dto.UserSummary;
import com.hsvj.pfm.model.User;
import com.hsvj.pfm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSummary> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserSummary> getUserById(@PathVariable (name = "id") String id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @GetMapping(path ="/search/login/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummary> getUserByLogin(@PathVariable(name = "login") String login) {
        return ResponseEntity.ok().body(userService.findByLogin(login));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummary> createUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.createOrUpdateUser(user));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummary> updateUser(@PathVariable (name = "id") String id, @RequestBody User user){
        user.setId(id);
        return ResponseEntity.ok().body(userService.createOrUpdateUser(user));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.accepted().build();
    }
}
