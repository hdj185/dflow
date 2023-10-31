package com.dflow.repository.querydsl;

import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.entity.AnnualLeave;
import com.dflow.entity.LeaveRecord;
import com.dflow.entity.Rollbook;
import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.rollbook.requestDto.AnnualSearch;
import com.dflow.rollbook.requestDto.RollbookSearch;
import com.dflow.rollbook.responseDto.RollbookPageResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface RollbookQuerydsl {

    Page<Rollbook> selRollbookPaging(Pageable pageable, RollbookSearch search, Principal principal);

    Page<Rollbook> selAdminRollbookPaging(Pageable pageable, RollbookSearch search);

    Page<LeaveRecord> selAnnualByConditions(Pageable pageable, AnnualSearch search, Principal principal);

}
