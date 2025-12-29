package com.example.user_management.service;

import com.example.user_management.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    Optional<Role> getRoleByName(String name);
    boolean existsByName(String name);
}