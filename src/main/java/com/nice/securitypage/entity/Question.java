package com.nice.securitypage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 질문 번호가 바뀌지 않는다고 가정 PK로 잡음
    private Long id;

    // 질문 내용
    private String content;

    // 필수 여부
    private Boolean isRequired;

    // 질문 타입
    @Enumerated(EnumType.STRING)
    private ResponseType type;
}
