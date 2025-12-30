package com.example.user_management.controller;

import com.example.user_management.model.UserAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditController {

    @GetMapping("/audit")
    public String auditPage(Model model) {
        // 将操作类型枚举添加到模型中，用于下拉选择
        model.addAttribute("actionTypes", UserAction.values());
        return "audit";
    }
}