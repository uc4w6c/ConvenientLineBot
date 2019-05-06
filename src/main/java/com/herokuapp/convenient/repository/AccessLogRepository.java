package com.herokuapp.convenient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.AccessLog;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {
}
