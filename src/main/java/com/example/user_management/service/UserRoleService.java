package com.example.user_management.service;

import com.example.user_management.model.UserRole;
import java.util.List;

public interface UserRoleService {
    List<UserRole> getAllUserRoles();
    List<UserRole> getUserRolesByUserId(Long userId);
    List<UserRole> getUserRolesByRoleId(Long roleId);
    UserRole assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
    boolean isUserHasRole(Long userId, Long roleId);
}