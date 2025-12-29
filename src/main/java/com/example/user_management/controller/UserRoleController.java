package com.example.user_management.controller;

import com.example.user_management.model.UserRole;
import com.example.user_management.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public ResponseEntity<List<UserRole>> getAllUserRoles() {
        List<UserRole> userRoles = userRoleService.getAllUserRoles();
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRole>> getUserRolesByUserId(@PathVariable Long userId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userId);
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserRole>> getUserRolesByRoleId(@PathVariable Long roleId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByRoleId(roleId);
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserRole> assignRoleToUser(@RequestBody UserRole userRole) {
        UserRole assignedUserRole = userRoleService.assignRoleToUser(userRole.getUserId(), userRole.getRoleId());
        if (assignedUserRole != null) {
            return new ResponseEntity<>(assignedUserRole, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<Boolean> isUserHasRole(@PathVariable Long userId, @PathVariable Long roleId) {
        boolean hasRole = userRoleService.isUserHasRole(userId, roleId);
        return new ResponseEntity<>(hasRole, HttpStatus.OK);
    }
}