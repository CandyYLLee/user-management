package com.example.user_management.repository;

import com.example.user_management.model.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long>, JpaSpecificationExecutor<UserActionLog> {
}