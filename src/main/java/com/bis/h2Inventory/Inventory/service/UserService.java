package com.bis.h2Inventory.Inventory.service;

import com.bis.h2Inventory.Inventory.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    User updateUser(User user, Long id);

    void deleteUser(Long userId);

    User addUser(User user);

    void addExistingHardwareToExistingUser(Long userId, Long hardwareId);

    void addNewBalance(Long userId);

}
