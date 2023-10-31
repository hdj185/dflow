package com.dflow.rollbook.service;

import com.dflow.admin.approval.responseDto.AprvCodeResp;
import com.dflow.entity.Rollbook;
import com.dflow.rollbook.requestDto.*;
import com.dflow.rollbook.responseDto.*;
import net.bytebuddy.build.BuildLogger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface RollbookService {
    //연차 기준 정보 RespDto 형태로 받아오기
    AdminAnuSetResp selAdminAnuSetResp();
    //연차 기준 정보 수정 요청
    Boolean udtAdminAnnaulSetting(AdminAnuSetUdtReq request);

    List<AprvCodeResp> selRollbookCodeList();

    List<RespRollbookList> selRollbookList(String memberId);

    AnnualCountResp selAnnualCount(String memberId);

    // 연차 기록 불러오기
    Page<AnnualLeaveRecordResp> selLeaveRecordList(Principal principal, AnnualSearch search, Pageable pageable);

    // 연차 종류 구분 코드 리스트
    List<RespAnnualCode> selAnnualCodeList(String codeType);

    Boolean udtAnnualCount(UdtAnnualReq req, String memberId);

    Page<Rollbook> selRollbookEntity(Pageable pageable, RollbookSearch search, Principal principal);

    Page<RollbookPageResp> sellRollbookPagingList(Pageable pageable, RollbookSearch search, Principal principal);

    Page<Rollbook> selAdminRollbookEntity(Pageable pageable, RollbookSearch search);

    Page<RollbookPageResp> selAdminRollbookPagingList(Pageable pageable, RollbookSearch search);

    List<CorrectRollbookResp> selAdminCorrectionRollbookList();

    Boolean insCorrentRollbookRequest(CorrectRollbookReq req);

    // 어드민 근태 조회
    RespAdminRollbook selAdminEditRollbook(Long rollbookNo);

    Boolean udtAdminEditRollbook(AdminRollbookUdtReq req);

    // 어드민 삭제
    RollbookPageResp delRollbook(Long rollBookNo);

    // Boolean delAllRollbooks(DelRollbookReq req);

    // 근태 기준 시간 수정
    Boolean udtRollbookSetting(RollbookSettingUdtReq req);
    // 자정 근태 처리
    void insMidnightRollbook();
    // 연차 자동 생성
    void resetAnnualSetting();
}