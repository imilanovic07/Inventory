package com.bis.h2Inventory.Inventory.service.impl;

import com.bis.h2Inventory.Inventory.entity.Hardware;
import com.bis.h2Inventory.Inventory.exception.NotEnoughMoneyException;
import com.bis.h2Inventory.Inventory.exception.ResourceNotFoundException;
import com.bis.h2Inventory.Inventory.exception.WrongDateAndTimeForProductException;
import com.bis.h2Inventory.Inventory.repository.HardwareRepository;
import com.bis.h2Inventory.Inventory.repository.UserRepository;
import com.bis.h2Inventory.Inventory.service.HardwareService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HardwareServiceImpl implements HardwareService {

    private final HardwareRepository hardwareRepository;
    private final UserRepository userRepository;

    @Autowired
    public HardwareServiceImpl(HardwareRepository hardwareRepository, UserRepository userRepository) {
        this.hardwareRepository = hardwareRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Hardware> findAll() {
        return hardwareRepository.findAll();
    }

    @Override
    public Optional<Hardware> findById(final Long id) {
        return hardwareRepository.findById(id);
    }

    @Override
    public Hardware save(final Hardware hardware) {
        if (!checkIfHardwarePurchaseDateIsValid(hardware)) {
            throw new WrongDateAndTimeForProductException("Invalid date and time for hardware product");
        }
        return hardwareRepository.save(hardware);

    }

    @Override
    public void deleteById(final Long id) {
        hardwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hardware with id: " + id + " not found"));
        hardwareRepository.deleteById(id);
    }

   /* @Override
    public Hardware createUsersHardware(final Hardware hardware, final Long userId) {
        if (checkIfHardwarePurchaseDateIsValid(hardware)) {
            return userRepository.findById(userId).map(user -> {
                if (user.getCurrentBudget() >= hardware.getPrice()) {
                    user.setCurrentBudget(user.getCurrentBudget() - hardware.getPrice());
                    hardware.setUser(user);
                    return hardwareRepository.save(hardware);
                }
                throw new NotEnoughMoneyException("No enough money");
            }).orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        }
        throw new WrongDateAndTimeForProductException("Invalid date and time for hardware purchase date");
    }*/


    @Override
    public Hardware createUsersHardware(final Hardware hardware, final Long userId) {
        if (!checkIfHardwarePurchaseDateIsValid(hardware)) {
            throw new WrongDateAndTimeForProductException("Invalid date and time for hardware purchase date");
        }
        return userRepository.findById(userId).map(user -> {
            if (!(user.getCurrentBudget() >= hardware.getPrice())) {
                throw new NotEnoughMoneyException("No enough money");
            }
            user.setCurrentBudget(user.getCurrentBudget() - hardware.getPrice());
            hardware.setUser(user);
            return hardwareRepository.save(hardware);
        }).orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));

    }



    @Override
    public Hardware updateHardware(final Hardware hardwareBody, final Long hardwareId) {
        return hardwareRepository.findById(hardwareId).map(hardware -> {
            hardware.setName(hardwareBody.getName());
            hardware.setPrice(hardware.getPrice());
            hardware.setSerialNumber(hardwareBody.getSerialNumber());
            hardware.setDateOfPurchase(hardware.getDateOfPurchase());
            return hardwareRepository.save(hardware);
        }).orElseThrow(() -> new ResourceNotFoundException("Hardware with id: " + hardwareId + " not found"));
    }

    // USING DATE
    /* @Override
     public void buyOutHardware(final Long id) {
         Hardware hardware = hardwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hardware with id: " + id + " not found"));
         Date currentTimeStamp = new Date(System.currentTimeMillis());
         Date hardwareTimeStamp = hardware.getDateOfPurchase();
         long diff = currentTimeStamp.getTime() - hardwareTimeStamp.getTime();
         long daysPassed = diff / 86400000L;
         long yearsPassed = daysPassed / 365;
         long remainingDaysFromYearsPassed = daysPassed % 365L;
         String timeInHours = String.format("%1$tH:%1$tM:%1$tS.%1$tL", diff);
         if (diff > 94608000000L) { // 94608000000ms -> 3 years
             System.out.println(yearsPassed + " years " + remainingDaysFromYearsPassed + " days and " + timeInHours + " hours have passed since hardware was bought");
             System.out.println("Hardware will be bought and removed from db");
             hardwareRepository.deleteById(id);
         } else {
             System.out.println(yearsPassed + " years " + remainingDaysFromYearsPassed + " days and " + timeInHours + " hours have passed since hardware was bought");
             System.out.println("Days Left " + (1095 - daysPassed));
         }
     }
 */
    // USING LOCAL DATE
    @Override
    public void buyOutHardware(final Long id) {
        Hardware hardware = hardwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hardware with id: " + id + " not found"));
        LocalDate currentTimeStamp = LocalDate.now();
        LocalDate hardwareTimeStamp = hardware.getDateOfPurchase();
        Period period = Period.between(hardwareTimeStamp, currentTimeStamp);
        System.out.println("Hardware time: " + hardwareTimeStamp + "\nCurrent time: " + currentTimeStamp);
        if(period.getYears()>= 3){
            System.out.println(period.getYears()+" years " + period.getMonths() +" months and " + period.getDays()+ " days have passed hardware can be bought");
            hardwareRepository.deleteById(id);
        }
        else{
            System.out.println(period.getYears()+" years " + period.getMonths() +" months and " + period.getDays()+ " days. Hardware cannot be bought jet");
        }
    }


    private boolean checkIfHardwarePurchaseDateIsValid(final Hardware hardwareBody) {
        LocalDate currentTimeStamp = LocalDate.now();
        if (hardwareBody.getDateOfPurchase() == null) {
            throw new WrongDateAndTimeForProductException("Pls set date and time of purchase");
        } else {
            if (currentTimeStamp.compareTo(hardwareBody.getDateOfPurchase()) > 0) {
                return true;
            } else {
                throw new WrongDateAndTimeForProductException("Invalid date and time for product");
            }
        }
    }


}
