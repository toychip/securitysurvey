package com.nice.securitypage.config;

import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class ManagerDataInit {

    private final ManagerRepository managerRepository;

//    @PostConstruct
    public void init(){
        Manager admin = Manager.builder()
                .username("admin")
                .password(encryptPassword("12345678"))
                .build();
        managerRepository.save(admin);
    }
    private String encryptPassword(String password) {
        String md5Password = EncryptionUtil.encryptMD5(password);
        return EncryptionUtil.encryptSHA256(md5Password);
    }
}
