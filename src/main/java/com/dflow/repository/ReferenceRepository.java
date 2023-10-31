package com.dflow.repository;

import com.dflow.entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    //메인화면용 참조문서 수
    @Query("SELECT COUNT (r.refNo) FROM Reference r " +
            "WHERE r.member.memberId = :memberId " +
            "AND r.document.docState != '회수' " +
            "AND r.document.docFlag = 'Y' " +
            "AND r.updateDate >= :thirtyDaysAgo")
    Long countReferencesByMember(@Param("memberId") String memberId,
                                 @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    @Query("SELECT r FROM Reference r WHERE r.docNo = :docNo")
    List<Reference> findReferenceByDocNo(@Param("docNo") Long docNo);
}
