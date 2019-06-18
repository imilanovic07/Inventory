package com.bis.h2Inventory.Inventory.repository;

import com.bis.h2Inventory.Inventory.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareRepository extends JpaRepository<Hardware, Long> {

}
