package com.mes.entity;// entity/User.java


import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "real_name", length = 100)
    private String realName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "employee_id", length = 50)
    private String employeeId;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "failed_attempts")
    private Integer failedAttempts = 0;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "password_reset_time")
    private LocalDateTime passwordResetTime;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // 构造函数
    public User() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }

    public Integer getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(Integer failedAttempts) { this.failedAttempts = failedAttempts; }

    public LocalDateTime getLockTime() { return lockTime; }
    public void setLockTime(LocalDateTime lockTime) { this.lockTime = lockTime; }

    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }

    public LocalDateTime getPasswordResetTime() { return passwordResetTime; }
    public void setPasswordResetTime(LocalDateTime passwordResetTime) { this.passwordResetTime = passwordResetTime; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

    public String getRole() {
        if (roles == null || roles.isEmpty()) {
            return "USER"; // 默认角色
        }
        // 返回第一个角色的名称，或者你可以根据业务逻辑返回合适的角色
        return roles.iterator().next().getName(); // 假设 Role 实体有 getName() 方法
    }

    public String getAllRoles() {
        if (roles == null || roles.isEmpty()) {
            return "USER";
        }
        StringBuilder roleNames = new StringBuilder();
        for (Role role : roles) {
            if (roleNames.length() > 0) {
                roleNames.append(", ");
            }
            roleNames.append(role.getName());
        }
        return roleNames.toString();
    }

    /**
     * 检查用户是否拥有特定角色
     */
    public boolean hasRole(String roleName) {
        if (roles == null) {
            return false;
        }
        return roles.stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    /**
     * 为了兼容性，添加 createdAt 方法（返回 createdTime）
     */
    public LocalDateTime getCreatedAt() {
        return this.createdTime;
    }






}