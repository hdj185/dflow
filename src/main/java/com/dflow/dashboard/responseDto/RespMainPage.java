package com.dflow.dashboard.responseDto;

import com.dflow.entity.Approval;
import com.dflow.entity.Rollbook;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainPage {
    String rollbookStatus;      //근무 상태 - 근무중, 퇴근, 외근 등
    String rollbookOpenTime;    //출근시간
    String rollbookCloseTime;   //퇴근시간
    Long annualTotal;           //총연차
    Long annualUsed;            //연차 사용일
    Long annualRemained;        //연차 잔여일
    Long approvalProgress;      //결재진행수
    Long approvalCompleted;     //결재완료수
    Long approvalReturn;        //결재반려수
    Long approvalWaited;        //결재대기수
    Long approvalReferenced;    //참조문서수

//    public static RespMainPage of(Rollbook rollbook, Approval approval) {
//
//    }
}
