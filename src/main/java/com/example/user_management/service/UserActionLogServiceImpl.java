package com.example.user_management.service;

import com.example.user_management.dto.UserActionLogQueryDTO;
import com.example.user_management.model.Account;
import com.example.user_management.model.User;
import com.example.user_management.model.UserAction;
import com.example.user_management.model.UserActionLog;
import com.example.user_management.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserActionLogServiceImpl implements UserActionLogService {

    private final UserActionLogRepository userActionLogRepository;

    @Autowired
    public UserActionLogServiceImpl(UserActionLogRepository userActionLogRepository) {
        this.userActionLogRepository = userActionLogRepository;
    }

    @Override
    public void logAction(Account operator, User user, UserAction action) {
        UserActionLog log = new UserActionLog();
        log.setOperatorId(operator.getId());
        log.setUserId(user.getId());
        log.setAction(action);
        // createdAt 会通过 @PrePersist 注解自动设置
        userActionLogRepository.save(log);
    }

    @Override
    public Page<UserActionLog> queryUserActionLogs(UserActionLogQueryDTO query) {
        Specification<UserActionLog> spec = (root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), query.getUserId()));
            }

            if (query.getOperatorId() != null) {
                predicates.add(cb.equal(root.get("operatorId"), query.getOperatorId()));
            }

            if (query.getAction() != null) {
                predicates.add(cb.equal(root.get("action"), query.getAction()));
            }

            if (query.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("createdAt"), query.getStartTime()));
            }

            if (query.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("createdAt"), query.getEndTime()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return userActionLogRepository.findAll(spec, pageable);
    }
}