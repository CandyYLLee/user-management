package com.example.user_management.service;

import com.example.user_management.dto.UserActionLogQueryDTO;
import com.example.user_management.model.Account;
import com.example.user_management.model.User;
import com.example.user_management.model.UserAction;
import com.example.user_management.model.UserActionLog;
import org.springframework.data.domain.Page;

public interface UserActionLogService {
    void logAction(Account operator,
                    User user,
                    UserAction action);
    
    Page<UserActionLog> queryUserActionLogs(UserActionLogQueryDTO queryDTO);
}