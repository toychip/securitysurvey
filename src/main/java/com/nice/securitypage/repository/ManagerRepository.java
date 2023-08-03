package com.nice.securitypage.repository;

import com.nice.securitypage.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUsername(String username);
    /*
    Mysql
    SELECT * FROM manager WHERE username = ?
     */
}
