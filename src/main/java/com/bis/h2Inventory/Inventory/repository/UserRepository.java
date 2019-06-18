package com.bis.h2Inventory.Inventory.repository;

import com.bis.h2Inventory.Inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
