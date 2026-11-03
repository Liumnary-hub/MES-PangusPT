package com.mes.controller;

import com.mes.dto.JwtResponse;
import com.mes.dto.LoginRequest;
import com.mes.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins= "*",maxAge=3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {

        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest, request);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        // 返回管理员联系信息
        AdminContactInfo contactInfo = new AdminContactInfo();
        contactInfo.setMessage("请联系系统管理员进行密码重置");
        contactInfo.setPhone("010-12345678");
        contactInfo.setEmail("admin@pangu-manufacturing.com");
        contactInfo.setExtension("8001");

        return ResponseEntity.ok(contactInfo);
    }

    // 内部类用于响应
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class AdminContactInfo {
        private String message;
        private String phone;
        private String email;
        private String extension;

        // Getter和Setter
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getExtension() { return extension; }
        public void setExtension(String extension) { this.extension = extension; }
    }
}
