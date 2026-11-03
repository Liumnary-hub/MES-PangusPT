package com.mes.service;

import com.mes.entity.User;
import com.mes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateFailedAttempts(String username, Integer failedAttempts) {
        userRepository.updateFailedAttempts(username, failedAttempts);
    }

    public void lockUser(String username) {
        userRepository.lockUser(username);
    }

    public void unlockUser(String username) {
        userRepository.unlockUser(username);
    }

    public void updateLastLoginTime(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetTime(LocalDateTime.now());
        userRepository.save(user);
    }

}
