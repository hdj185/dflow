package com.dflow.rollbook.responseDto;

import com.dflow.entity.CodeInfo;
import com.dflow.project.responseDto.RespProjCode;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespAnnualCode {
    private String codeName;
    private String codeAccount;
    private String codeValue;

    //entity -> dto
    public static RespAnnualCode of(CodeInfo codeInfo) {
        return RespAnnualCode.builder()
                .codeName(codeInfo.getCodeName())
                .codeAccount(codeInfo.getCodeAccount())
                .codeValue(codeInfo.getCodeValue())
                .build();
    }

    public static List<RespAnnualCode> of(List<CodeInfo> entityList) {
        List<RespAnnualCode> list = new ArrayList<>();
        for(CodeInfo code : entityList)
            list.add(of(code));
        return list;
    }
}
