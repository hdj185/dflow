package com.dflow.approval.requestDto;

import com.dflow.entity.Approval;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AprvUdtReq {

    private Long docNo;                                                 //결재 문서 번호
    private String aprvOpinion;                                         //결재 의견
    private String aprvResult;                                          //결재 결과 (승인/반려)
    private String aprvContent;                                         //문서 내용 (사인함)
    private Long memberNo;    //결재하는 사람 no
}
