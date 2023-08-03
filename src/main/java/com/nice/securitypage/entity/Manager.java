package com.nice.securitypage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // index, PK
    private Long id;
    // 유저 아이디
    private String username;
    // 비밀번호
    private String password;

    // 객체가 생성될때의 시간으로 설정
    @CreatedDate
    private LocalDateTime createdDate;

    // 잠금 여부, default는 False
    @Column
    private boolean isLocked = false;

    public void lock() {
        this.isLocked = true;
    }
}
