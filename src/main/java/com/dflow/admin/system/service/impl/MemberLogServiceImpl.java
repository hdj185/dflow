package com.dflow.admin.system.service.impl;

import com.dflow.admin.system.requestDto.LogInOutSearch;
import com.dflow.admin.system.responseDto.LogCodeResp;
import com.dflow.admin.system.responseDto.MemberLoginHistoryResp;
import com.dflow.admin.system.service.MemberLogService;
import com.dflow.entity.CodeInfo;
import com.dflow.entity.LoginOutHistory;
import com.dflow.project.responseDto.RespProjCode;
import com.dflow.repository.CodeInfoRepository;
import com.dflow.repository.LoginOutHistoryRepository;
import com.dflow.rollbook.responseDto.AnnualLeaveRecordResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberLogServiceImpl implements MemberLogService {

    private final LoginOutHistoryRepository loginOutHistoryRepository;
    private final CodeInfoRepository codeInfoRepository;

//    @Override
//    public List<MemberLoginHistoryResp> selAllMemberLogList() {
//        List<LoginOutHistory> logList = loginOutHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
//        return MemberLoginHistoryResp.of(logList);
//    }

    // 로그 기록 페이징
    @Override
    public Page<MemberLoginHistoryResp> selLogList(LogInOutSearch search, Pageable pageable) {
        Page<LoginOutHistory> entityPage = loginOutHistoryRepository.selLogByConditions(pageable, search);

        return MemberLoginHistoryResp.of(entityPage);
    }

    // 로그 타입 리스트 불러오기
    @Override
    public List<LogCodeResp> selLogCodeList(String codeType) {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse(codeType);
        return LogCodeResp.of(codeInfoList);
    }
}
