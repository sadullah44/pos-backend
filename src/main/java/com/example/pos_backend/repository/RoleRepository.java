package com.example.pos_backend.repository;

import com.example.pos_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Bu import'u ekleyin

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 'Role' değil, 'Optional<Role>' döndürmeli
    Optional<Role> findByRoleName(String roleName);
}