package com.bis.h2Inventory.Inventory.Controller;

import com.bis.h2Inventory.Inventory.entity.User;
import com.bis.h2Inventory.Inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{userId}")
    public Optional<User> findUserById(@PathVariable(value = "userId") final Long userId) {
        return userService.findById(userId);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody final User userBody) {
        return userService.addUser(userBody);
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@RequestBody final User userBody,
                           @PathVariable(value = "userId") final Long userId) {
        return userService.updateUser(userBody, userId);
    }

    @PutMapping("users/{userId}/hardware/{hardwareId}")
    public void addExistingHardwareToExistingUser(@PathVariable(value = "userId") final Long userId,
                                                  @PathVariable(value = "hardwareId") final Long hardwareId) {
        userService.addExistingHardwareToExistingUser(userId, hardwareId);
    }

    @PutMapping("updateUsersBalance/{userId}")
    public void updateUsersBalance(@PathVariable(value = "userId") final Long userId) {
        userService.addNewBalance(userId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@PathVariable(value = "userId") final Long userId) {
        userService.deleteUser(userId);
    }

}
