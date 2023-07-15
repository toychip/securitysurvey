package com.nice.securitypage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
    private Long id;
    // 이름
    private String name;
    // 이메일(사내)
    private String emailname;
    // 휴대폰 번호
    private String phoneNumber;
    // 사내 번호
    private String extensionNumber;
    // 사용자 ip
    private String ip;
    // 브라우저 정보
    private String browser;
}
