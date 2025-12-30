package com.example.user_management.controller;

import com.example.user_management.dto.AssignUserRequest;
import com.example.user_management.dto.UpdateUserRequest;
import com.example.user_management.dto.UserQuery;
import com.example.user_management.model.Account;
import com.example.user_management.model.User;
import com.example.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ===================== 查询 ===================== */

    @GetMapping
    public Page<User> list(@AuthenticationPrincipal Account account,
                           UserQuery query,
                           @org.springframework.data.web.PageableDefault Pageable pageable) {
        query.setPageable(pageable);
        return userService.queryUsers(account, query);
    }

    @GetMapping("/{id}")
    public User detail(@AuthenticationPrincipal Account account,
                      @PathVariable Long id) {
        return userService.getUserDetail(account, id);
    }

    /* ===================== 编辑 ===================== */

    @PutMapping("/{id}")
    public void edit(@AuthenticationPrincipal Account account,
                     @PathVariable Long id,
                     @RequestBody UpdateUserRequest request) {
        userService.editUser(account, id, request);
    }

    /* ===================== 分配负责人 ===================== */

    @PutMapping("/{id}/assign")
    public void assign(@AuthenticationPrincipal Account account,
                       @PathVariable Long id,
                       @RequestBody AssignUserRequest request) {
        userService.assignUser(account, id, request);
    }

    /* ===================== 状态操作 ===================== */

    @PutMapping("/{id}/disable")
    public void disable(@AuthenticationPrincipal Account account,
                        @PathVariable Long id) {
        userService.disableUser(account, id);
    }

    @PutMapping("/{id}/enable")
    public void enable(@AuthenticationPrincipal Account account,
                       @PathVariable Long id) {
        userService.enableUser(account, id);
    }

    @PutMapping("/{id}/archive")
    public void archive(@AuthenticationPrincipal Account account,
                        @PathVariable Long id) {
        userService.archiveUser(account, id);
    }

    @PutMapping("/{id}/unarchive")
    public void unarchive(@AuthenticationPrincipal Account account,
                          @PathVariable Long id) {
        userService.unarchiveUser(account, id);
    }
}