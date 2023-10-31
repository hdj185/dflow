package com.dflow.admin.approval.responseDto;

import com.dflow.entity.DocumentType;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DocFormResp {

    /** 결재문서 양식 번호 **/
    private Long docFormNo;

    /** 결재문서 양식 명 **/
    private String docFormName;

    /** 결재문서 양식 사용 유무 **/
    private String typeFlag;

    /** 상위 폴더 번호 **/
    private Long typeFolderNo;

    /** 문서 양식 코드 **/
    private String docFormCode;

    /** 첨부 파일 번호 **/
    private Long attachFileNo;


    public static DocFormResp toDto(DocumentType documentType){
        return DocFormResp.builder()
                .docFormNo(documentType.getDocFormTypeNo())
                .docFormName(documentType.getDocFormName())
                .typeFlag(documentType.getDocFormUseFlag())
                .typeFolderNo(documentType.getFolderNo())
                .docFormCode(documentType.getDocFormCode())
                .attachFileNo(documentType.getFileAttachNo())
                .build();
    }
}
