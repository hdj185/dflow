package com.dflow.approval.responseDto;

import com.dflow.entity.DocumentType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeResp {
    private Long docFormTypeNo;     //문서양식번호
    private String docFormName;    //문서양식명

    //entity -> dto
    public static DocTypeResp of(DocumentType documentType) {
        return DocTypeResp.builder()
                .docFormTypeNo(documentType.getDocFormTypeNo())
                .docFormName(documentType.getDocFormName())
                .build();
    }

    //entity 리스트 -> dto 리스트
    public static List<DocTypeResp> of(List<DocumentType> entityList) {
        List<DocTypeResp> list = new ArrayList<>();
        for(DocumentType entity : entityList)
            list.add(of(entity));
        return list;
    }
}
