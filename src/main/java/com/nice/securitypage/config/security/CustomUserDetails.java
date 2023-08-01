package com.nice.securitypage.config.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserDetails {
    private Boolean passwordExpired;

    public CustomUserDetails(Boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }
}
