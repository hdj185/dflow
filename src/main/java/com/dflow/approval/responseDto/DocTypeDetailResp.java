package com.dflow.approval.responseDto;

import com.dflow.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeDetailResp {
    private Long docFormTypeNo;     //문서양식번호
    private String docFormName;    //문서양식명
    private String docFormCode;    //문서양식코드
    private String docFormContents;    //양식 내용

    //entity -> dto
    public static DocTypeDetailResp of(DocumentType documentType, String contents) {
        return DocTypeDetailResp.builder()
                .docFormTypeNo(documentType.getDocFormTypeNo())
                .docFormName(documentType.getDocFormName())
                .docFormCode(documentType.getDocFormCode())
                .docFormContents(contents)
                .build();
    }
}
