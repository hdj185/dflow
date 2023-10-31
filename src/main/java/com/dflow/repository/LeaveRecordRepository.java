package com.dflow.repository;

import com.dflow.entity.LeaveRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long> {
    //연차기록 페이지 불러오기
    @Query("SELECT r FROM LeaveRecord r " +
            "WHERE r.member.memberId = :memberId " +
            "ORDER BY r.leaveNo DESC")
    Page<LeaveRecord> findLeaveRecordsByConditions(@Param("memberId") String memberId,
                                          Pageable pageable);
}
