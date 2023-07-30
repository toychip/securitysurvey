package com.nice.securitypage.service;

import com.nice.securitypage.dto.ManagerDto;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    public void login(ManagerDto managerDto) {
        System.out.println("Username: " + managerDto.getUsername());
        System.out.println("Password: " + managerDto.getPassword());
        // 이후 로그인 처리 코드 추가
    }

}
