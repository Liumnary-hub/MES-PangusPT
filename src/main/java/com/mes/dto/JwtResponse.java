package com.mes.dto;

// dto/JwtResponse.java
import java.time.LocalDateTime;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String realName;
    private String department;
    private LocalDateTime loginTime;

    public JwtResponse(String token, Long id, String username, String realName, String department) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.department = department;
        this.loginTime = LocalDateTime.now();
    }

    // Getter和Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
}
