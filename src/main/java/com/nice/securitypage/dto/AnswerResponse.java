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
    private String response;
    private String decryptedResponse;
}
