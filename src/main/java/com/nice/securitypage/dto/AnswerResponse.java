package com.nice.securitypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 관리자 페이지에 내려줄 Response
public class AnswerResponse {
    private Long id;
    private String content;
    private Boolean isRequired;
    private String emailName;
    private String response; // 암호화된 url 혹은 예 아니오 혹은 input 값
    private String decryptedResponse; // 복호화 url (사진주소를을 담아놓을 공간)
}
