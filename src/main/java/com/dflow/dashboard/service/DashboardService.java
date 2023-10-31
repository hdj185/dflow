package com.dflow.dashboard.service;

import com.dflow.dashboard.requestDto.ReqMainRollbook;
import com.dflow.dashboard.responseDto.*;
import com.dflow.entity.Rollbook;

import java.util.List;


public interface DashboardService  {

    RespMainStandard selStandardTimeMain();
    RespMainMemberInfo selMemberInfoMain(String memberId);

    List<RespMainBoard> selNoticeMain();

    List<RespMainBoard> selBoardMain();

    List<RespMainProject> selProjectMain(String memberId);
    RespMainAprv selAprvMain(String memberId);

    RespMainAnnual selAnnualMain(String memberId);
    boolean insRollbookMain(String memberId, ReqMainRollbook request);
    boolean udtRollbookMain(String memberId, ReqMainRollbook request);
    RespMainRollbook selRollbookMain(String memberId);

}
