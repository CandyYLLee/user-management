package com.example.user_management.controller;

import com.example.user_management.dto.UserActionLogQueryDTO;
import com.example.user_management.model.UserActionLog;
import com.example.user_management.service.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-logs")
public class UserActionLogController {

    private final UserActionLogService userActionLogService;

    @Autowired
    public UserActionLogController(UserActionLogService userActionLogService) {
        this.userActionLogService = userActionLogService;
    }

    @GetMapping
    public Page<UserActionLog> queryUserLogs(UserActionLogQueryDTO queryDTO,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        queryDTO.setPageable(pageable);
        return userActionLogService.queryUserActionLogs(queryDTO);
    }
}