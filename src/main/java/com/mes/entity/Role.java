package com.mes.entity;// src/main/java/com/pangu/manufacturing/entity/Role.java


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "role_code", unique = true, nullable = false, length = 50)
    private String roleCode;

    @Column(length = 200)
    private String description;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    // 无参构造函数（必需）
    public Role() {
        this.createdTime = LocalDateTime.now();
    }

    // 有参构造函数
    public Role(String name, String roleCode) {
        this();
        this.name = name;
        this.roleCode = roleCode;
    }

    // 全参构造函数
    public Role(String name, String roleCode, String description) {
        this();
        this.name = name;
        this.roleCode = roleCode;
        this.description = description;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}