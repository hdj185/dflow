package com.dflow.repository;

import com.dflow.entity.LoginOutHistory;
import com.dflow.repository.querydsl.SystemQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Principal;
import java.util.List;

public interface LoginOutHistoryRepository extends JpaRepository<LoginOutHistory, Long>, SystemQuerydsl {

    List<LoginOutHistory> findAll();

}
