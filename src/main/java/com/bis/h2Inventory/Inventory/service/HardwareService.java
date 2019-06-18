package com.bis.h2Inventory.Inventory.service;

import com.bis.h2Inventory.Inventory.entity.Hardware;

import java.util.List;
import java.util.Optional;

public interface HardwareService {

    List<Hardware> findAll();

    Optional<Hardware> findById(Long id);

    Hardware save(Hardware hardware);

    void deleteById(Long id);

    Hardware updateHardware(Hardware hardwareBody, Long hardwareId);

    Hardware createUsersHardware(Hardware hardware, Long userId);

    void buyOutHardware(Long id);

}
