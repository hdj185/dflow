package com.dflow.project.responseDto;

import com.dflow.entity.CodeInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespProjCode {
    private String codeName;
    private String codeAccount;
    private String codeValue;

    //entity -> dto
    public static RespProjCode of(CodeInfo codeInfo) {
        return RespProjCode.builder()
                .codeName(codeInfo.getCodeName())
                .codeAccount(codeInfo.getCodeAccount())
                .codeValue(codeInfo.getCodeValue())
                .build();
    }

    public static List<RespProjCode> of(List<CodeInfo> entityList) {
        List<RespProjCode> list = new ArrayList<>();
        for(CodeInfo code : entityList)
            list.add(of(code));
        return list;
    }
}
