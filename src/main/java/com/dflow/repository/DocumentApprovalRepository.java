package com.dflow.repository;

import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.entity.DocumentApproval;
import com.dflow.repository.querydsl.DocumentApprovalQuerydsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentApprovalRepository extends JpaRepository<DocumentApproval, Long>, DocumentApprovalQuerydsl {


    List<DocumentApproval> findAllByDocFlag(String docFlag);

    Page<DocumentApproval> findByDocTTLContaining(String keyword, Pageable pageable);



}


