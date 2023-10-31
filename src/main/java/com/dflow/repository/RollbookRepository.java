package com.dflow.repository;

import com.dflow.entity.QRollbook;
import com.dflow.entity.Rollbook;
import com.dflow.repository.querydsl.DocumentApprovalQuerydsl;
import com.dflow.repository.querydsl.RollbookQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RollbookRepository extends JpaRepository<Rollbook, Long>, RollbookQuerydsl {

    //memberId의 근태리스트 모두 불러오기
    @Query("SELECT r FROM Rollbook r " +
            "WHERE r.member.memberId = :memberId " +
            "AND r.rollbookFlag = :rollbookFlag " +
            "ORDER BY r.rollbookNo DESC")
    List<Rollbook> findAllByMemberId(@Param("memberId") String memberId,
                                     @Param("rollbookFlag") String rollbookFlag);

    // 특정 하루의 근태리스트 중 null인 모두 불러오기
    @Query("SELECT r FROM Rollbook r " +
            "WHERE r.rollbookOpenTime >= :yesterday " +
            "AND r.rollbookOpenTime < :today " +
            "AND r.rollbookCloseTime IS NULL")
    List<Rollbook> findAllByDay(@Param("today") LocalDateTime today,
                                @Param("yesterday") LocalDateTime yesterday);

}
