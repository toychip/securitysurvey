package com.nice.securitypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormDto {

    // 이름
    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    // 이메일(사내)
    @NotBlank(message = "이메일을 입력하세요.")
    private String emailname;
    // 휴대폰 번호를 List에 저장
    @NotBlank(message = "휴대폰 번호를 입력하세요.")
    List<Integer> phoneNumber = new ArrayList<>();
    // 사내 번호를 List에 저장
    @NotBlank(message = "사내번호를 입력하세요.")
    List<Integer> extensionNumber = new ArrayList<>();
    // 사용자 ip
    private String ip;
    // 브라우저 정보
    private String browser;
}
