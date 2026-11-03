package com.mes.dto;

// dto/LoginRequest.java


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3到50个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6到100个字符之间")
    private String password;

    private Boolean rememberUser = false;
    private Boolean rememberPassword = false;

    // 构造函数
    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter和Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getRememberUser() { return rememberUser; }
    public void setRememberUser(Boolean rememberUser) { this.rememberUser = rememberUser; }

    public Boolean getRememberPassword() { return rememberPassword; }
    public void setRememberPassword(Boolean rememberPassword) { this.rememberPassword = rememberPassword; }
}
