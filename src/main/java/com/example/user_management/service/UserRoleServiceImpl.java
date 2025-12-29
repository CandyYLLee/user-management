package com.example.user_management.service;

import com.example.user_management.model.UserRole;
import com.example.user_management.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    @Override
    public List<UserRole> getUserRolesByRoleId(Long roleId) {
        return userRoleRepository.findByRoleId(roleId);
    }

    @Override
    public UserRole assignRoleToUser(Long userId, Long roleId) {
        if (!userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setAssignedAt(LocalDateTime.now());
            return userRoleRepository.save(userRole);
        }
        return null;
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public boolean isUserHasRole(Long userId, Long roleId) {
        return userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
    }
}