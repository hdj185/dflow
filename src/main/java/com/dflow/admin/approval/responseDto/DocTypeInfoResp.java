package com.dflow.admin.approval.responseDto;

import com.dflow.entity.DocumentType;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DocTypeInfoResp {

    /** 결재문서 양식 번호 **/
    private Long docTypeNo;

    /** 결재문서 양식 명 **/
    private String docTypeName;

    /** 결재문서 양식 사용 유무 **/
    private String typeFlag;

    public static  DocTypeInfoResp toDto(DocumentType documentType){
        return new DocTypeInfoResp(
                documentType.getDocFormTypeNo(),
                documentType.getDocFormName(),
                documentType.getDocFormUseFlag()
        );
    }
}
