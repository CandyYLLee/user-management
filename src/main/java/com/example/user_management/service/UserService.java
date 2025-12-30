package com.example.user_management.service;

import com.example.user_management.dto.AssignUserRequest;
import com.example.user_management.dto.UpdateUserRequest;
import com.example.user_management.dto.UserQuery;
import com.example.user_management.model.Account;
import com.example.user_management.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    /* ===================== 查询 ===================== */
    Page<User> queryUsers(Account account, UserQuery query);
    User getUserDetail(Account account, Long userId);
    
    /* ===================== 编辑 ===================== */
    void editUser(Account account, Long userId, UpdateUserRequest request);
    
    /* ===================== 负责人 ===================== */
    void assignUser(Account account, Long userId, AssignUserRequest request);
    
    /* ===================== 状态操作 ===================== */
    void disableUser(Account account, Long userId);
    void enableUser(Account account, Long userId);
    void archiveUser(Account account, Long userId);
    void unarchiveUser(Account account, Long userId);
}