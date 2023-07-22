package com.nice.securitypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class FormDto {

    // 이름
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    // 이메일(사내)
    @NotBlank(message = "이메일은 필수입니다.")
    private String emailname;
    // 휴대폰 번호
    @NotBlank(message = "전화번호 앞 자리는 필수입니다.")
    private String phoneNumber1;
    @NotBlank(message = "전화번호 중간 자리는 필수입니다.")
    private String phoneNumber2;
    @NotBlank(message = "전화번호 뒷 자리는 필수입니다.")
    private String phoneNumber3;
    // 사내 번호
    @NotBlank(message = "사내번호 앞 자리는 필수입니다.")
    private String extensionNumber1;
    @NotBlank(message = "사내번호 중간 자리는 필수입니다.")
    private String extensionNumber2;
    @NotBlank(message = "사내번호 뒷 자리는 필수입니다.")
    private String extensionNumber3;
    // 사용자 ip
    private String ip;
    // 브라우저 정보
    private String browser;

}
