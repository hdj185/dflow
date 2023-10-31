package com.dflow.repository;

import com.dflow.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    //
    @Query("SELECT a FROM Approval a WHERE a.docNo = :docNo ORDER BY a.aprvOrder")
    List<Approval> findApprovalsByDocumentNo(@Param("docNo") Long docNo);

    @Query("SELECT a FROM Approval a WHERE a.docNo = :docNo AND a.aprvOrder = :aprvOrder")
    Optional<Approval> findApprovalByDocumentNo(@Param("docNo") Long docNo, @Param("aprvOrder") int aprvOrder);
}
