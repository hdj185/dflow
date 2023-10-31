package com.dflow.approval.responseDto;

import com.dflow.entity.CodeInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocAprvCodeResp {


    private String codeName;
    private String codeAccount;
    private String codeValue;


    public static DocAprvCodeResp toDto(CodeInfo codeInfo) {
        return DocAprvCodeResp.builder()
                .codeName(codeInfo.getCodeName())
                .codeAccount(codeInfo.getCodeAccount())
                .codeValue(codeInfo.getCodeValue())
                .build();
    }

    //CodeInfo entity -> toDto
    public static List<DocAprvCodeResp> toDto(List<CodeInfo> entityList) {
        List<DocAprvCodeResp> list = new ArrayList<>();
        for(CodeInfo code : entityList)
            list.add(toDto(code));
        return list;
    }

}
