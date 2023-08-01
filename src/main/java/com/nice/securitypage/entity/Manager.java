package com.nice.securitypage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(columnDefinition = "boolean default false")
    private boolean isLocked;

    public void lock() {
        this.isLocked = true;
    }
}
