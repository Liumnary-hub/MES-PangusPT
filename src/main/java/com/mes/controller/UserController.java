package com.mes.controller;
// UserController.java (确保包路径正确)

import com.mes.entity.User; // 注意包路径
import com.mes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> userOptional = userService.findByUsername(username);

            if (userOptional.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            User user = userOptional.get();

            // 返回用户信息（不包含密码等敏感信息）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("realName", user.getRealName());
            userInfo.put("department", user.getDepartment());
            userInfo.put("employeeId", user.getEmployeeId());

            // 使用我们新创建的方法
            userInfo.put("role", user.getRole()); // 主要角色
            userInfo.put("allRoles", user.getAllRoles()); // 所有角色
            userInfo.put("createdAt", user.getCreatedAt()); // 创建时间

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
