package com.dflow.repository;

import com.dflow.entity.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
    //요청 불러오기
    @Query("SELECT r FROM AdminRequest r WHERE r.requestFlag = 'N' " +
            "AND r.requestType = :requestType AND r.rollbook.rollbookFlag = 'Y'")
    List<AdminRequest> findAdminRequestByConditions(@Param("requestType") String requestType);

    //특정 정정 요청 불러오기
    @Query("SELECT r FROM AdminRequest r WHERE r.requestFlag = 'N' AND r.rollbookNo = :rollbookNo")
    List<AdminRequest> findAdminRequestByRollbookNo(@Param("rollbookNo") Long rollbookNo);
}
