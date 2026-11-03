package com.mes.repository;

import com.mes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = :failedAttempts WHERE u.username = :username")
    void updateFailedAttempts(@Param("username") String username, @Param("failedAttempts") Integer failedAttempts);

    @Modifying
    @Query("UPDATE User u SET u.isLocked = true, u.lockTime = CURRENT_TIMESTAMP WHERE u.username = :username")
    void lockUser(@Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.isLocked = false, u.failedAttempts = 0, u.lockTime = null WHERE u.username = :username")
    void unlockUser(@Param("username") String username);
}
