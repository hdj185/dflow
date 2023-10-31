package com.dflow.approval.responseDto;

import com.dflow.entity.DocumentApproval;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempDocAprvDetailResp {

    private Long docNo;
    private Long docFormNo;
    private String docFormName;
    private String docFormCode;
    private String docContent;

    public static TempDocAprvDetailResp toDto(DocumentApproval documentApproval){
        return TempDocAprvDetailResp.builder()
                .docNo(documentApproval.getDocNo())
                .docFormNo(documentApproval.getDocFormNo())
                .docFormName(documentApproval.getDocumentTypeInfo().getDocFormName())
                .docFormCode(documentApproval.getDocumentTypeInfo().getDocFormCode())
                .docContent(documentApproval.getDocCn())
                .build();
    }
}
