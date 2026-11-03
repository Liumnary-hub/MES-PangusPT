package com.mes.repository;

import com.mes.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
    long countByUsernameAndStatusAndLoginTimeAfter(
            String username, Boolean status, java.time.LocalDateTime since);
}
