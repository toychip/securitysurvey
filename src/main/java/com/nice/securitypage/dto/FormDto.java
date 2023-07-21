package com.nice.securitypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormDto {

    // 이름
    @NotBlank
    private String name;
    // 이메일(사내)
    @NotBlank
    private String emailname;
    // 휴대폰 번호
    @NotBlank
    private String phoneNumber1;
    @NotBlank
    private String phoneNumber2;
    @NotBlank
    private String phoneNumber3;
    // 사내 번호
    @NotBlank
    private String extensionNumber1;
    @NotBlank
    private String extensionNumber2;
    @NotBlank
    private String extensionNumber3;
    // 사용자 ip
    private String ip;
    // 브라우저 정보
    private String browser;

    public String getEmailname() {
        return this.emailname + "@nicednr.co.kr";
    }

}
