package com.nice.securitypage.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class AnswerDto {
    private String response;  // 사용자의 답변 (예: true, 아니오: false 혹은 String)
    private MultipartFile file;  // 사용자가 업로드한 파일
}
