package com.dflow.repository;

import com.dflow.entity.ProjectInfo;
import com.dflow.repository.querydsl.ProjectInfoQuerydsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long>, ProjectInfoQuerydsl {
    //투입공수 등록용 ProjectInfo 목록 뽑기(투입공수 등록되지 않은 것)
    @Query("SELECT p FROM ProjectInfo p WHERE p.projectIsDeleted = false AND p.resources.size = 0")
    List<ProjectInfo> findProjectInfoWithEmptyResources();
}
