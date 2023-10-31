package com.dflow.repository;

import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import com.dflow.repository.querydsl.ProjectInfoQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectResourceRepository extends JpaRepository<ProjectResource, Long>, ProjectInfoQuerydsl {

    //메인화면 - 프로젝트 출력
    @Query("SELECT r FROM ProjectResource r " +
            "WHERE r.member.memberId = :memberId " +
            "AND r.project.projectStartDate <= current_date " +
            "AND r.project.projectEndDate >= current_date " +
            "ORDER BY r.project.projectEndDate")
    List<ProjectResource> findProjectInfosByConditionsInMain(@Param("memberId") String memberId);

    //투입인력 삭제
    @Transactional
    @Modifying
    @Query("delete from ProjectResource r where r.resourceNo in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
