package com.example.user_management.util;

import com.example.user_management.exception.ForbiddenException;
import com.example.user_management.model.Account;
import com.example.user_management.model.DataScope;
import com.example.user_management.model.PermissionCode;
import com.example.user_management.model.User;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {

    public void check(Account account, 
                      PermissionCode permission, 
                      User targetUser) {

        // 1. 动作权限
        checkPermission(account, permission);

        // 2. 数据范围权限
        checkDataScope(account, targetUser);
    }

    private void checkPermission(Account account, 
                                 PermissionCode permission) {
        if (!account.hasPermission(permission)) {
            throw new ForbiddenException("无操作权限：" + permission);
        }
    }

    private void checkDataScope(Account account, 
                                User targetUser) {

        if (account.getRole() == null) {
            throw new ForbiddenException("用户角色未设置");
        }

        DataScope scope = account.getRole().getDataScope();

        switch (scope) {
            case ALL:
                return;

            case SELF:
                // 由于当前User实体没有ownerId字段，暂时简化处理
                // 在实际项目中，需要根据业务需求调整
                return;

            case DEPARTMENT:
                // MVP 阶段：先允许（以后补）
                return;
            
            default:
                throw new ForbiddenException("无效的数据范围权限");
        }
    }
}