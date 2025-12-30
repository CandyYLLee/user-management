package com.example.user_management.service;

import com.example.user_management.dto.AssignUserRequest;
import com.example.user_management.dto.UpdateUserRequest;
import com.example.user_management.dto.UserQuery;
import com.example.user_management.exception.NotFoundException;
import com.example.user_management.model.Account;
import com.example.user_management.model.DataScope;
import com.example.user_management.model.PermissionCode;
import com.example.user_management.model.User;
import com.example.user_management.model.UserAction;
import com.example.user_management.repository.UserRepository;
import com.example.user_management.util.PermissionChecker;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionChecker permissionChecker;
    private final UserActionLogService userActionLogService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PermissionChecker permissionChecker, UserActionLogService userActionLogService) {
        this.userRepository = userRepository;
        this.permissionChecker = permissionChecker;
        this.userActionLogService = userActionLogService;
    }

    /* ===================== 查询 ===================== */

    @Override
    public Page<User> queryUsers(Account account, UserQuery query) {
        // 数据范围控制（查询层）
        if (account.getRole() != null && account.getRole().getDataScope() == DataScope.SELF) {
            query.setOwnerId(account.getId());
        }

        // 构建动态查询条件
        Specification<User> spec = (root, queryBuilder, criteriaBuilder) -> {
            java.util.List<Predicate> predicates = new java.util.ArrayList<>();

            // 关键字搜索
            if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
                String keyword = "%" + query.getKeyword() + "%";
                Predicate namePredicate = criteriaBuilder.like(root.get("name"), keyword);
                Predicate emailPredicate = criteriaBuilder.like(root.get("email"), keyword);
                predicates.add(criteriaBuilder.or(namePredicate, emailPredicate));
            }

            // 角色筛选
            if (query.getRole() != null && !query.getRole().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("role"), query.getRole()));
            }

            // 激活状态筛选
            if (query.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), query.getActive()));
            }

            // 负责人筛选
            if (query.getOwnerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("ownerId"), query.getOwnerId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, query.getPageable());
    }

    @Override
    public User getUserDetail(Account account, Long userId) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_VIEW, user);
        return user;
    }

    /* ===================== 编辑 ===================== */

    @Override
    public void editUser(Account account, Long userId, UpdateUserRequest request) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_EDIT, user);

        user.update(request);
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.EDIT);
    }



    /* ===================== 负责人 ===================== */

    @Override
    public void assignUser(Account account, Long userId, AssignUserRequest request) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_ASSIGN, user);

        user.assignOwner(request.getOwnerId());
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.ASSIGN);
    }



    /* ===================== 状态操作 ===================== */

    @Override
    public void disableUser(Account account, Long userId) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_DISABLE, user);

        user.disable();
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.DISABLE);
    }

    @Override
    public void enableUser(Account account, Long userId) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_EDIT, user);

        user.enable();
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.DISABLE);
    }

    @Override
    public void archiveUser(Account account, Long userId) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_ARCHIVE, user);

        user.archive();
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.ARCHIVE);
    }

    @Override
    public void unarchiveUser(Account account, Long userId) {
        User user = findUser(userId);
        permissionChecker.check(account, PermissionCode.USER_EDIT, user);

        user.unarchive();
        userRepository.save(user);
        
        // 记录操作日志
        userActionLogService.logAction(account, user, UserAction.ARCHIVE);
    }

    /* ===================== 内部方法 ===================== */

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("用户不存在"));
    }
}