package com.nice.securitypage.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}",
            message = "비밀번호는 영문자 8~16자리에 대소문자, 특수문자가 최소 1개 이상 포함되어야 합니다.")
    private String newPassword;
}
