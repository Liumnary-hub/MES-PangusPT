package com.mes.service;// src/main/java/com/pangu/manufacturing/service/AuthService.java


import com.mes.dto.JwtResponse;
import com.mes.dto.LoginRequest;
import com.mes.entity.LoginLog;
import com.mes.entity.User;
import com.mes.repository.LoginLogRepository;
import com.mes.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.security.max-failed-attempts:5}")
    private Integer maxFailedAttempts;

    @Value("${app.security.lock-time-minutes:30}")
    private Integer lockTimeMinutes;

    public JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 记录登录尝试
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setIpAddress(getClientIpAddress(request));
        loginLog.setUserAgent(request.getHeader("User-Agent"));
        loginLog.setLoginTime(LocalDateTime.now()); // 确保设置时间
        loginLog.setStatus(false); // 或 true
        try {
            // 1. 检查用户是否存在
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> {
                        loginLog.setStatus(false);
                        loginLog.setFailureReason("用户不存在");
                        loginLogRepository.save(loginLog);
                        return new RuntimeException("用户名或密码错误");
                    });

            // 2. 检查用户是否启用
            if (!user.getIsEnabled()) {
                loginLog.setStatus(false);
                loginLog.setFailureReason("用户账户已禁用");
                loginLogRepository.save(loginLog);
                throw new RuntimeException("用户账户已禁用，请联系管理员");
            }

            // 3. 检查用户是否被锁定
            if (user.getIsLocked()) {
                // 检查是否超过锁定时间
                if (user.getLockTime() != null &&
                        user.getLockTime().plusMinutes(lockTimeMinutes).isAfter(LocalDateTime.now())) {
                    loginLog.setStatus(false);
                    loginLog.setFailureReason("用户账户已锁定");
                    loginLogRepository.save(loginLog);
                    throw new RuntimeException("用户账户已锁定，请稍后重试或联系管理员");
                } else {
                    // 自动解锁
                    userService.unlockUser(username);
                }
            }

            // 4. 验证密码
            if (!userService.validatePassword(password, user.getPassword())) {
                // 密码错误，增加失败次数
                int newFailedAttempts = user.getFailedAttempts() + 1;
                userService.updateFailedAttempts(username, newFailedAttempts);
                System.out.println(user.getPassword());

                // 如果超过最大尝试次数，锁定账户
                if (newFailedAttempts >= maxFailedAttempts) {
                    userService.lockUser(username);
                    loginLog.setStatus(false);
                    loginLog.setFailureReason("密码错误次数过多，账户已锁定");
                    loginLogRepository.save(loginLog);
                    throw new RuntimeException("密码错误次数过多，账户已锁定，请联系管理员");
                }

                loginLog.setStatus(false);
                loginLog.setFailureReason("密码错误");
                loginLogRepository.save(loginLog);
                throw new RuntimeException("用户名或密码错误");
            }

            // 5. 登录成功
            // 重置失败次数
            if (user.getFailedAttempts() > 0) {
                userService.updateFailedAttempts(username, 0);
            }

            // 更新最后登录时间
            userService.updateLastLoginTime(username);

            // 生成JWT令牌
            String jwt = jwtUtils.generateJwtToken(user);

            // 记录成功登录
            loginLog.setStatus(true);
            loginLogRepository.save(loginLog);

            return new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getRealName(),
                    user.getDepartment()
            );

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            loginLog.setStatus(false);
            loginLog.setFailureReason("系统错误: " + e.getMessage());
            loginLogRepository.save(loginLog);
            throw new RuntimeException("登录过程中发生系统错误");
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}