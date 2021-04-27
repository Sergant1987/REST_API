package org.marchenko.controller;

import org.marchenko.model.User;
import org.marchenko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(@RequestParam(required = false) String name,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                               @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
        if (name == null || name.trim().isEmpty()) {
            return userService.findAllUsers(pageNumber, pageSize);
        } else {
            return userService.findUsersByName(name, pageNumber, pageSize);
        }
    }


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestParam String name,
                           @RequestParam String phone) {
        return userService.createUser(name, phone);
    }

    @DeleteMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserById(@PathVariable("user_id") Long userId) {
        return userService.findUserById(userId);
    }

    @PatchMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable("user_id") Long userId,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String phone) {
        return userService.updateUser(userId, name, phone);
    }

}
