package com.mes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String department;
    private String employeeId;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdTime;
}
