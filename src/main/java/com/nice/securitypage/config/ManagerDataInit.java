package com.nice.securitypage.config;

import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
                .createdDate(LocalDateTime.now())
                .build();

        Manager before90day = Manager.builder()
                .username("before90day")
                .password(encryptPassword("90"))
                .createdDate(LocalDateTime.now().minusDays(91))
                .build();

        Manager b11111111 = Manager.builder()
                .username("b11111111")
                .password(encryptPassword("11111111"))
                .createdDate(LocalDateTime.now())
                .build();

        Manager locked = Manager.builder()
                .username("locktest")
                .password(encryptPassword("1234"))
                .createdDate(LocalDateTime.now())
                .build();


        List<Manager> managerList = Arrays.asList(admin, before90day, b11111111, locked);

        managerRepository.saveAll(managerList);
    }

    // 비밀번호 암호화
    private String encryptPassword(String password) {
        String md5Password = EncryptionUtil.encryptMD5(password);
        return EncryptionUtil.encryptSHA256(md5Password);
    }
}
