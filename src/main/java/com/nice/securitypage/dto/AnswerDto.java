package com.nice.securitypage.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

    private String response;  // 사용자의 답변 (예: true, 아니오: false 혹은 String)
}
