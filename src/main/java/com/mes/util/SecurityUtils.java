package com.mes.util;

import com.mes.entity.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类 - 适配你的 User 实体
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : "system";
    }

    /**
     * 获取当前登录用户信息
     */
    public static User getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return getSystemUser();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            // 如果 Spring Security 使用的是 UserDetails，需要转换为你的 User 实体
            return convertToUserEntity((org.springframework.security.core.userdetails.UserDetails) principal);
        } else if (principal instanceof String) {
            // 如果是用户名字符串，创建一个简化用户
            return createUserFromUsername((String) principal);
        }

        return getSystemUser();
    }

    /**
     * 获取当前登录用户的简化信息（用于兼容之前的代码）
     */
    public static UserInfo getUser() {
        User user = getCurrentUser();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setEmail(user.getEmail());
        userInfo.setDepartment(user.getDepartment());
        userInfo.setEmployeeId(user.getEmployeeId());
        return userInfo;
    }

    /**
     * 检查当前用户是否有指定角色
     */
    public static boolean hasRole(String roleName) {
        User user = getCurrentUser();
        return user != null && user.hasRole(roleName);
    }

    /**
     * 检查当前用户是否有任意指定角色
     */
    public static boolean hasAnyRole(String... roleNames) {
        User user = getCurrentUser();
        if (user == null) return false;

        for (String roleName : roleNames) {
            if (user.hasRole(roleName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前用户的所有角色
     */
    public static String getAllRoles() {
        User user = getCurrentUser();
        return user != null ? user.getAllRoles() : "USER";
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 从 UserDetails 转换为 User 实体（需要根据你的实际情况实现）
     */
    private static User convertToUserEntity(org.springframework.security.core.userdetails.UserDetails userDetails) {
        // 这里需要根据你的用户服务实现从用户名加载用户实体
        // 暂时返回一个简化版本
        User user = new User();
        user.setId(1L); // 需要从数据库查询获取真实ID
        user.setUsername(userDetails.getUsername());
        user.setRealName(userDetails.getUsername()); // 简化处理
        return user;
    }

    /**
     * 从用户名创建用户实体（简化版本）
     */
    private static User createUserFromUsername(String username) {
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setRealName(username);
        return user;
    }

    /**
     * 获取系统用户（当无法获取当前用户时使用）
     */
    private static User getSystemUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("system");
        user.setRealName("系统用户");
        return user;
    }

    /**
     * 用户信息简化对象（用于兼容性）
     */
    @Data
    public static class UserInfo {
        private Long userId;
        private String username;
        private String nickName;
        private String realName;
        private String email;
        private String department;
        private String employeeId;

        // 为了兼容之前的代码
        public String getNickName() {
            return nickName != null ? nickName : realName;
        }
    }
}