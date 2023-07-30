package com.nice.securitypage.service;

import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.dto.ManagerDto;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    public boolean login(ManagerDto managerDto) {
        String username = managerDto.getUsername();
        String providedPassword = managerDto.getPassword();

        // Encrypt the provided password
        String md5Password = EncryptionUtil.encryptMD5(providedPassword);
        String sha256Password = EncryptionUtil.encryptSHA256(md5Password);

        Manager manager = managerRepository.findByUsername(username)
                .orElse(null);

        if (manager == null) {
            return false;
        }

        // Check the provided password against the one in the database
        if (!manager.getPassword().equals(sha256Password)) {
            return false;
        }
        System.out.println("Username: " + username);
        System.out.println("Password: " + providedPassword);
        // If everything is fine, return true
        return true;
        // Continue with the login process
    }

}
