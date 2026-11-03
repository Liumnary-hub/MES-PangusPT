package com.mes.entity;// src/main/java/com/pangu/manufacturing/entity/LoginLog.java


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_login_log")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime = LocalDateTime.now(); // 设置默认值

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "status", nullable = false)
    private Boolean status; // true: 成功, false: 失败

    @Column(name = "failure_reason", length = 200)
    private String failureReason;

    // 构造函数
    public LoginLog() {
        this.loginTime = LocalDateTime.now(); // 确保构造函数中也设置默认值
    }

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}