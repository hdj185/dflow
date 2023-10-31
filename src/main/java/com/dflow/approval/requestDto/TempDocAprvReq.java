package com.dflow.approval.requestDto;

import com.dflow.entity.DocumentApproval;
import com.dflow.entity.MemberInfo;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempDocAprvReq {

    private Long docNo;                                     //임시 저장 문서 번호
    private String docTTL;                                          //문서 제목
    private String docCn;                                           //문서 내용
    private Long docFormNo;                                         //문서양식번호


    public DocumentApproval toEntity(){
        return DocumentApproval.builder()
                .docTTL(this.docTTL)
                .docCn(this.docCn)
                .docState("임시저장")
                .docRecovery("N")
                .docFlag("Y")
                .fileAttachNo(null)
                .docFormNo(this.docFormNo)
                .build();
    }

}
