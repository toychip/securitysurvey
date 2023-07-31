package com.nice.securitypage.config;

import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
// 스프링 컨테이너 실행시 데이터 등록
public class ManagerDataInit {

    private final ManagerRepository managerRepository;

//    @PostConstruct
    public void init(){
        Manager admin = Manager.builder()
                .username("test")
                .password(encryptPassword("1234"))
                .build();
        managerRepository.save(admin);
    }

    // 비밀번호 암호화
    private String encryptPassword(String password) {
        String md5Password = EncryptionUtil.encryptMD5(password);
        return EncryptionUtil.encryptSHA256(md5Password);
    }
}
