package com.dflow.admin.system.service;

import com.dflow.admin.system.requestDto.LogInOutSearch;
import com.dflow.admin.system.responseDto.LogCodeResp;
import com.dflow.admin.system.responseDto.MemberLoginHistoryResp;
import com.dflow.entity.LoginOutHistory;
import com.dflow.project.responseDto.RespProjCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberLogService {

//    List<MemberLoginHistoryResp> selAllMemberLogList();

    Page<MemberLoginHistoryResp> selLogList(LogInOutSearch search, Pageable pageable);

    List<LogCodeResp> selLogCodeList(String codeType);

}
