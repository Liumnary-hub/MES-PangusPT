package com.mes.config;// src/main/java/com/pangu/manufacturing/config/DataInitializer.java



import com.mes.entity.Role;
import com.mes.entity.User;
import com.mes.repository.RoleRepository;
import com.mes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始初始化数据...");

        // 初始化角色
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("系统管理员", "ROLE_ADMIN");
            Role userRole = new Role("普通用户", "ROLE_USER");
            Role operatorRole = new Role("操作员", "ROLE_OPERATOR");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(operatorRole);

            System.out.println("角色初始化完成");
        }

        // 初始化管理员用户
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRealName("系统管理员");
            adminUser.setEmail("admin@pangu-manufacturing.com");
            adminUser.setDepartment("信息技术部");
            adminUser.setEmployeeId("PG001");
            adminUser.setIsEnabled(true);
            adminUser.setIsLocked(false);

            Role adminRole = roleRepository.findByName("系统管理员")
                    .orElseThrow(() -> new RuntimeException("管理员角色未找到"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);

            userRepository.save(adminUser);
            System.out.println("默认管理员用户已创建 - 用户名: admin, 密码: admin123");
        }

        System.out.println("数据初始化完成");
    }
}