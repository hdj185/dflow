package com.dflow.admin.approval.requestDto;

import com.dflow.entity.AttachFile;
import com.dflow.entity.DocumentType;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDocTypeInsReq {
    private String docFormName;         //문서양식명
    private String docFormCode;         //문서양식코드
    private String docFormUseFlag;      // 사용여부
    private Long folderNo;              // 파일 상위폴더 번호
    private String contents;            //양식 내용(html 변환 전)
    private Integer orderValue;         //정렬 번호

    //dto -> entity 변환
    public DocumentType of(AttachFile attachFile) {
        return DocumentType.builder()
                .docFormName(docFormName)
                .docFormCode(docFormCode)
                .docFormUseFlag(docFormUseFlag)
                .folderNo(folderNo)
                .fileAttachNo(attachFile.getFileAttachNo())
                .orderValue(orderValue)
                .build();
    }
}
