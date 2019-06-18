package com.bis.h2Inventory.Inventory.Controller;


import com.bis.h2Inventory.Inventory.entity.Hardware;
import com.bis.h2Inventory.Inventory.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HardwareController {

    private final HardwareService hardwareService;

    @Autowired
    public HardwareController(HardwareService hardwareService) {
        this.hardwareService = hardwareService;
    }

    @GetMapping("/hardware")
    public List<Hardware> findAllHardware() {
        return hardwareService.findAll();
    }

    @GetMapping("/hardware/{hardwareId}")
    public Optional<Hardware> findById(@PathVariable(value = "hardwareId") final Long hardwareId) {
        return hardwareService.findById(hardwareId);
    }

    @PostMapping("/hardware")
    public Hardware addHardware(@RequestBody final Hardware hardwareBody) {
        return hardwareService.save(hardwareBody);
    }

    @PostMapping("/users/{userId}/hardware")
    public Hardware createUsersHardware(@PathVariable(value = "userId") final Long userId,
                                        @RequestBody final Hardware hardwareBody) {
        return hardwareService.createUsersHardware(hardwareBody, userId);
    }

    @PutMapping("/hardware/{hardwareId}")
    public Hardware updateHardware(@PathVariable(value = "hardwareId") final Long hardwareId,
                                   @RequestBody final Hardware hardwareBody) {
        return hardwareService.updateHardware(hardwareBody, hardwareId);
    }

    @DeleteMapping("/hardware/{hardwareId}")
    public void deleteHardware(@PathVariable(value = "hardwareId") final Long hardwareId) {
        hardwareService.deleteById(hardwareId);
    }

    @DeleteMapping("/hardware/buyout/{hardwareId}")
    public void buyOutHardware(@PathVariable(value = "hardwareId") final Long hardwareID){
        hardwareService.buyOutHardware(hardwareID);

    }

}
