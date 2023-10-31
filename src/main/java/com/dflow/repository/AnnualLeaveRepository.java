package com.dflow.repository;

import com.dflow.entity.AnnualLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long> {
    //직원의 연차 정보 불러오기
    //memberNo: 직원 고유번호
    @Query("SELECT a FROM AnnualLeave a " +
            "WHERE a.member.memberId = :memberId")
    Optional<AnnualLeave> findAnnualLeaveByMemberId(@Param("memberId") String memberId);

    //퇴사자가 아닌 연차 정보 모두 불러오기
    @Query("SELECT a FROM AnnualLeave a " +
            "WHERE a.member.memberFlag = '0'")
    List<AnnualLeave> findAnnualLeavesByMemberFlag();
}
