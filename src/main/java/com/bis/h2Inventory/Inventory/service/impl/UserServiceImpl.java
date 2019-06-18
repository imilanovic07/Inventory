package com.bis.h2Inventory.Inventory.service.impl;

import com.bis.h2Inventory.Inventory.entity.Hardware;
import com.bis.h2Inventory.Inventory.entity.User;
import com.bis.h2Inventory.Inventory.exception.ResourceNotFoundException;
import com.bis.h2Inventory.Inventory.repository.HardwareRepository;
import com.bis.h2Inventory.Inventory.repository.UserRepository;
import com.bis.h2Inventory.Inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HardwareRepository hardwareRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, HardwareRepository hardwareRepository) {
        this.userRepository = userRepository;
        this.hardwareRepository = hardwareRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(final User userBody, final Long id) {
        return userRepository.findById(id).map(user -> {
            user.setId(id);
            user.setFirstName(userBody.getFirstName());
            user.setLastName(userBody.getLastName());
            user.setOib(userBody.getOib());
            user.setUserRank(userBody.getUserRank());
            user.setInitialBudget(getBudgetForUser(user));
            user.setCurrentBudget(user.getInitialBudget() - getUsersItemValue(user));
            user.setHardwareList(user.getHardwareList()); // may or may not include this in final version
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
    }

    @Override
    public void addExistingHardwareToExistingUser(final Long userId, final Long hardwareId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        hardwareRepository.findById(hardwareId).map(hardware -> {
            if (hardware.getUser() != null) {
                throw new RuntimeException("Changing users hardware to another user is not allowed");
            }
            hardware.setUser(user);
            user.setCurrentBudget(user.getCurrentBudget() - hardware.getPrice());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Hardware with id " + hardwareId + " not found"));
    }

    @Override
    public void deleteUser(final Long userId) {
        hardwareRepository.findAll().forEach(hardware -> {
            if (hardware.getUser() != null) {
                if (hardware.getUser().getId().equals(userId)) {
                    hardware.setUser(null);
                }
            }
        });
        userRepository.deleteById(userId);
    }

    @Override
    public User addUser(final User user) {
        user.setInitialBudget(getBudgetForUser(user));
        user.setCurrentBudget(user.getInitialBudget());
        return userRepository.save(user);
    }

    public void addNewBalance(final Long userId) {
        userRepository.findById(userId).map(user -> {
            user.setCurrentBudget(user.getCurrentBudget() + getBudgetForUser(user));
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found"));
    }

    private Float getBudgetForUser(User user) {
        switch (user.getUserRank()) {
            case JUNIOR:
                return 10000.0F;
            case MID:
                return 15000.0F;
            case SENIOR:
                return 20000.0F;
            default:
                return 0.0F;
        }
    }

    // adding some comments for git
    private float getUsersItemValue(final User user) {
        float currentItemValueForUser = 0.0F;
        List<Hardware> usersHardware;
        usersHardware = hardwareRepository.findAll();
        for (Hardware hardware : usersHardware) {
            if (hardware.getUser() != null) {
                if (hardware.getUser().getId().equals(user.getId())) {
                    currentItemValueForUser = currentItemValueForUser + hardware.getPrice();
                }
            }
        }
        return currentItemValueForUser;
    }

}




