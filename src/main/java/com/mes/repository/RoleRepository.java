package com.mes.repository;
// src/main/java/com/pangu/manufacturing/repository/RoleRepository.java



import com.mes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByRoleCode(String roleCode);
    Boolean existsByName(String name);
}
