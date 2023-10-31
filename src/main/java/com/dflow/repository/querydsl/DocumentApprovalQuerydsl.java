package com.dflow.repository.querydsl;

import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.dashboard.responseDto.RespMainAprv;
import com.dflow.entity.DocumentApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface DocumentApprovalQuerydsl {
    Long selMainApprovalCounts(String memberId, String docState);
    Page<DocumentApproval> selDocumentApprovalsByConditions(Pageable pageable, DocAprvSearch search, Principal principal);

    Page<DocumentApproval> selAllDocumentApprovals(Pageable pageable, DocAprvSearch search, Principal principal);

    Page<DocumentApproval> selTempDocumentApproval(Pageable pageable, Long memberNo);

}
