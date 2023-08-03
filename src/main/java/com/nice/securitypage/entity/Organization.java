package com.nice.securitypage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    // 부서 테이블
    // 계층형으로 생성하기 위해 p_id사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "p_id")
    private int pId;

    @Column(name = "name")
    private String name;
}
