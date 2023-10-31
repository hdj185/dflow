package com.dflow.admin.approval.responseDto;

import com.dflow.approval.responseDto.DocTypeResp;
import com.dflow.entity.DocumentType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DocFormListResp {

    private Long docFormTypeNo;     //문서양식번호
    private String docFormName;    //문서양식명
    private Integer orderValue;    //문서양식정렬순서

    //entity -> dto
    public static DocFormListResp of(DocumentType documentType) {
        return DocFormListResp.builder()
                .docFormTypeNo(documentType.getDocFormTypeNo())
                .docFormName(documentType.getDocFormName())
                .orderValue(documentType.getOrderValue())
                .build();
    }

    //entity 리스트 -> dto 리스트
    public static List<DocFormListResp> of(List<DocumentType> entityList) {
        List<DocFormListResp> list = new ArrayList<>();
        for(DocumentType entity : entityList)
            list.add(of(entity));
        return list;
    }

}
