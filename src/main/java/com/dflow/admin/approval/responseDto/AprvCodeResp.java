package com.dflow.admin.approval.responseDto;

import com.dflow.entity.CodeInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AprvCodeResp {

    private String codeName;

    private String codeAccount;

    //entity -> dto
    public static AprvCodeResp of(CodeInfo codeInfo) {
        return AprvCodeResp.builder()
                .codeName(codeInfo.getCodeName())
                .codeAccount(codeInfo.getCodeAccount())
                .build();
    }

    public static List<AprvCodeResp> of(List<CodeInfo> entityList) {
        List<AprvCodeResp> list = new ArrayList<>();
        for(CodeInfo code : entityList)
            list.add(of(code));
        return list;
    }

}
