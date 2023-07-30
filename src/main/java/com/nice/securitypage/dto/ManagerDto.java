package com.nice.securitypage.dto;

import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ManagerDto {
    private String username;
    private String password;
}
